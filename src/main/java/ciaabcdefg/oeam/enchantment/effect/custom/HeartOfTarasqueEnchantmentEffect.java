package ciaabcdefg.oeam.enchantment.effect.custom;

import ciaabcdefg.oeam.effect.ModEffects;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.util.ModEnchantmentUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class HeartOfTarasqueEnchantmentEffect implements EnchantmentEntityEffect {
    public static final MapCodec<HeartOfTarasqueEnchantmentEffect> CODEC = MapCodec.unit(new HeartOfTarasqueEnchantmentEffect());

    @Override
    public void apply(@NonNull ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
        if (entity instanceof LivingEntity target) {
            if (allArmorSlotsHaveEnchantment(target)) {
                var effect = target.getEffect(ModEffects.VITALITY);
                if (effect == null) {
                    target.addEffect(new MobEffectInstance(
                            ModEffects.VITALITY,
                            100,
                            0,
                            false,
                            false,
                            false)
                    );
                }
            }
        }
    }

    private boolean hasEnchantment(ItemStack item) {
        return ModEnchantmentUtil.getEnchantmentLevel(item, ModEnchantments.HEART_OF_TARASQUE) > 0;
    }

    private boolean allArmorSlotsHaveEnchantment(LivingEntity entity) {
        var head = entity.getItemBySlot(EquipmentSlot.HEAD);
        var chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        var legs = entity.getItemBySlot(EquipmentSlot.LEGS);
        var feet = entity.getItemBySlot(EquipmentSlot.FEET);
        return hasEnchantment(head) && hasEnchantment(chest) && hasEnchantment(legs) && hasEnchantment(feet);
    }

    @Override
    public @NonNull MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
