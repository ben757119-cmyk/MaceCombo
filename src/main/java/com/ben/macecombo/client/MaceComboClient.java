package com.ben.macecombo.client;

import com.ben.macecombo.config.ModConfig;
import com.ben.macecombo.gui.ClickGuiScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class MaceComboClient implements ClientModInitializer {
	public static KeyMapping openGuiKey;

	@Override
	public void onInitializeClient() {
		ModConfig.load();

		openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.macecombo.opengui",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_INSERT,
				"category.macecombo"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			ComboTracker.tick();

			while (openGuiKey.consumeClick()) {
				if (client.screen == null) {
					client.setScreen(new ClickGuiScreen());
				}
			}
		});

		HudRenderCallback.EVENT.register((graphics, tickCounter) -> {
			renderOverlay(graphics);
		});
	}

	private void renderOverlay(GuiGraphics graphics) {
		if (!ComboTracker.overlayActive || !ModConfig.INSTANCE.showOverlay) return;

		Minecraft mc = Minecraft.getInstance();
		ModConfig cfg = ModConfig.INSTANCE;

		int screenWidth = mc.getWindow().getGuiScaledWidth();

		float scale = cfg.overlayScale;
		String text = ComboTracker.currentOverlayText;

		int textWidth = (int) (mc.font.width(text) * scale);
		int x = cfg.overlayX < 0 ? (screenWidth - textWidth) / 2 : cfg.overlayX;
		int y = cfg.overlayY;

		// Fade out near end
		long elapsed = System.currentTimeMillis() - ComboTracker.overlayStartTime;
		long total = cfg.overlayDurationTicks * 50L;
		float alpha = 1.0f;
		if (elapsed > total * 0.7) {
			alpha = 1.0f - (float) (elapsed - total * 0.7) / (float) (total * 0.3);
		}
		alpha = Math.max(0f, Math.min(1f, alpha));

		int color = cfg.overlayColor;
		int a = (int) (((color >> 24) & 0xFF) * alpha);
		int rgb = color & 0x00FFFFFF;
		int finalColor = (a << 24) | rgb;

		graphics.pose().pushPose();
		graphics.pose().translate(x, y, 0);
		graphics.pose().scale(scale, scale, 1f);
		graphics.drawString(mc.font, text, 0, 0, finalColor, true);
		graphics.pose().popPose();
	}
}
