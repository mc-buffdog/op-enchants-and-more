package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder.Reference;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class CoupDeGraceMixin {
    @Unique
    private static Reference<Enchantment> cachedCoupDeGrace = null;

    @Unique
    private static Reference<Enchantment> getCoupDeGrace(ServerLevel level) {
        if (cachedCoupDeGrace == null) {
            cachedCoupDeGrace = level.registryAccess()
                    .lookup(ModEnchantments.COUP_DE_GRACE.registryKey())
                    .flatMap(reg -> reg.get(ModEnchantments.COUP_DE_GRACE))
                    .orElse(null);
        }
        return cachedCoupDeGrace;
    }

    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;",
                    shift = At.Shift.AFTER
            ),
            name = "totalDamage")
    private float applyExtraCritMultiplier(float totalDamage, Entity entity, @Local(name = "fullStrengthAttack") boolean fullStrengthAttack) {
        if (!fullStrengthAttack) return totalDamage;
        var self = (Player)(Object) this;

        // Should not proc on non-living entities
        if (!(entity instanceof LivingEntity target)) return totalDamage;
        // Should not proc on dead entities
        if (target.isDeadOrDying()) return totalDamage;

        var level = self.level();
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return totalDamage;

        var coupDeGrace = getCoupDeGrace(serverLevel);
        if (coupDeGrace == null) return totalDamage;

        var stack = self.getWeaponItem();
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(coupDeGrace, stack);
        if (enchantmentLevel == 0) return totalDamage;

        float chance;
        float damageMultiplier;
        if (enchantmentLevel == 1) {
            damageMultiplier = 2.0F;
            chance = 0.34F;
        } else if (enchantmentLevel == 2) {
            damageMultiplier = 3.25F;
            chance = 0.38F;
        } else {
            damageMultiplier = 4.5F;
            chance = 0.41F;
        }

        if (self.getRandom().nextDouble() >= chance) {
            return totalDamage;
        }

        // Coup de Grâce hit sound
        serverLevel.playSound(null, entity.blockPosition(), ModSounds.COUP_DE_GRACE, SoundSource.PLAYERS, 1f, 1f);

        // Blood splatter sound
        serverLevel.playSound(null, entity.blockPosition(), ModSounds.SPLATTER, SoundSource.PLAYERS, 0.5f, 1f);

        // Blood particles
        serverLevel.sendParticles(ModParticles.COUP_DE_GRACE_PARTICLE,
                entity.position().x(),
                entity.position().y(),
                entity.position().z(),
                3, 0, 0, 0, 1
        );

        return totalDamage * damageMultiplier;
    }

    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;causeExtraKnockback(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/phys/Vec3;)V",
                    shift = At.Shift.AFTER
            ),
            name = "baseDamage"
    )
    private float applySweepingDamage(float baseDamage, @Local(name = "totalDamage") float totalDamage) {
        return totalDamage;
    }

    @ModifyVariable(
            method = "stabAttack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;",
                    shift = At.Shift.AFTER
            ),
            name = "totalDamage"
    )
    private float applyStabCrit(float totalDamage, @Local(name = "target", argsOnly = true) Entity target) {
        return applyExtraCritMultiplier(totalDamage, target, true);
    }
}