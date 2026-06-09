package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.sound.ModSounds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class CleaveMixin {
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
        var cleaveLevel = getEnchantmentLevel(stack, ModEnchantments.CLEAVE);
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

        float bonusSweepRatio;
        float bbSize;
        float range;

        if (cleaveLevel == 1) {
            bonusSweepRatio = 0.010F;
            bbSize = 3F;
            range = 3F;
        } else if (cleaveLevel == 2) {
            bonusSweepRatio = 0.035F;
            bbSize = 5F;
            range = 4F;
        } else if (cleaveLevel == 3) {
            bonusSweepRatio = 0.075F;
            bbSize = 7F;
            range = 7F;
        } else {
            bonusSweepRatio = 0.1F;
            bbSize = 10F;
            range = 10F;
        }

        var stack = self.getWeaponItem();
        if (stack.is(ItemTags.AXES)) {
            bonusSweepRatio *= 7.0F;
        }

        float rangeSqr = range * range;
        float cleaveDamage = 1.0F + ((float)self.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) + bonusSweepRatio) * totalDamage;

        var aabb = target.getBoundingBox().inflate(
                bbSize,
                bbSize * 0.2,
                bbSize
        );

        var nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, aabb);
        var selfPos = self.position();
        var dir = target.position().subtract(selfPos);

        for (LivingEntity nearby : nearbyEntities) {
            if (nearby == target || self.isAlliedTo(nearby) || self.distanceToSqr(nearby) > rangeSqr) continue;
            if (dir.dot(nearby.position().subtract(selfPos)) < 0.0F) continue;

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

    @Unique
    private static int getEnchantmentLevel(ItemStack item, ResourceKey<Enchantment> enchantment) {
        for (var x : item.getEnchantments().entrySet()) {
            var holder = x.getKey();
            if (holder.is(enchantment)) {
                return item.getEnchantments().getLevel(holder);
            }
        }
        return 0;
    }
}
