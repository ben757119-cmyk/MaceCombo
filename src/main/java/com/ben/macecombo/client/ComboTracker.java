package com.ben.macecombo.client;

import com.ben.macecombo.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ComboTracker {
	// Target UUID -> time when we disabled their shield (ms)
	private static final Map<UUID, Long> recentShieldDisables = new ConcurrentHashMap<>();

	// Active overlay
	public static boolean overlayActive = false;
	public static long overlayStartTime = 0;
	public static String currentOverlayText = "";

	public static void onAttack(Player attacker, Entity target) {
		if (!ModConfig.INSTANCE.enabled) return;
		if (!(attacker instanceof net.minecraft.client.player.LocalPlayer)) return;
		if (!(target instanceof LivingEntity living)) return;
		if (ModConfig.INSTANCE.onlyPlayers && !(target instanceof Player)) return;

		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;

		ItemStack mainHand = attacker.getMainHandItem();
		long now = System.currentTimeMillis();

		// Check if this is a shield disable (axe on blocking target)
		if (mainHand.getItem() instanceof AxeItem && living.isBlocking()) {
			recentShieldDisables.put(target.getUUID(), now);
			return;
		}

		// Check if this is a mace smash within window
		if (isMace(mainHand) && isSmashAttack(attacker)) {
			Long disableTime = recentShieldDisables.get(target.getUUID());
			if (disableTime != null && (now - disableTime) <= ModConfig.INSTANCE.comboWindowMs) {
				triggerCombo(target);
				recentShieldDisables.remove(target.getUUID());
			}
		}
	}

	private static boolean isMace(ItemStack stack) {
		return stack.is(Items.MACE);
	}

	private static boolean isSmashAttack(Player player) {
		// Smash requires falling at least 1.5 blocks and not on ground
		return player.fallDistance >= 1.5f && !player.onGround();
	}

	private static void triggerCombo(Entity target) {
		ModConfig cfg = ModConfig.INSTANCE;
		Minecraft mc = Minecraft.getInstance();

		if (cfg.showChat && mc.player != null) {
			mc.player.displayClientMessage(Component.literal(cfg.chatMessage), false);
		}

		if (cfg.showOverlay) {
			overlayActive = true;
			overlayStartTime = System.currentTimeMillis();
			currentOverlayText = cfg.overlayText;
		}
	}

	public static void tick() {
		// Cleanup old entries
		long now = System.currentTimeMillis();
		recentShieldDisables.entrySet().removeIf(e -> now - e.getValue() > ModConfig.INSTANCE.comboWindowMs + 1000);

		// Overlay timeout
		if (overlayActive) {
			long elapsed = now - overlayStartTime;
			if (elapsed > ModConfig.INSTANCE.overlayDurationTicks * 50L) {
				overlayActive = false;
			}
		}
	}

	public static void clear() {
		recentShieldDisables.clear();
		overlayActive = false;
	}
}
