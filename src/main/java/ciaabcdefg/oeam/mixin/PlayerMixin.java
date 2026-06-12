package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.CleaveEnchantment;
import ciaabcdefg.oeam.enchantment.custom.CoupDeGraceEnchantment;
import ciaabcdefg.oeam.enchantment.custom.GiantSlayerEnchantment;
import ciaabcdefg.oeam.mixin.invoker.PlayerInvoker;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import ciaabcdefg.oeam.util.ModEnchantmentUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    //-- MODIFY DAMAGE (COUP DE GRACE/GIANT SLAYER) --//
    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;",
                    shift = At.Shift.AFTER
            ),
            name = "totalDamage")
    private float modifyDamage(
            float totalDamage,
            Entity entity,
            @Local(name = "fullStrengthAttack")
            boolean fullStrengthAttack
    ) {
        var self = (Player)(Object) this;

        if (!(entity instanceof LivingEntity target))
            return totalDamage;
        if (target.isDeadOrDying())
            return totalDamage;
        if (!(self.level() instanceof ServerLevel serverLevel))
            return totalDamage;

        var itemStack = self.getWeaponItem();
        if (itemStack == null) return totalDamage;

        int giantSlayerLevel = ModEnchantmentUtil.getEnchantmentLevel(itemStack, ModEnchantments.GIANT_SLAYER);
        int coupDeGraceLevel = ModEnchantmentUtil.getEnchantmentLevel(itemStack, ModEnchantments.COUP_DE_GRACE);

        if (giantSlayerLevel > 0) {
            totalDamage = giantSlayerPipe(self, target, serverLevel, totalDamage, giantSlayerLevel);
        }

        if (fullStrengthAttack && coupDeGraceLevel > 0) {
            totalDamage = coupDeGracePipe(self, target, serverLevel, totalDamage, coupDeGraceLevel);
        }

        return totalDamage;
    }

    @Unique
    private static float coupDeGracePipe(Player player, LivingEntity target, ServerLevel level, float damage, int coupDeGraceLevel) {
        float chance = CoupDeGraceEnchantment.calculateCritChance(coupDeGraceLevel);
        if (player.getRandom().nextDouble() >= chance) {
            return damage;
        }

        float damageMultiplier = CoupDeGraceEnchantment.calculateCritDamageMul(coupDeGraceLevel);
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //-- CLEAVE --//
    @Unique
    int cleaveLevel = 0;

    @ModifyExpressionValue(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isSweepAttack(ZZZ)Z")
    )
    private boolean modifySweepCondition(boolean original) {
        var self = (Player)(Object)this;
        var stack = self.getWeaponItem();
        var cleaveLevel = ModEnchantmentUtil.getEnchantmentLevel(stack, ModEnchantments.CLEAVE);
        if (cleaveLevel == 0) {
            this.cleaveLevel = 0;
            return original;
        }
        this.cleaveLevel = cleaveLevel;
        return false;
    }

    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;causeExtraKnockback(Lnet/minecraft/world/entity/Entity;FLnet/minecraft/world/phys/Vec3;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void cleave(
            Entity entity,
            CallbackInfo ci,
            @Local(name = "totalDamage") float totalDamage,
            @Local(name = "damageSource") DamageSource damageSource
    ) {
        if (cleaveLevel == 0) return;

        var self = (Player)(Object)this;
        if (!(entity instanceof LivingEntity target)) return;

        var invoker = (PlayerInvoker)self;
        invoker.invokePlayServerSideSound(ModSounds.CLEAVE);

        if (!(self.level() instanceof ServerLevel level)) return;

        float sweepMultiplier = 1F;
        float range = (float)self.getAttributeValue(ModAttributes.SWEEPING_AREA);

        var stack = self.getWeaponItem();
        if (stack.is(ItemTags.AXES)) {
            sweepMultiplier = 7.0F;
        }

        float rangeSqr = range * range;
        float cleaveDamage = 1.0F + ((float)self.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) * sweepMultiplier) * totalDamage;

        var aabb = target.getBoundingBox().inflate(
                range,
                range * 0.2,
                range
        );

        var nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);
        var selfPos = self.position();
        var dir = target.position().subtract(selfPos).normalize();
        var cleaveAngle = CleaveEnchantment.CLEAVE_ANGLE.calculate(cleaveLevel);

        for (LivingEntity nearby : nearbyEntities) {
            if (nearby == target || self.isAlliedTo(nearby) || self.distanceToSqr(nearby) > rangeSqr) continue;

            double dot = dir.dot(nearby.position().subtract(selfPos).normalize());
            double angle = Math.toDegrees(Math.acos(dot));

            if (angle > cleaveAngle) continue;

            float enchantedDamage = invoker.invokeGetEnchantedDamage(nearby, cleaveDamage, damageSource);
            if (nearby.hurtServer(level, damageSource, enchantedDamage)) {
                nearby.knockback(0.4F, Mth.sin(self.getYRot() * (float) (Math.PI / 180.0)), -Mth.cos(self.getYRot() * (float) (Math.PI / 180.0)));
                EnchantmentHelper.doPostAttackEffects(level, nearby, damageSource);
            }
        }

        double dx = -Mth.sin(self.getYRot() * (float) (Math.PI / 180.0));
        double dz = Mth.cos(self.getYRot() * (float) (Math.PI / 180.0));
        level.sendParticles(ParticleTypes.SWEEP_ATTACK, self.getX() + dx, self.getY(0.5), self.getZ() + dz, 0, dx, 0.0, dz, 0.0);

        cleaveLevel = 0;
    }
}