package ciaabcdefg.oeam.effect.custom;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.util.ModEnchantmentUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public class VitalityEffect extends MobEffect {
    public VitalityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(final int tickCount, final int amplification) {
        int interval = 10 >> amplification;
        return tickCount > 0 && tickCount % interval == 0;
    }

    @Override
    public boolean applyEffectTick(final ServerLevel level, final LivingEntity mob, final int amplification) {
        if (mob.getHealth() < mob.getMaxHealth()) {
            var healing = 0.2F * getAverageEnchantmentLevel(mob);
            mob.heal(healing);
        }
        return true;
    }

    private float getAverageEnchantmentLevel(LivingEntity entity) {
        var head = entity.getItemBySlot(EquipmentSlot.HEAD);
        var chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        var legs = entity.getItemBySlot(EquipmentSlot.LEGS);
        var feet = entity.getItemBySlot(EquipmentSlot.FEET);
        var sumLevels = getEnchantmentLevel(head) + getEnchantmentLevel(chest) + getEnchantmentLevel(legs) + getEnchantmentLevel(feet);
        return sumLevels / 4F;
    }

    private int getEnchantmentLevel(ItemStack item) {
        return ModEnchantmentUtil.getEnchantmentLevel(item, ModEnchantments.HEART_OF_TARASQUE);
    }
}
