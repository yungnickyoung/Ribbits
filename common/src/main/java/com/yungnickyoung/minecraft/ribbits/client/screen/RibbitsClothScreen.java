package com.yungnickyoung.minecraft.ribbits.client.screen;

import com.yungnickyoung.minecraft.ribbits.config.RibbitsConfig;
import com.yungnickyoung.minecraft.ribbits.util.GeoIP;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RibbitsClothScreen {

    public static Screen create(Screen parent) {
        RibbitsConfig config = AutoConfig.getConfigHolder(RibbitsConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("text.autoconfig.ribbits-1_21_10.title"));

        ConfigEntryBuilder eb = builder.entryBuilder();

        var general = builder.getOrCreateCategory(
                Component.translatable("text.autoconfig.ribbits-1_21_10.option.general")
        );

        general.addEntry(
                eb.startBooleanToggle(
                                Component.translatable("text.autoconfig.ribbits-1_21_10.option.general.prideFlagAllYear"),
                                config.general.prideFlagAllYear
                        )
                        .setDefaultValue(false)
                        .setTooltip(Component.translatable(
                                "text.autoconfig.ribbits-1_21_10.option.general.prideFlagAllYear.@Tooltip"
                        )).setSaveConsumer(val -> config.general.prideFlagAllYear = val)
                        .build()
        );

        if (GeoIP.isInChina()) {
            general.addEntry(
                    eb.startBooleanToggle(
                                    Component.translatable("text.autoconfig.ribbits-1_21_10.option.general.disablePrideFlagCN"),
                                    config.general.disablePrideFlagCN
                            )
                            .setDefaultValue(true)
                            .setTooltip(Component.translatable(
                                    "text.autoconfig.ribbits-1_21_10.option.general.disablePrideFlagCN.@Tooltip"
                            )).setSaveConsumer(val -> config.general.disablePrideFlagCN = val)
                            .build()
            );
        }

        var network = builder.getOrCreateCategory(
                Component.translatable("text.autoconfig.ribbits-1_21_10.option.network")
        );

        network.addEntry(
                eb.startStrField(
                                Component.translatable("text.autoconfig.ribbits-1_21_10.option.network.proxyHost"),
                                config.network.proxyHost
                        ).setTooltip(Component.translatable(
                                "text.autoconfig.ribbits-1_21_10.option.network.proxyHost.@Tooltip"
                        )).setSaveConsumer(val -> config.network.proxyHost = val)
                        .build()
        );

        network.addEntry(
                eb.startIntField(
                                Component.translatable("text.autoconfig.ribbits-1_21_10.option.network.proxyPort"),
                                config.network.proxyPort
                        ).setTooltip(Component.translatable(
                                "text.autoconfig.ribbits-1_21_10.option.network.proxyPort.@Tooltip"
                        )).setSaveConsumer(val -> config.network.proxyPort = val)
                        .build()
        );

        network.addEntry(
                eb.startStrField(
                                Component.translatable("text.autoconfig.ribbits-1_21_10.option.network.proxyUsername"),
                                config.network.proxyUsername
                        ).setTooltip(Component.translatable(
                                "text.autoconfig.ribbits-1_21_10.option.network.proxyUsername.@Tooltip"
                        )).setSaveConsumer(val -> config.network.proxyUsername = val)
                        .build()
        );

        network.addEntry(
                eb.startStrField(
                                Component.translatable("text.autoconfig.ribbits-1_21_10.option.network.proxyPassword"),
                                config.network.proxyPassword
                        ).setTooltip(Component.translatable(
                                "text.autoconfig.ribbits-1_21_10.option.network.proxyPassword.@Tooltip"
                        )).setSaveConsumer(val -> config.network.proxyPassword = val)
                        .build()
        );

        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(RibbitsConfig.class).save());

        return builder.build();
    }
}
