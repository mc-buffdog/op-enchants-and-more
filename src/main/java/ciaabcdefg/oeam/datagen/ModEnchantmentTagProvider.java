package ciaabcdefg.oeam.datagen;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.tag.ModEnchantmentTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModEnchantmentTagProvider extends FabricTagsProvider<Enchantment> {
    public ModEnchantmentTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        builder(ModEnchantmentTags.EFFICIENCY_EXCLUSIVE)
                .add(ModEnchantments.GREATER_EFFICIENCY)
                .add(Enchantments.EFFICIENCY);

        builder(ModEnchantmentTags.LOOTING_EXCLUSIVE)
                .add(ModEnchantments.GREATER_LOOTING)
                .add(Enchantments.LOOTING);

        builder(ModEnchantmentTags.MENDING_EXCLUSIVE)
                .add(ModEnchantments.GREATER_MENDING)
                .add(Enchantments.MENDING);

        builder(ModEnchantmentTags.UNBREAKING_EXCLUSIVE)
                .add(ModEnchantments.GREATER_UNBREAKING)
                .add(Enchantments.UNBREAKING);

        builder(EnchantmentTags.NON_TREASURE)
                .add(ModEnchantments.COUP_DE_GRACE)
                .add(ModEnchantments.LIFESTEAL)
                .add(ModEnchantments.BUTTERFLY)
                .add(ModEnchantments.GREATER_SHARPNESS)
                .add(ModEnchantments.GREATER_EFFICIENCY)
                .add(ModEnchantments.GREATER_PROTECTION)
                .add(ModEnchantments.GREATER_FORTUNE)
                .add(ModEnchantments.GREATER_LOOTING)
                .add(ModEnchantments.GREATER_UNBREAKING)
                .add(ModEnchantments.SOAR)
                .add(ModEnchantments.CLEAVE)
                .add(ModEnchantments.SMELTING)
                .add(ModEnchantments.HEART_OF_TARASQUE)
                .add(ModEnchantments.GIANT_SLAYER);

        builder(EnchantmentTags.TRADEABLE)
                .add(ModEnchantments.GREATER_MENDING);
    }
}