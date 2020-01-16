package com.wtoll.simplequern.container;

import com.wtoll.simplequern.Utility;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.util.Identifier;

//TODO: Finalize
public class Containers {

    public static Identifier QUERN;

    public static void initialize() {
        ContainerProviderRegistry.INSTANCE.registerFactory(QUERN, (syncId, id, player, buf) -> {
            return new QuernContainer(syncId, player.inventory, buf.readBlockPos());
        });
    }

    static {
        QUERN = Utility.id("quern");
    }
}
