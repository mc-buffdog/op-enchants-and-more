package ciaabcdefg.oeam.enchantment.custom;

import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.RemoveBinomial;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

public class GreaterUnbreakingEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        return Enchantment
                .enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                3,
                                3,
                                Enchantment.dynamicCost(15, 15),
                                Enchantment.dynamicCost(25, 15),
                                2,
                                EquipmentSlotGroup.ANY
                        )
                )
                .withEffect(
                        EnchantmentEffectComponents.ITEM_DAMAGE,
                        new RemoveBinomial(new LevelBasedValue.Fraction(LevelBasedValue.perLevel(4.0F), LevelBasedValue.perLevel(10.0F, 5.0F))),
                        MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE))
                )
                .withEffect(
                        EnchantmentEffectComponents.ITEM_DAMAGE,
                        new RemoveBinomial(new LevelBasedValue.Fraction(LevelBasedValue.perLevel(1.0F), LevelBasedValue.perLevel(1.3F, 1.0F))),
                        InvertedLootItemCondition.invert(MatchTool.toolMatches(ItemPredicate.Builder.item().of(items, ItemTags.ARMOR_ENCHANTABLE)))
                );
    }
}