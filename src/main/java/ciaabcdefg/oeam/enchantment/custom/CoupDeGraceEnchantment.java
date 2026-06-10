package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.attribute.ModAttributes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

public final class CoupDeGraceEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORDS),
                        10,
                        3,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(21, 11),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        ).withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "crit_chance"),
                        ModAttributes.CRIT_CHANCE,
                        LevelBasedValue.perLevel(0.34F, 0.035F),
                        AttributeModifier.Operation.ADD_VALUE
                )
        ).withEffect(
                EnchantmentEffectComponents.ATTRIBUTES,
                new EnchantmentAttributeEffect(
                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "crit_damage_mul"),
                        ModAttributes.CRIT_DAMAGE_MUL,
                        LevelBasedValue.perLevel(2.0F, 1.25F),
                        AttributeModifier.Operation.ADD_VALUE
                )
        );
    }
}
