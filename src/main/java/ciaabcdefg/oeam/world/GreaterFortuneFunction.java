package ciaabcdefg.oeam.world;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import ciaabcdefg.oeam.block.tag.ModBlockTags;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class GreaterFortuneFunction extends LootItemConditionalFunction {
    public static final MapCodec<GreaterFortuneFunction> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> commonFields(i)
                    .and(Enchantment.CODEC.fieldOf("enchantment").forGetter(f -> f.enchantment))
                    .apply(i, GreaterFortuneFunction::new)
    );

    private final Holder<Enchantment> enchantment;

    public GreaterFortuneFunction(List<LootItemCondition> predicates, Holder<Enchantment> enchantment) {
        super(predicates);
        this.enchantment = enchantment;
    }

    @Override
    public @NonNull MapCodec<? extends LootItemConditionalFunction> codec() {
        return MAP_CODEC;
    }

    @Override
    protected @NonNull ItemStack run(@NonNull ItemStack stack, LootContext context) {
        ItemInstance tool = context.getOptionalParameter(LootContextParams.TOOL);
        if (tool == null) return stack;

        int level = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool);
        if (level <= 0) return stack;

        var blockState = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
        if (blockState == null || !blockState.is(ModBlockTags.AFFECTED_BY_GREATER_FORTUNE)) return stack;

        stack.setCount(calculateNewCount(context.getRandom(), stack.getCount(), level));
        return stack;
    }

    private int calculateNewCount(RandomSource random, int count, int level) {
        if (level <= 0) return count;
        int bonus = random.nextInt(2,level + 4) - 1;
        return count * (Math.max(bonus, 0) + 1);
    }

    public static LootItemConditionalFunction.Builder<?> create(Holder<Enchantment> enchantment) {
        return simpleBuilder(conditions -> new GreaterFortuneFunction(conditions, enchantment));
    }
}
