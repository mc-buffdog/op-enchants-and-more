package ciaabcdefg.oeam.block.tag;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static TagKey<Block> AFFECTED_BY_GREATER_FORTUNE = create("affected_by_greater_fortune");

    private static TagKey<Block> create(final String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name));
    }
}
