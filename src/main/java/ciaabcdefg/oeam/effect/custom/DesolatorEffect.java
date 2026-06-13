package ciaabcdefg.oeam.effect.custom;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class DesolatorEffect extends MobEffect {
    public final Identifier ATTRIBUTE_MODIFIER_ID = Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, "desolator");
    public DesolatorEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
        addAttributeModifier(
                Attributes.ARMOR,
                ATTRIBUTE_MODIFIER_ID,
                -10,
                AttributeModifier.Operation.ADD_VALUE
        );
        addAttributeModifier(
                Attributes.ARMOR_TOUGHNESS,
                ATTRIBUTE_MODIFIER_ID,
                -5,
                AttributeModifier.Operation.ADD_VALUE
        );
    }
}
