package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.effect.custom.HeartOfTarasqueEnchantmentEffect;
import ciaabcdefg.oeam.item.tag.ModItemTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class HeartOfTarasqueEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ModItemTags.HEART_OF_TARASQUE_ENCHANTABLE),
                                3,
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
                ).withEffect(
                        EnchantmentEffectComponents.TICK, new HeartOfTarasqueEnchantmentEffect()
                );
    }
}
