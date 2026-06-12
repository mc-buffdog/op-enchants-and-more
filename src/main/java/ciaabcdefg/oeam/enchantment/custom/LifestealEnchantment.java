package ciaabcdefg.oeam.enchantment.custom;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public final class LifestealEnchantment {
    public static final LevelBasedValue LIFESTEAL_AMOUNT = LevelBasedValue.perLevel(0.1F, 0.15F);

    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        10,
                        5,
                        Enchantment.dynamicCost(1, 11),
                        Enchantment.dynamicCost(21, 11),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        );
    }

    public static float calculateLifesteal(int level) {
        return Math.max(0, LIFESTEAL_AMOUNT.calculate(level));
    }
}
