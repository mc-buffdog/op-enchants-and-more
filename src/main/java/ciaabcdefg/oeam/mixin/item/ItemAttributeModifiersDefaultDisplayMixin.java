package ciaabcdefg.oeam.mixin.item;

import ciaabcdefg.oeam.attribute.ModAttributes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.text.DecimalFormat;

@SuppressWarnings("deprecation")
@Mixin(ItemAttributeModifiers.Display.Default.class)
public class ItemAttributeModifiersDefaultDisplayMixin {
    @Unique
    private static boolean isPercent(Holder<Attribute> attribute) {
        return attribute.is(ModAttributes.LIFESTEAL);
    }

    @Redirect(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/text/DecimalFormat;format(D)Ljava/lang/String;",
                    ordinal = 1
            )
    )
    private String modifyAttributeModifierGive(DecimalFormat instance, double v, @Local(name = "attribute", argsOnly = true) Holder<Attribute> attribute) {
        if (isPercent(attribute)) {
            return instance.format(v * 100) + "%";
        }
        return instance.format(v);
    }

    @Redirect(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/text/DecimalFormat;format(D)Ljava/lang/String;",
                    ordinal = 2
            )
    )
    private String modifyAttributeModifierTake(DecimalFormat instance, double v, @Local(name = "attribute", argsOnly = true) Holder<Attribute> attribute) {
        if (isPercent(attribute)) {
            return instance.format(v * 100) + "%";
        }
        return instance.format(v);
    }
}
