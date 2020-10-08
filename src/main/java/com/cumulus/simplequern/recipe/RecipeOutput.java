package com.cumulus.simplequern.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipeOutput {

    public String type;
    public Identifier id;
    public int count;

    private ItemStack stack;

    public static final Codec<RecipeOutput> CODEC = Codec.STRING.dispatch((recipeOutput) -> {
        return "type";
    }, (type) -> {
        if (type.equals("loot_table")) {
            return RecordCodecBuilder.create((instance) -> {
                return instance.group(Identifier.CODEC.fieldOf("id").forGetter((recipeOutput) -> {
                    return recipeOutput.id;
                })).apply(instance, (id) -> {
                    return new RecipeOutput(type, id);
                });
            });
        } else {
            return RecordCodecBuilder.create((instance) -> {
                return instance.group(Identifier.CODEC.fieldOf("id").forGetter((recipeOutput) -> {
                    return recipeOutput.id;
                }), Codec.INT.fieldOf("count").forGetter((recipeOutput) -> {
                    return recipeOutput.count;
                })).apply(instance, (id, count) -> {
                    return new RecipeOutput(type, id, count);
                });
            });
        }
    });

    public RecipeOutput(String type, Identifier id, int count) {
        this.type = type;
        this.id = id;
        this.count = count;

        this.stack = new ItemStack(Registry.ITEM.get(this.id), this.count);
    }

    public RecipeOutput(String type, Identifier id) {
        this.type = type;
        this.id = id;
    }

    public ItemStack getStackIfItem() {
        return this.stack;
    }
}
