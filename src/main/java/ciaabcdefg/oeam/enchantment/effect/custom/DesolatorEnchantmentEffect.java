package ciaabcdefg.oeam.enchantment.effect.custom;

import ciaabcdefg.oeam.effect.ModEffects;
import ciaabcdefg.oeam.sound.ModSounds;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record DesolatorEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<DesolatorEnchantmentEffect> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    LevelBasedValue.CODEC.fieldOf("amount").forGetter(DesolatorEnchantmentEffect::amount)
            ).apply(instance, DesolatorEnchantmentEffect::new)
    );


    @Override
    public void apply(
        @NonNull ServerLevel serverLevel,
        int enchantmentLevel,
        @NonNull EnchantedItemInUse item,
        @NonNull Entity target,
        @NonNull Vec3 position
    ) {
        if (!(target instanceof LivingEntity victim)) return;
        var desolatorInstance = new MobEffectInstance(ModEffects.DESOLATOR, 2 * 20, 0, false, true, true);
        victim.addEffect(desolatorInstance);

        if (item.owner() instanceof LivingEntity attacker) {
            serverLevel.playSound(null, attacker.blockPosition(), ModSounds.DESOLATOR, SoundSource.PLAYERS, 0.8f, 1f);
        }
    }

    @Override
    public @NonNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
