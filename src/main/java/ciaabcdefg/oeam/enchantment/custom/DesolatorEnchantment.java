package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.enchantment.effect.custom.DesolatorEnchantmentEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public class DesolatorEnchantment {
    public static final float DESOLATOR_DURATION = 6.0F;
    public static final float DESOLATOR_ARMOR_REDUCTION = 12.0F;
    public static final float DESOLATOR_ARMOR_TOUGHNESS_REDUCTION = 5.0F;
    public static final float DESOLATOR_MAGIC_ARMOR_REDUCTION_RATIO = 0.25F;

    public static final int MAX_STACKS = 30;
    public static final float DESOLATOR_MAX_DAMAGE = 5.0F;
    public static final LevelBasedValue DESOLATOR_DAMAGE_PER_STACK = LevelBasedValue.perLevel(DESOLATOR_MAX_DAMAGE / MAX_STACKS);

    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORDS),
                        3,
                        1,
                        Enchantment.dynamicCost(15, 10),
                        Enchantment.dynamicCost(25, 15),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        ).withEffect(
                EnchantmentEffectComponents.POST_ATTACK,
                EnchantmentTarget.ATTACKER,
                EnchantmentTarget.VICTIM,
                new DesolatorEnchantmentEffect()
        );
    }

    public static float calculateDesolatorDamage(int stacks) {
        return Math.max(DESOLATOR_DAMAGE_PER_STACK.calculate(Math.min(stacks, MAX_STACKS)), 0);
    }
}
