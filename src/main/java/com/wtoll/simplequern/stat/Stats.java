package com.wtoll.simplequern.stat;

import com.wtoll.simplequern.Utility;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Stats {
    public static final Identifier INTERACT_WITH_QUERN;

    static {
        INTERACT_WITH_QUERN =  register("interact_with_quern", StatFormatter.DEFAULT);
    }

    private static Identifier register(String string, StatFormatter statFormatter) {
        Identifier identifier = Utility.id(string);
        Registry.register(Registry.CUSTOM_STAT, string, identifier);
        net.minecraft.stat.Stats.CUSTOM.getOrCreateStat(identifier, statFormatter);
        return identifier;
    }

    public static void initialize() {  }
}
