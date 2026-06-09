package ciaabcdefg.oeam;

import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.enchantment.effect.ModEnchantmentEffects;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.particle.custom.CoupDeGraceParticle;
import ciaabcdefg.oeam.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OPEnchantsAndMore implements ModInitializer {
	public static final String MOD_ID = "op-enchants-and-more";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModSounds.initialize();
		ModParticles.initialize();
		ModEnchantments.initialize();
//
//		EnchantmentEvents.ALLOW_ENCHANTING.register((Holder<Enchantment> enchantment, ItemStack target, EnchantingContext enchantingContext) -> {
//            LOGGER.info("{} {} {}", enchantment.getRegisteredName(), target.getItemName(), enchantingContext.name());
//			return TriState.DEFAULT;
//        });

//		EnchantmentEvents.MODIFY.register((enchantment, builder, source) -> {
//			LOGGER.info("{}", source.name());
//		});
	}
}