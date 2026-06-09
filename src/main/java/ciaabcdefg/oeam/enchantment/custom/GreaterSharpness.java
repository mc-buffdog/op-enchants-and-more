package ciaabcdefg.oeam.enchantment.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;

public class GreaterSharpness {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        return  Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.SHARP_WEAPON_ENCHANTABLE),
                                items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
                                5,
                                5,
                                Enchantment.dynamicCost(5, 15),
                                Enchantment.dynamicCost(25, 15),
                                1,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
                .exclusiveWith(enchantments.getOrThrow(EnchantmentTags.DAMAGE_EXCLUSIVE))
                .withEffect(EnchantmentEffectComponents.DAMAGE,
                        new AddValue(LevelBasedValue.perLevel(5.0F, 1.8F)));
    }
}
