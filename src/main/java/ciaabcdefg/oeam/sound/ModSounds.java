package ciaabcdefg.oeam.sound;

import ciaabcdefg.oeam.OPEnchantsAndMore;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {
    public static final SoundEvent COUP_DE_GRACE = register("coup_de_grace");

    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(OPEnchantsAndMore.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void initialize() {
        OPEnchantsAndMore.LOGGER.info("Registering sounds");
    }
}