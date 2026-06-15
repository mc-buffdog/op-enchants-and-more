package ciaabcdefg.oeam.effect.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class VitalityEffect extends MobEffect {
    public VitalityEffect() {
        super(MobEffectCategory.BENEFICIAL, 0x00FFFF);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(final int tickCount, final int amplification) {
        int interval = 10 >> amplification;
        return tickCount > 0 && tickCount % interval == 0;
    }

    @Override
    public boolean applyEffectTick(final ServerLevel level, final LivingEntity mob, final int amplification) {
        if (mob.getHealth() < mob.getMaxHealth()) {
            mob.heal(0.5F);
        }
        return true;
    }
}
