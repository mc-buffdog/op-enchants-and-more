package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class CoupDeGraceMixin {
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

        double chance = self.getAttributeValue(ModAttributes.CRIT_CHANCE);
        if (self.getRandom().nextDouble() >= chance) {
            return totalDamage;
        }

        float damageMultiplier = (float)self.getAttributeValue(ModAttributes.CRIT_DAMAGE_MUL);

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

    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;doSweepAttack(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/damagesource/DamageSource;F)V"
            )
    )
    private void applySweepingDamage(Player instance, Entity entity, float baseDamage, DamageSource damageSource, float attackStrengthScale, @Local(name = "totalDamage") float totalDamage) {
        var playerInvoker = (PlayerInvoker)instance;
        playerInvoker.invokeDoSweepAttack(entity, totalDamage, damageSource, attackStrengthScale);
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