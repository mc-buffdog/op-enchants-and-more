package ciaabcdefg.oeam.enchantment.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;

import java.util.Locale;
import java.util.function.Consumer;

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

    public static void displayTooltip(Consumer<Component> builder, int level) {
        var lifestealAmount = calculateLifesteal(level);
        var formatted = String.format(Locale.ROOT, "%+.1f%%", lifestealAmount * 100);
        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.lifesteal",
                        formatted
                ).withStyle(ChatFormatting.BLUE)
        );
    }
}
