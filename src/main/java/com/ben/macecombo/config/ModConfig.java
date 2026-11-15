package com.ben.macecombo.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("macecombo.json");

	public boolean enabled = true;
	public boolean showOverlay = true;
	public boolean showChat = true;
	public String chatMessage = "§a§lSHIELD BREAK → MACE SMASH!";
	public String overlayText = "COMBO!";
	public int overlayDurationTicks = 60; // 3 seconds
	public int overlayColor = 0xFF00FF00; // ARGB green
	public float overlayScale = 2.0f;
	public int overlayX = -1; // -1 = center
	public int overlayY = 40;
	public int comboWindowMs = 3000; // 3 seconds
	public boolean onlyPlayers = true;

	public static ModConfig INSTANCE = new ModConfig();

	public static void load() {
		if (Files.exists(CONFIG_PATH)) {
			try {
				String json = Files.readString(CONFIG_PATH);
				INSTANCE = GSON.fromJson(json, ModConfig.class);
				if (INSTANCE == null) INSTANCE = new ModConfig();
			} catch (Exception e) {
				INSTANCE = new ModConfig();
			}
		} else {
			save();
		}
	}

	public static void save() {
		try {
			Files.createDirectories(CONFIG_PATH.getParent());
			Files.writeString(CONFIG_PATH, GSON.toJson(INSTANCE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
