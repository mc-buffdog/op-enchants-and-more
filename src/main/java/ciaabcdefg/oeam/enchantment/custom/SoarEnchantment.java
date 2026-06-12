package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.item.tag.ModItemTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public class SoarEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ModItemTags.FLYING_ENCHANTABLE),
                                3,
                                4,
                                Enchantment.dynamicCost(10, 15),
                                Enchantment.dynamicCost(25, 20),
                                1,
                                EquipmentSlotGroup.CHEST
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "soar"),
                                ModAttributes.ELYTRA_SPEED_BONUS,
                                LevelBasedValue.perLevel(2, 2.5F),
                                AttributeModifier.Operation.ADD_VALUE
                        )
                );
    }
}
