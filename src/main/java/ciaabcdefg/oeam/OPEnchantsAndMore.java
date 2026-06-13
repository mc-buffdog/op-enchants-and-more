package ciaabcdefg.oeam;

import ciaabcdefg.oeam.attribute.ModAttributes;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.sound.ModSounds;
import ciaabcdefg.oeam.world.GreaterFortuneFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

			var smelting = registries.lookupOrThrow(Registries.ENCHANTMENT)
					.getOrThrow(ModEnchantments.SMELTING);

			tableBuilder.modifyPools(poolBuilder ->
					poolBuilder
							.apply(GreaterFortuneFunction.create(greaterFortune))
							.apply(SmeltItemFunction.smelted()
								.when(MatchTool.toolMatches(
										ItemPredicate.Builder.item()
												.withComponents(DataComponentMatchers.Builder.components()
														.partial(DataComponentPredicates.ENCHANTMENTS,
																EnchantmentsPredicate.enchantments(List.of(
																		new EnchantmentPredicate(smelting, MinMaxBounds.Ints.atLeast(1))
																)))
														.build())
							)))
			);
		});
	}
}