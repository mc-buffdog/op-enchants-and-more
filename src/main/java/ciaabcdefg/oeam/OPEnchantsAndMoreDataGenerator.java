package ciaabcdefg.oeam;

import ciaabcdefg.oeam.datagen.ModEnchantmentTagProvider;
import ciaabcdefg.oeam.datagen.ModRegistryDataGenerator;
import ciaabcdefg.oeam.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class OPEnchantsAndMoreDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModRegistryDataGenerator::new);
		pack.addProvider(ModEnchantmentTagProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.ENCHANTMENT, ModRegistryDataGenerator::bootstrap);
	}
}
