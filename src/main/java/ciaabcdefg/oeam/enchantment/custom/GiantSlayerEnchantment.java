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

public final class GiantSlayerEnchantment {
    public static LevelBasedValue DAMAGE_PERCENT_MAX_HP = LevelBasedValue.perLevel(0.0125F);

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
        );
    }

    public static float calculateDamagePercentMaxHP(int level) {
        return Math.max(DAMAGE_PERCENT_MAX_HP.calculate(level), 0.0F);
    }

    public static void displayTooltip(Consumer<Component> builder, int level) {
        var damagePercentMaxHP = calculateDamagePercentMaxHP(level);
        var formattedDamagePercentMaxHP = String.format(Locale.ROOT, "%+.1f%%", damagePercentMaxHP * 100);

        builder.accept(
                Component.translatable(
                        "enchantment.effect.tooltip.op-enchants-and-more.damage_percent_max_hp",
                        formattedDamagePercentMaxHP
                ).withStyle(ChatFormatting.BLUE)
        );
    }
}
