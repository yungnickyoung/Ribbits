package com.yungnickyoung.minecraft.ribbits.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yungnickyoung.minecraft.ribbits.RibbitsCommon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GeoIP {
    private static Boolean inChina = null;
    private static boolean checked = false;

    public static void init() {
        if (checked) return;
        checked = true;

        new Thread(() -> inChina = checkChina(), "GeoIP-Check").start();
    }

    public static boolean isInChina() {
        return Boolean.TRUE.equals(inChina);
    }

    private static boolean checkChina() {
        try {
            URL url = URI.create("https://ip-api.com/json/?fields=status,countryCode").toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String jsonText = br.lines().collect(Collectors.joining());
                RibbitsCommon.LOGGER.info("GeoIP Raw Response: {}", jsonText);

                JsonObject root = JsonParser.parseString(jsonText).getAsJsonObject();
                if ("success".equals(root.get("status").getAsString())) {
                    String countryCode = root.get("countryCode").getAsString();
                    return "CN".equals(countryCode);
                }
                return false;
            }
        } catch (Exception e) {
            RibbitsCommon.LOGGER.error("GeoIP check failed", e);
            return false;
        }
    }
}