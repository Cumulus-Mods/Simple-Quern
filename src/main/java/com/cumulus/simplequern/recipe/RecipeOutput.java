package com.cumulus.simplequern.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.data.server.LootTablesProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.LootTables;
import net.minecraft.recipe.Recipe;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.Map;
import java.util.Optional;

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
