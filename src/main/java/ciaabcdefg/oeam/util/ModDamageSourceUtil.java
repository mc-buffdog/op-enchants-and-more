package ciaabcdefg.oeam.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;

public class ModDamageSourceUtil {
    public static ItemStack getWeaponFromSource(DamageSource source) {
        var directEntity = source.getDirectEntity();

        if (directEntity instanceof Projectile projectile) {
            return projectile.getWeaponItem();
        } else if (directEntity instanceof LivingEntity attacker) {
            return attacker.getMainHandItem();
        }

        return null;
    }
}
