package ciaabcdefg.oeam.enchantment.effect.custom;

import ciaabcdefg.oeam.effect.ModEffects;
import ciaabcdefg.oeam.enchantment.custom.DesolatorEnchantment;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public record DesolatorEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<DesolatorEnchantmentEffect> CODEC = MapCodec.unit(new DesolatorEnchantmentEffect());

    @Override
    public void apply(
        @NonNull ServerLevel serverLevel,
        int enchantmentLevel,
        @NonNull EnchantedItemInUse item,
        @NonNull Entity target,
        @NonNull Vec3 position
    ) {
        if (!(target instanceof LivingEntity victim)) return;
        var desolatorInstance = new MobEffectInstance(ModEffects.DESOLATOR, Math.round(DesolatorEnchantment.DESOLATOR_DURATION * 20), 0, false, true, true);
        victim.addEffect(desolatorInstance);
    }

    @Override
    public @NonNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
