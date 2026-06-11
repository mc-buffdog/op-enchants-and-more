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
        builder(EnchantmentTags.NON_TREASURE)
                .add(ModEnchantments.COUP_DE_GRACE)
                .add(ModEnchantments.LIFESTEAL)
                .add(ModEnchantments.BUTTERFLY)
                .add(ModEnchantments.GREATER_SHARPNESS)
                .add(ModEnchantments.GREATER_EFFICIENCY)
                .add(ModEnchantments.SOAR)
                .add(ModEnchantments.CLEAVE)
                .add(ModEnchantments.GIANT_SLAYER);

        builder(ModEnchantmentTags.EFFICIENCY_EXCLUSIVE)
                .add(ModEnchantments.GREATER_EFFICIENCY)
                .add(Enchantments.EFFICIENCY);
    }
}