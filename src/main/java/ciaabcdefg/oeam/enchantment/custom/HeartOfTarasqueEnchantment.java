package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class HeartOfTarasqueEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                                items.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE),
                                1,
                                5,
                                Enchantment.dynamicCost(2, 12),
                                Enchantment.dynamicCost(21, 12),
                                1,
                                EquipmentSlotGroup.ARMOR
                        )
                )
                .withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "heart_of_tarasque"),
                        Attributes.MAX_HEALTH,
                        LevelBasedValue.perLevel(1F),
                        AttributeModifier.Operation.ADD_VALUE)
                );
    }
}
