package com.ben.macecombo.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Placeholder / extension point.
 * Shield disable is primarily detected client-side via attack + isBlocking + axe.
 * Server-side disable happens via LivingEntity.disableShield, but for client prediction
 * the attack mixin is sufficient for the combo window.
 */
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	// Can be extended later for more precise shield cooldown tracking if needed.
}
