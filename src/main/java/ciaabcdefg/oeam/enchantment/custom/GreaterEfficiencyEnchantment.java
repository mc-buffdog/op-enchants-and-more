package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.tag.ModEnchantmentTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class GreaterEfficiencyEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        return Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                5,
                                5,
                                Enchantment.dynamicCost(5, 15),
                                Enchantment.dynamicCost(60, 15),
                                1,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(ModEnchantmentTags.EFFICIENCY_EXCLUSIVE))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "greater_efficiency"),
                                Attributes.MINING_EFFICIENCY,
                                new LevelBasedValue.LevelsSquared(28.0F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                );
    }
}
