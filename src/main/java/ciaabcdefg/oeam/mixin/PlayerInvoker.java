package ciaabcdefg.oeam.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerInvoker {
    @Invoker("doSweepAttack")
    void invokeDoSweepAttack(final Entity entity, final float baseDamage, final DamageSource damageSource, final float attackStrengthScale);

    @Invoker("playServerSideSound")
    void invokePlayServerSideSound(final SoundEvent sound);

    @Invoker("getEnchantedDamage")
    float invokeGetEnchantedDamage(final Entity entity, final float dmg, final DamageSource damageSource);
}
