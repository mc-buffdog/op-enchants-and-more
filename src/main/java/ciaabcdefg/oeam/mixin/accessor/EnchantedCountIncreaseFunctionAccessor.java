package ciaabcdefg.oeam.mixin.accessor;

import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnchantedCountIncreaseFunction.class)
public interface EnchantedCountIncreaseFunctionAccessor {
    @Accessor("count")
    NumberProvider getCount();

    @Accessor("limit")
    int getLimit();

    @Invoker("hasLimit")
    boolean invokeHasLimit();
}
