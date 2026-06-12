package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.item.tag.ModItemTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.LevelBasedValue;

import java.util.Locale;
import java.util.function.Consumer;

public class CleaveEnchantment {
    public static LevelBasedValue CLEAVE_RANGE = LevelBasedValue.perLevel(3F, 1.4F);
    public static LevelBasedValue CLEAVE_BONUS = LevelBasedValue.perLevel(0.010F, 0.03F);
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
        );
    }

    public static float calculateCleaveRange(int level) {
        return Math.max(CLEAVE_RANGE.calculate(level), 0.0F);
    }

    public static float calculateCleaveBonus(int level) {
        return Math.max(CLEAVE_BONUS.calculate(level), 0.0F);
    }

    public static float calculateCleaveAngle(int level) {
        return Math.max(CLEAVE_ANGLE.calculate(level), 0.0F);
    }

    public static void displayTooltip(Consumer<Component> builder, int level) {
        var range = calculateCleaveRange(level);
        var bonus = calculateCleaveBonus(level);
        var angle = calculateCleaveAngle(level);

        var formattedRange = String.format(Locale.ROOT, "%+.1f", range);
        var formattedBonus = String.format(Locale.ROOT, "%+.1f", bonus);
        var formattedAngle = String.format(Locale.ROOT, "%+.1f°", angle);

        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.cleave_range",
                        formattedRange
                ).withStyle(ChatFormatting.BLUE)
        );

        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.cleave_bonus",
                        formattedBonus
                ).withStyle(ChatFormatting.BLUE)
        );

        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.cleave_angle",
                        formattedAngle
                ).withStyle(ChatFormatting.BLUE)
        );
    }
}
