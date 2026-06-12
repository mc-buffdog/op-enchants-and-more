package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.LifestealEnchantment;
import ciaabcdefg.oeam.util.ModDamageSourceUtil;
import ciaabcdefg.oeam.util.ModEnchantmentUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    //-- ATTRIBUTES -- //
    @ModifyReturnValue(
            method = "createLivingAttributes",
            at = @At("RETURN")
    )
    private static AttributeSupplier.Builder modifyAttributes(AttributeSupplier.Builder original) {
        return original
                .add(ModAttributes.ELYTRA_SPEED_BONUS)
                .add(ModAttributes.LIFESTEAL)
                .add(ModAttributes.CRIT_CHANCE)
                .add(ModAttributes.CRIT_DAMAGE_MUL)
                .add(ModAttributes.SWEEPING_AREA);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //-- LIFESTEAL -- //
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //-- ELYTRA -- //
    @Unique
    private static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    @ModifyArgs(
            method = "updateFallFlyingMovement",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/phys/Vec3;multiply(DDD)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private void modifyDrag(Args args) {
        var entity = (LivingEntity)(Object)this;
        double speedBonus = entity.getAttributeValue(ModAttributes.ELYTRA_SPEED_BONUS);

        double x = args.get(0);
        double y = args.get(1);
        double z = args.get(2);

        double mod = map(speedBonus, -10, 10, 0.909090909091, 1.010101010101010101);

        args.set(0, x * mod);
        args.set(1, y);
        args.set(2, z * mod);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //-- BUTTERFLY -- //
    @Unique
    private static Holder.Reference<Enchantment> cachedButterfly = null;

    @Unique
    private static Holder.Reference<Enchantment> getButterfly(Level level) {
        if (cachedButterfly == null) {
            cachedButterfly = level.registryAccess()
                    .lookup(ModEnchantments.BUTTERFLY.registryKey())
                    .flatMap(reg -> reg.get(ModEnchantments.BUTTERFLY))
                    .orElse(null);
        }
        return cachedButterfly;
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

        var butterfly = getButterfly(level);
        var stack = attacker.getWeaponItem();
        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(butterfly, stack);

        if (enchantmentLevel == 0) {
            return;
        }

        self.invulnerableTime = 1;
    }

    @ModifyExpressionValue(
            method = "swing(Lnet/minecraft/world/InteractionHand;Z)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/LivingEntity;swinging:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean cancelSwing(boolean original) {
        var self = (LivingEntity)(Object)this;
        var weapon = self.getWeaponItem();
        var isSpear = weapon.is(ItemTags.SPEARS);

        if (isSpear) {
            return false;
        }

        return original;
    }
}
