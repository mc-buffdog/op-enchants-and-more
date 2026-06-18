package ciaabcdefg.oeam.enchantment.custom;

import ciaabcdefg.oeam.enchantment.tag.ModEnchantmentTags;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.EntityTypePredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class GreaterLootingEnchantment {
    public static Enchantment.Builder build(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        var entityTypes = context.lookup(Registries.ENTITY_TYPE);
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        return Enchantment.enchantment(
                    Enchantment.definition(
                            items.getOrThrow(ItemTags.MELEE_WEAPON_ENCHANTABLE),
                            1,
                            3,
                            Enchantment.dynamicCost(15, 9),
                            Enchantment.dynamicCost(65, 9),
                            4,
                            EquipmentSlotGroup.MAINHAND
                    )
            )
            .exclusiveWith(enchantments.getOrThrow(ModEnchantmentTags.LOOTING_EXCLUSIVE))
            .withEffect(
                    EnchantmentEffectComponents.EQUIPMENT_DROPS,
                    EnchantmentTarget.ATTACKER,
                    EnchantmentTarget.VICTIM,
                    new AddValue(LevelBasedValue.perLevel(0.25F)),
                    LootItemEntityPropertyCondition.hasProperties(
                            LootContext.EntityTarget.ATTACKER, EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityTypes, EntityType.PLAYER))
                    )
            );
    }
}