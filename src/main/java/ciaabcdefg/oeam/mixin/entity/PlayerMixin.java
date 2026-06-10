package ciaabcdefg.oeam.mixin.entity;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.GiantSlayerEnchantment;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import ciaabcdefg.oeam.util.ModEnchantmentUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;",
                    shift = At.Shift.AFTER
            ),
            name = "totalDamage")
    private float modifyDamage(float totalDamage, Entity entity, @Local(name = "fullStrengthAttack") boolean fullStrengthAttack) {
        var self = (Player)(Object) this;

        if (!(entity instanceof LivingEntity target))
            return totalDamage;
        if (target.isDeadOrDying())
            return totalDamage;
        if (!(self.level() instanceof ServerLevel serverLevel))
            return totalDamage;

        var itemStack = self.getWeaponItem();
        int giantSlayerLevel = ModEnchantmentUtil.getEnchantmentLevel(itemStack, ModEnchantments.GIANT_SLAYER);

        if (giantSlayerLevel > 0) {
            totalDamage = giantSlayerPipe(self, target, serverLevel, totalDamage, giantSlayerLevel);
        }

        if (fullStrengthAttack) {
            totalDamage = coupDeGracePipe(self, target, serverLevel, totalDamage);
        }

        return totalDamage;
    }

    @Unique
    private static float coupDeGracePipe(Player player, LivingEntity target, ServerLevel level, float damage) {
        double chance = player.getAttributeValue(ModAttributes.CRIT_CHANCE);
        if (player.getRandom().nextDouble() >= chance) {
            return damage;
        }

        float damageMultiplier = Math.max((float)player.getAttributeValue(ModAttributes.CRIT_DAMAGE_MUL), 1.0F);
        var targetPos = target.blockPosition();

        level.playSound(null, targetPos, ModSounds.COUP_DE_GRACE, SoundSource.PLAYERS, 1f, 1f);
        level.playSound(null, targetPos, ModSounds.SPLATTER, SoundSource.PLAYERS, 0.5f, 1f);
        level.sendParticles(ModParticles.COUP_DE_GRACE_PARTICLE,
                target.position().x(),
                target.position().y(),
                target.position().z(),
                3, 0, 0, 0, 1
        );

        return damage * damageMultiplier;
    }

    @Unique
    private static float giantSlayerPipe(Player player, LivingEntity target, ServerLevel level, float damage, int giantSlayerLevel) {
        float targetMaxHealth = target.getMaxHealth();
        return damage + targetMaxHealth * GiantSlayerEnchantment.DAMAGE_PERCENT_MAX_HP.calculate(giantSlayerLevel);
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
        return modifyDamage(totalDamage, target, true);
    }
}