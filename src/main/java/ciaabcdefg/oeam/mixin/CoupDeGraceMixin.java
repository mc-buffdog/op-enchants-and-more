package ciaabcdefg.oeam.mixin;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public class CoupDeGraceMixin {
    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurtOrSimulate(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    shift = At.Shift.BEFORE
            ),
            name = "totalDamage")
    private float applyExtraCritMultiplier(float totalDamage, Entity entity) {
        Player self = (Player) (Object) this;

        var world = self.level();
        if (world.isClientSide()) return totalDamage;

        var stack = self.getWeaponItem();
        var coupDeGrace = world.registryAccess()
                .lookup(ModEnchantments.COUP_DE_GRACE.registryKey())
                .flatMap(reg -> reg.get(ModEnchantments.COUP_DE_GRACE))
                .orElse(null);

        if (coupDeGrace == null) return totalDamage;

        int level = EnchantmentHelper.getItemEnchantmentLevel(coupDeGrace, stack);
        if (level == 0) return totalDamage;

        float damageMult;
        float chance;

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

        if (self.getRandom().nextDouble() >= chance) return totalDamage;

        OPEnchantsAndMore.LOGGER.info("Coup De Grace!!: {} -> {}", totalDamage, totalDamage * damageMult);

        world.playSound(null, entity.blockPosition(), ModSounds.COUP_DE_GRACE, SoundSource.PLAYERS, 1f, 1f);
        ((ServerLevel) world).sendParticles(ModParticles.COUP_DE_GRACE_PARTICLE,
                entity.position().x(),
                entity.position().y(),
                entity.position().z(),
                3, 0, 0, 0, 1
        );

        return totalDamage * damageMult;
    }
}