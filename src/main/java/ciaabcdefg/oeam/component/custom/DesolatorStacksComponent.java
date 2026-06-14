package ciaabcdefg.oeam.component.custom;

import ciaabcdefg.oeam.component.ModDataComponents;
import ciaabcdefg.oeam.enchantment.custom.DesolatorEnchantment;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

import java.util.Locale;
import java.util.function.Consumer;

public record DesolatorStacksComponent(int stacks) implements TooltipProvider {
    public static final Codec<DesolatorStacksComponent> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.INT.fieldOf("stacks").forGetter(DesolatorStacksComponent::stacks)
            ).apply(builder, DesolatorStacksComponent::new)
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        var desolatorStacks = components.get(ModDataComponents.DESOLATOR_STACKS_COMPONENT);
        float desolatorDamage = 0;

        if (desolatorStacks != null) {
            desolatorDamage = DesolatorEnchantment.calculateDesolatorDamage(desolatorStacks.stacks());
        }

        var formattedStacks = String.format(Locale.ROOT, "%+.1f", desolatorDamage);
        consumer.accept(
                Component
                        .translatable("enchantment.effect.tooltip.op-enchants-and-more.desolator", formattedStacks)
                        .withStyle(ChatFormatting.RED)
        );
    }
}
