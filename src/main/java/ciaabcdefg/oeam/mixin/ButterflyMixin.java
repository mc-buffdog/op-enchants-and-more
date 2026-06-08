package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class ButterflyMixin {
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
    private boolean aaaa(boolean original) {
        return false;
    }
}
