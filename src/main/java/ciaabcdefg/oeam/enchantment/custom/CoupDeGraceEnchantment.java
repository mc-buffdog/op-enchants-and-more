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

public final class CoupDeGraceEnchantment {
    public static final LevelBasedValue CRIT_CHANCE = LevelBasedValue.perLevel(0.34F, 0.035F);
    public static final LevelBasedValue CRIT_DAMAGE_MUL = LevelBasedValue.perLevel(1.5F, 0.85F);

    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment.enchantment(
                Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ItemTags.SWORDS),
                        7,
                        3,
                        Enchantment.dynamicCost(5, 25),
                        Enchantment.dynamicCost(25, 15),
                        1,
                        EquipmentSlotGroup.MAINHAND
                )
        );
    }

    public static float calculateCritChance(int level) {
        return Math.max(CRIT_CHANCE.calculate(level), 0F);
    }

    public static float calculateCritDamageMul(int level) {
        return Math.max(CRIT_DAMAGE_MUL.calculate(level), 1.0F);
    }

    public static void displayTooltip(Consumer<Component> builder, int level) {
        var critChance = calculateCritChance(level);
        var critDamageMul = calculateCritDamageMul(level);
        var formattedCritChance = String.format(Locale.ROOT, "%+.1f%%", critChance * 100);
        var formattedCritDamageMul = String.format(Locale.ROOT, "%+.1f", critDamageMul);

        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.crit_chance",
                        formattedCritChance
                ).withStyle(ChatFormatting.BLUE)
        );

        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.crit_damage_mul",
                        formattedCritDamageMul
                ).withStyle(ChatFormatting.BLUE)
        );
    }
}
