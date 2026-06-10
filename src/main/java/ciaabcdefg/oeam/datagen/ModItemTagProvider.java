package ciaabcdefg.oeam.datagen;

import ciaabcdefg.oeam.item.tag.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public ModItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider wrapperLookup) {
        valueLookupBuilder(ModItemTags.CLEAVE_WEAPON_ENCHANTABLE)
                .addOptionalTag(ItemTags.SWORDS)
                .addOptionalTag(ItemTags.AXES);

        valueLookupBuilder(ModItemTags.FLYING_ENCHANTABLE)
                .add(Items.ELYTRA);
    }
}