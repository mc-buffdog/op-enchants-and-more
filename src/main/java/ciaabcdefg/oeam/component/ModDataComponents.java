package ciaabcdefg.oeam.component;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.component.custom.DesolatorStacksComponent;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.item.v1.ItemComponentTooltipProviderRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ModDataComponents {
    public static final DataComponentType<DesolatorStacksComponent> DESOLATOR_STACKS_COMPONENT =
            register("desolator_stacks", DesolatorStacksComponent.CODEC);

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Initialized ModDataComponents");
        ItemComponentTooltipProviderRegistry.addAfter(DataComponents.ATTRIBUTE_MODIFIERS, DESOLATOR_STACKS_COMPONENT);
    }

    private static <T> DataComponentType<T> register(String name, @Nullable Codec<T> codec) {
        return Registry.register(
                BuiltInRegistries.DATA_COMPONENT_TYPE,
                Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name),
                DataComponentType.<T>builder().persistent(codec).build()
        );
    }
}
