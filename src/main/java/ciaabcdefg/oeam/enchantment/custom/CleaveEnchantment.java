package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.attribute.ModAttributes;
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

public class CleaveEnchantment {
    public static LevelBasedValue CLEAVE_ANGLE = LevelBasedValue.perLevel(45F, 5F);

    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ModItemTags.CLEAVE_WEAPON_ENCHANTABLE),
                        10,
                        4,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(21, 11),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        ).withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "sweep_area"),
                        ModAttributes.SWEEPING_AREA,
                        LevelBasedValue.perLevel(3F, 1.4F),
                        AttributeModifier.Operation.ADD_VALUE
                )
        ).withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "sweep_damage_ratio"),
                        Attributes.SWEEPING_DAMAGE_RATIO,
                        LevelBasedValue.perLevel(0.010F, 0.03F),
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}
