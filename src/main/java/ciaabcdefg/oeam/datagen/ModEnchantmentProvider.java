package ciaabcdefg.oeam.datagen;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.custom.*;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
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
                GreaterSharpnessEnchantment.build(context)
        );

        // Greater Efficiency
        register(
                context,
                ModEnchantments.GREATER_EFFICIENCY,
                GreaterEfficiencyEnchantment.build(context)
        );

        // Greater Protection
        register(
                context,
                ModEnchantments.GREATER_PROTECTION,
                GreaterProtectionEnchantment.build(context)
        );

        // Greater Fortune
        register(
                context,
                ModEnchantments.GREATER_FORTUNE,
                GreaterFortuneEnchantment.build(context)
        );

        // Soar
        register(
                context,
                ModEnchantments.SOAR,
                SoarEnchantment.build(context)
        );

        // Giant Slayer
        register(
                context,
                ModEnchantments.GIANT_SLAYER,
                GiantSlayerEnchantment.build(context)
        );

        // Desolator
        register(
                context,
                ModEnchantments.DESOLATOR,
                DesolatorEnchantment.build(context)
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.identifier()));
    }
}
