package ciaabcdefg.oeam.mixin.livingentity;

import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.LifestealEnchantment;
import ciaabcdefg.oeam.util.ModDamageSourceUtil;
import ciaabcdefg.oeam.util.ModEnchantmentUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityHurtMixin {
    @Unique
    private float healthBeforeDamage;

    @Inject(
            method = "hurtServer",
            at = @At(value = "HEAD")
    )
    private void captureHealthBeforeDamage(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity)(Object)this;
        healthBeforeDamage = self.getHealth();
    }

    @Inject(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;resolveMobResponsibleForDamage(Lnet/minecraft/world/damagesource/DamageSource;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void modifyInvulnerableTime(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity)(Object) this;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        var stack = attacker.getWeaponItem();
        int butterflyLevel = ModEnchantmentUtil.getEnchantmentLevel(stack, ModEnchantments.BUTTERFLY);

        if (butterflyLevel == 0) {
            return;
        }

        self.invulnerableTime = 1;
    }

    @Inject(
            method = "hurtServer",
            at = @At(value = "RETURN")
    )
    private void lifesteal(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity)(Object)this;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        var baseLifesteal = attacker.getAttributeValue(ModAttributes.LIFESTEAL);
        float weaponLifesteal = 0;

        if (ModDamageSourceUtil.getWeaponFromSource(source) instanceof ItemStack weapon) {
            weaponLifesteal = getWeaponLifesteal(weapon);
        }

        var totalLifesteal = baseLifesteal + weaponLifesteal;
        var toHeal = calculateLifesteal(healthBeforeDamage, (float)totalLifesteal, self);
        if (toHeal > 0) {
            attacker.heal(toHeal);
        }
    }

    @Unique
    private static float calculateLifesteal(float healthBeforeDamage, float lifesteal, LivingEntity self) {
        var currentHealth = self.getHealth();
        return Math.max(0, healthBeforeDamage - currentHealth) * lifesteal;
    }

    @Unique
    private static float getWeaponLifesteal(ItemStack weapon) {
        var level = ModEnchantmentUtil.getEnchantmentLevel(weapon, ModEnchantments.LIFESTEAL);
        return LifestealEnchantment.calculateLifesteal(level);
    }
}
