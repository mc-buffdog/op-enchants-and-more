package ciaabcdefg.oeam;

import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import ciaabcdefg.oeam.world.GreaterFortuneFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
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
		ModAttributes.initialize();

		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (!key.identifier().getPath().startsWith("blocks/")) return;

			var greaterFortune = registries.lookupOrThrow(Registries.ENCHANTMENT)
					.getOrThrow(ModEnchantments.GREATER_FORTUNE);

			tableBuilder.modifyPools(poolBuilder ->
					poolBuilder.apply(GreaterFortuneFunction.create(greaterFortune))
			);
		});
	}
}