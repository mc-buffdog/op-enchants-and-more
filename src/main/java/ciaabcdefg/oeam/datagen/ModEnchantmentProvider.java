package ciaabcdefg.oeam.datagen;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.*;
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
                CoupDeGraceEnchantment.build(context)
        );

        // Lifesteal
        register(
                context,
                ModEnchantments.LIFESTEAL,
                LifestealEnchantment.build(context)
        );

        // Butterfly
        register(
                context,
                ModEnchantments.BUTTERFLY,
                ButterflyEnchantment.build(context)
        );

        // Cleave
        register(
                context,
                ModEnchantments.CLEAVE,
                CleaveEnchantment.build(context)
        );

        // Greater Sharpness
        register(
                context,
                ModEnchantments.GREATER_SHARPNESS,
                GreaterSharpness.build(context)
        );

        // Greater Efficiency
        register(
                context,
                ModEnchantments.GREATER_EFFICIENCY,
                GreaterEfficiency.build(context)
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.identifier()));
    }
}
