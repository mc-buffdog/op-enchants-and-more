package ciaabcdefg.oeam;

import ciaabcdefg.oeam.particle.ModParticles;
import ciaabcdefg.oeam.particle.custom.CoupDeGraceParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class OPEnchantsAndMoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(ModParticles.COUP_DE_GRACE_PARTICLE, CoupDeGraceParticle.Provider::new);

//        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) -> {
//            int i = 0;
//            for (var line : lines) {
//                var contents = line.getContents();
//                OPEnchantsAndMore.LOGGER.info("[{}] {}", i, );
//                i++;
//            }
//        });
    }
}
