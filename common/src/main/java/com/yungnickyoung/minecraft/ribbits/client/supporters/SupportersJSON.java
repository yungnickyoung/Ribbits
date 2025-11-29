package com.yungnickyoung.minecraft.ribbits.client.supporters;

import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;
import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;
import com.yungnickyoung.minecraft.ribbits.platform.PlatformHelper;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import me.shedaniel.autoconfig.AutoConfig;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class SupportersJSON {
    private static volatile SupportersJSON instance;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Ribbits Supporter JSON Loader");
        t.setDaemon(true);
        return t;
    });

    private final Set<UUID> supporters = new HashSet<>();
    private final Set<UUID> friends = new HashSet<>();

    private static final URI SUPPORTERS_URI_PRIMARY = URI.create(
            "https://raw.githubusercontent.com/yungnickyoung/Ribbits/refs/heads/1.20.1/supporters.json"
    );
    private static final URI SUPPORTERS_URI_FALLBACK = URI.create(
            "https://cdn.jsdelivr.net/gh/yungnickyoung/Ribbits@1.20.1/supporters.json"
    );

    private static final Path CACHE_PATH = PlatformHelper.getConfigFolder().resolve("ribbits-supporters.json");
    private static final int MAX_ATTEMPTS = 2;
    private static final long RETRY_DELAY_SECONDS = 3;

    private SupportersJSON() {
    }

    public static SupportersJSON get() {
        if (instance == null) {
            synchronized (SupportersJSON.class) {
                if (instance == null) instance = new SupportersJSON();
            }
        }
        return instance;
    }

    public boolean isSupporter(UUID uuid) {
        return supporters.contains(uuid) || friends.contains(uuid);
    }

    public static void populateSupportersList() {
        RibbitsCommon.LOGGER.info("Starting supporters list population...");
        CompletableFuture.runAsync(() -> {
            if (PlatformHelper.isServer()) loadCacheIfAvailable();
            fetchAndCacheSupportersJsonAsync();
        }, EXECUTOR);
    }

    private static void loadCacheIfAvailable() {
        try {
            File file = CACHE_PATH.toFile();
            if (file.exists() && file.canRead()) {
                SupportersJSON data = JSON.loadObjectFromJsonFile(CACHE_PATH, SupportersJSON.class);
                synchronized (SupportersJSON.class) {
                    instance = data;
                }
                RibbitsCommon.LOGGER.info("Loaded cached supporters list.");
            }
        } catch (Exception e) {
            RibbitsCommon.LOGGER.warn("Failed to load cached supporters.json: {}", e.toString());
        }
    }

    private static void saveCache(SupportersJSON data) {
        try (Writer writer = Files.newBufferedWriter(CACHE_PATH)) {
            JSON.gson.toJson(data, writer);
        } catch (IOException e) {
            RibbitsCommon.LOGGER.warn("Failed to save supporters cache: {}", e.toString());
        }
    }

    private static void fetchAndCacheSupportersJsonAsync() {
        HttpClient client = createHttpClient();
        List<URI> uris = List.of(SUPPORTERS_URI_PRIMARY, SUPPORTERS_URI_FALLBACK);
        attemptFetch(client, uris, 0, 1);
    }

    private static HttpClient createHttpClient() {
        RibbitsConfig config = AutoConfig.getConfigHolder(RibbitsConfig.class).getConfig();
        HttpClient.Builder clientBuilder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .followRedirects(HttpClient.Redirect.NORMAL);

        if (config.network.proxyHost != null && !config.network.proxyHost.trim().isEmpty()) {
            ProxySelector proxySelector = ProxySelector.of(
                    new InetSocketAddress(config.network.proxyHost.trim(), config.network.proxyPort)
            );
            clientBuilder.proxy(proxySelector);
            if (config.network.proxyUsername != null && !config.network.proxyUsername.trim().isEmpty()) {
                clientBuilder.authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType() == RequestorType.PROXY) {
                            return new PasswordAuthentication(
                                    config.network.proxyUsername.trim(),
                                    config.network.proxyPassword != null ?
                                            config.network.proxyPassword.toCharArray() : new char[0]
                            );
                        }
                        return null;
                    }
                });
            }
        }
        return clientBuilder.build();
    }

    private static void attemptFetch(HttpClient client, List<URI> uris, int index, int attempt) {
        if (index >= uris.size()) return;

        URI target = uris.get(index);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(target)
                    .timeout(Duration.ofSeconds(20))
                    .header("User-Agent", "Ribbits/" + RibbitsCommon.MC_VERSION_STRING)
                    .header("Accept", "application/json, text/plain, */*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .GET()
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                    .orTimeout(25, TimeUnit.SECONDS)
                    .thenAccept(response -> handleResponse(response, target, client, uris, index, attempt))
                    .exceptionally(ex -> {
                        handleException(ex, client, uris, index, attempt);
                        return null;
                    });

        } catch (Exception e) {
            tryNextUriOrRetry(client, uris, index, attempt);
        }
    }

    private static void handleResponse(HttpResponse<InputStream> response, URI target,
                                       HttpClient client, List<URI> uris, int index, int attempt) {
        if (response.statusCode() == 200) {
            try {
                InputStream bodyStream = response.body();
                if ("gzip".equalsIgnoreCase(response.headers().firstValue("Content-Encoding").orElse("")))
                    bodyStream = new GZIPInputStream(bodyStream);

                try (InputStreamReader reader = new InputStreamReader(bodyStream, StandardCharsets.UTF_8)) {
                    SupportersJSON data = JSON.gson.fromJson(reader, SupportersJSON.class);
                    if (data != null) {
                        synchronized (SupportersJSON.class) {
                            instance = data;
                        }
                        if (PlatformHelper.isServer()) saveCache(data);
                        RibbitsCommon.LOGGER.info("Supporters.json loaded successfully: {} supporters, {} friends",
                                data.supporters.size(), data.friends.size());
                    } else RibbitsCommon.LOGGER.error("Parsed JSON is null");
                }
            } catch (Exception e) {
                RibbitsCommon.LOGGER.error("Error processing supporters.json from {}: {}", target, e.toString());
            }
        } else tryNextUriOrRetry(client, uris, index, attempt);
    }

    private static Void handleException(Throwable ex, HttpClient client, List<URI> uris, int index, int attempt) {
        RibbitsCommon.LOGGER.warn("Failed to fetch {}: {}", uris.get(index), ex.toString());
        tryNextUriOrRetry(client, uris, index, attempt);
        return null;
    }

    private static void tryNextUriOrRetry(HttpClient client, List<URI> uris, int index, int attempt) {
        if (index + 1 < uris.size()) attemptFetch(client, uris, index + 1, attempt);
        else if (attempt < MAX_ATTEMPTS)
            CompletableFuture.delayedExecutor(RETRY_DELAY_SECONDS, TimeUnit.SECONDS, EXECUTOR)
                    .execute(() -> attemptFetch(client, uris, 0, attempt + 1));
        else RibbitsCommon.LOGGER.warn("All attempts exhausted. Using cached data if available. URLs tried: {}", uris);
    }
}
