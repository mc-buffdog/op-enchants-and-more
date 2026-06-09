package ciaabcdefg.oeam.datagen;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.tags.ModEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentProvider extends FabricDynamicRegistryProvider {
    public ModEnchantmentProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.addAll(registries.lookupOrThrow(Registries.ENCHANTMENT));
    }

    @Override
    public @NonNull String getName() {
        return "ModEnchantmentProvider";
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var items = context.lookup(Registries.ITEM);
        var enchantments = context.lookup(Registries.ENCHANTMENT);

        // Coup de Grace
        register(
                context,
                ModEnchantments.COUP_DE_GRACE,
                Enchantment.enchantment(
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
                )
        );

        // Lifesteal
        register(
                context,
                ModEnchantments.LIFESTEAL,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                10,
                                5,
                                Enchantment.dynamicCost(1, 11),
                                Enchantment.dynamicCost(21, 11),
                                1,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        // Butterfly
        register(
                context,
                ModEnchantments.BUTTERFLY,
                Enchantment.enchantment(
                        Enchantment.definition(
                                items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                                10,
                                5,
                                Enchantment.dynamicCost(1, 11),
                                Enchantment.dynamicCost(21, 11),
                                1,
                                EquipmentSlotGroup.MAINHAND
                        )
                ).withEffect(EnchantmentEffectComponents.ATTRIBUTES, new EnchantmentAttributeEffect(
                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "butterfly"),
                        Attributes.ATTACK_SPEED,
                        LevelBasedValue.perLevel(0.2F, 0.3F),
                        AttributeModifier.Operation.ADD_VALUE)
                )
        );

        // Greater Sharpness
        register(
                context,
                ModEnchantments.GREATER_SHARPNESS,
                Enchantment.enchantment(
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
                                new AddValue(LevelBasedValue.perLevel(5.0F, 1.8F)))
        );

        // Greater Efficiency
        register(
                context,
                ModEnchantments.GREATER_EFFICIENCY,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        items.getOrThrow(ItemTags.MINING_ENCHANTABLE),
                                        5,
                                        5,
                                        Enchantment.dynamicCost(5, 15),
                                        Enchantment.dynamicCost(60, 15),
                                        1,
                                        EquipmentSlotGroup.MAINHAND
                                )
                        )
                        .exclusiveWith(enchantments.getOrThrow(ModEnchantmentTags.EFFICIENCY_EXCLUSIVE))
                        .withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "greater_efficiency"),
                                        Attributes.MINING_EFFICIENCY,
                                        new LevelBasedValue.LevelsSquared(28.0F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.identifier()));
    }
}
