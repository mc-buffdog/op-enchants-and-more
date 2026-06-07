package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(Player.class)
public class CoupDeGraceMixin {
    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0   // targets `totalDamage` — the first float local before hurtOrSimulate
    )
    private float applyExtraCritMultiplier(float totalDamage, Entity target) {
        Player self = (Player) (Object) this;
        var world = self.level();

        if (world.isClientSide()) return totalDamage;

        var stack = self.getWeaponItem();
        var coupDeGrace = world.registryAccess()
                .lookup(ModEnchantments.COUP_DE_GRACE.registryKey()) // use your registry key
                .flatMap(reg -> reg.get(ModEnchantments.COUP_DE_GRACE))
                .orElse(null);

        if (coupDeGrace == null) return totalDamage;

        int level = EnchantmentHelper.getItemEnchantmentLevel(coupDeGrace, stack);
        if (level == 0) return totalDamage;

        float damageMult = 1.0F;
        float chance = 0.0F;

        if (level == 1) {
            damageMult = 2.0F;
            chance = 0.34F;
        } else if (level == 2) {
            damageMult = 3.25F;
            chance = 0.38F;
        } else {
            damageMult = 4.5F;
            chance = 0.41F;
        }

        OPEnchantsAndMore.LOGGER.info("Has Coup De Grace");

        boolean isCritical = self.getRandom().nextDouble() < chance;
        if (isCritical) {
            OPEnchantsAndMore.LOGGER.info("Coup De Grace!!: {} -> {}", totalDamage, totalDamage * damageMult);
            world.playSound(null, target.blockPosition(), ModSounds.COUP_DE_GRACE, SoundSource.PLAYERS, 1f, 1f);

//            var direction = target.position().subtract(self.position()).normalize();

            ((ServerLevel) world).sendParticles(ModParticles.COUP_DE_GRACE_PARTICLE,
                    target.position().x(),
                    target.position().y(),
                    target.position().z(),
                    3, 0, 0, 0, 1
            );

            return totalDamage * damageMult;
        }

        return totalDamage;
    }
}