package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class SimpleQuernCompat {
    public static void onInitialize() {
        ModContainer container = FabricLoader.getInstance().getModContainer(SimpleQuern.MODID).get();
        // SAFETY: getModContainer should always return an optional that is present for this code is contained in the
        // mod that is being asked to be loaded
        ResourceManagerHelper.registerBuiltinResourcePack(
                new Identifier("simplequern", "compat/techreborn"),
                "subpacks/compat/techreborn",
                container,
                false);
        ResourceManagerHelper.registerBuiltinResourcePack(
                new Identifier("simplequern", "compat/ae2"),
                "subpacks/compat/ae2",
                container,
                false);
    }
}
