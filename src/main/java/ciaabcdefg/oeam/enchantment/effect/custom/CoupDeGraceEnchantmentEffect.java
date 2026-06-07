package ciaabcdefg.oeam.enchantment.effect.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record CoupDeGraceEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<CoupDeGraceEnchantmentEffect> CODEC = MapCodec.unit(CoupDeGraceEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {}

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
