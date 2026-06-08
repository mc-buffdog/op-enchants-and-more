package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import net.minecraft.core.Holder.Reference;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LifestealMixin {
    @Shadow
    public abstract float getHealth();

    @Unique
    private static Reference<Enchantment> cachedLifesteal = null;

    @Unique
    private float healthBeforeDamage;

    @Unique
    private static Reference<Enchantment> getLifesteal(ServerLevel level) {
        if (cachedLifesteal == null) {
            cachedLifesteal = level.registryAccess()
                    .lookup(ModEnchantments.LIFESTEAL.registryKey())
                    .flatMap(reg -> reg.get(ModEnchantments.LIFESTEAL))
                    .orElse(null);
        }
        return cachedLifesteal;
    }

    @Inject(
            method = "hurtServer",
            at = @At(value = "HEAD")
    )
    private void captureHealthBeforeDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (level.isClientSide()) return;
        healthBeforeDamage = this.getHealth();
    }

    @Inject(
            method = "hurtServer",
            at = @At(value = "RETURN")
    )
    private void lifesteal(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (level.isClientSide()) return;

        var self = (LivingEntity)(Object)this;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        var lifestealEnchantment = getLifesteal(level);
        if (lifestealEnchantment == null) return;

        var stack = attacker.getWeaponItem();
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(lifestealEnchantment, stack);
        if (enchantmentLevel == 0) return;

        var toHeal = calculateLifesteal(enchantmentLevel, self);
        attacker.heal(toHeal);
    }

    @Unique
    private float calculateLifesteal(int enchantmentLevel, LivingEntity self) {
        float lifestealPercentage;
        if (enchantmentLevel == 1) {
            lifestealPercentage = 0.15F;
        } else if (enchantmentLevel == 2) {
            lifestealPercentage = 0.25F;
        } else if (enchantmentLevel == 3) {
            lifestealPercentage = 0.375F;
        } else if (enchantmentLevel == 4) {
            lifestealPercentage = 0.500F;
        } else {
            lifestealPercentage = 0.680F;
        }

        var currentHealth = self.getHealth();
        OPEnchantsAndMore.LOGGER.info("Health diff: {}", healthBeforeDamage - currentHealth);
        return Math.max(0, healthBeforeDamage - currentHealth) * lifestealPercentage;
    }
}
