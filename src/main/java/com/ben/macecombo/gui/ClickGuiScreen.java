package com.ben.macecombo.gui;

import com.ben.macecombo.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClickGuiScreen extends Screen {
	private EditBox chatMessageBox;
	private EditBox overlayTextBox;
	private EditBox durationBox;
	private EditBox scaleBox;
	private EditBox windowBox;

	private static final int PANEL_WIDTH = 280;
	private static final int PANEL_HEIGHT = 260;
	private int panelX, panelY;

	public ClickGuiScreen() {
		super(Component.literal("Mace Combo ClickGUI"));
	}

	@Override
	protected void init() {
		panelX = (this.width - PANEL_WIDTH) / 2;
		panelY = (this.height - PANEL_HEIGHT) / 2;

		int y = panelY + 25;
		int left = panelX + 10;
		int fieldWidth = PANEL_WIDTH - 20;

		this.addRenderableWidget(Button.builder(
				Component.literal("Enabled: " + (ModConfig.INSTANCE.enabled ? "§aON" : "§cOFF")),
				b -> {
					ModConfig.INSTANCE.enabled = !ModConfig.INSTANCE.enabled;
					b.setMessage(Component.literal("Enabled: " + (ModConfig.INSTANCE.enabled ? "§aON" : "§cOFF")));
					ModConfig.save();
				}
		).bounds(left, y, fieldWidth, 20).build());
		y += 24;

		this.addRenderableWidget(Button.builder(
				Component.literal("Show Overlay: " + (ModConfig.INSTANCE.showOverlay ? "§aON" : "§cOFF")),
				b -> {
					ModConfig.INSTANCE.showOverlay = !ModConfig.INSTANCE.showOverlay;
					b.setMessage(Component.literal("Show Overlay: " + (ModConfig.INSTANCE.showOverlay ? "§aON" : "§cOFF")));
					ModConfig.save();
				}
		).bounds(left, y, fieldWidth / 2 - 2, 20).build());

		this.addRenderableWidget(Button.builder(
				Component.literal("Show Chat: " + (ModConfig.INSTANCE.showChat ? "§aON" : "§cOFF")),
				b -> {
					ModConfig.INSTANCE.showChat = !ModConfig.INSTANCE.showChat;
					b.setMessage(Component.literal("Show Chat: " + (ModConfig.INSTANCE.showChat ? "§aON" : "§cOFF")));
					ModConfig.save();
				}
		).bounds(left + fieldWidth / 2 + 2, y, fieldWidth / 2 - 2, 20).build());
		y += 28;

		this.addRenderableWidget(Button.builder(Component.literal("Chat Message:"), b -> {}).bounds(left, y, fieldWidth, 12).build());
		y += 14;
		chatMessageBox = new EditBox(this.font, left, y, fieldWidth, 18, Component.literal("Chat"));
		chatMessageBox.setValue(ModConfig.INSTANCE.chatMessage);
		chatMessageBox.setMaxLength(256);
		this.addRenderableWidget(chatMessageBox);
		y += 24;

		this.addRenderableWidget(Button.builder(Component.literal("Overlay Text:"), b -> {}).bounds(left, y, fieldWidth, 12).build());
		y += 14;
		overlayTextBox = new EditBox(this.font, left, y, fieldWidth, 18, Component.literal("Overlay"));
		overlayTextBox.setValue(ModConfig.INSTANCE.overlayText);
		overlayTextBox.setMaxLength(64);
		this.addRenderableWidget(overlayTextBox);
		y += 24;

		this.addRenderableWidget(Button.builder(Component.literal("Overlay Duration (ticks):"), b -> {}).bounds(left, y, 140, 12).build());
		durationBox = new EditBox(this.font, left + 145, y - 2, 60, 16, Component.literal("Duration"));
		durationBox.setValue(String.valueOf(ModConfig.INSTANCE.overlayDurationTicks));
		this.addRenderableWidget(durationBox);
		y += 22;

		this.addRenderableWidget(Button.builder(Component.literal("Overlay Scale:"), b -> {}).bounds(left, y, 140, 12).build());
		scaleBox = new EditBox(this.font, left + 145, y - 2, 60, 16, Component.literal("Scale"));
		scaleBox.setValue(String.valueOf(ModConfig.INSTANCE.overlayScale));
		this.addRenderableWidget(scaleBox);
		y += 22;

		this.addRenderableWidget(Button.builder(Component.literal("Combo Window (ms):"), b -> {}).bounds(left, y, 140, 12).build());
		windowBox = new EditBox(this.font, left + 145, y - 2, 60, 16, Component.literal("Window"));
		windowBox.setValue(String.valueOf(ModConfig.INSTANCE.comboWindowMs));
		this.addRenderableWidget(windowBox);
		y += 28;

		this.addRenderableWidget(Button.builder(Component.literal("§aSave & Close"), b -> {
			saveFromFields();
			this.onClose();
		}).bounds(left, y, fieldWidth / 2 - 2, 20).build());

		this.addRenderableWidget(Button.builder(Component.literal("§cCancel"), b -> {
			this.onClose();
		}).bounds(left + fieldWidth / 2 + 2, y, fieldWidth / 2 - 2, 20).build());
	}

	private void saveFromFields() {
		ModConfig.INSTANCE.chatMessage = chatMessageBox.getValue();
		ModConfig.INSTANCE.overlayText = overlayTextBox.getValue();
		try {
			ModConfig.INSTANCE.overlayDurationTicks = Integer.parseInt(durationBox.getValue().trim());
		} catch (NumberFormatException ignored) {}
		try {
			ModConfig.INSTANCE.overlayScale = Float.parseFloat(scaleBox.getValue().trim());
		} catch (NumberFormatException ignored) {}
		try {
			ModConfig.INSTANCE.comboWindowMs = Integer.parseInt(windowBox.getValue().trim());
		} catch (NumberFormatException ignored) {}
		ModConfig.save();
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
		this.renderBackground(graphics, mouseX, mouseY, delta);
		graphics.fill(panelX - 2, panelY - 2, panelX + PANEL_WIDTH + 2, panelY + PANEL_HEIGHT + 2, 0xFF000000);
		graphics.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + PANEL_HEIGHT, 0xEE1A1A1A);
		graphics.drawCenteredString(this.font, "§lMace Combo §7[INS]", this.width / 2, panelY + 8, 0xFF55FF55);
		super.render(graphics, mouseX, mouseY, delta);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
