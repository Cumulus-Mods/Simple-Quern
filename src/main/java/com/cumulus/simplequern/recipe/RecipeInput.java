package com.cumulus.simplequern.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

public class RecipeInput {

    public String type;
    public Identifier id;

    private DefaultedList<Ingredient> list = DefaultedList.of();

    public static final Codec<RecipeInput> CODEC = Codec.STRING.dispatch((recipeInput) -> {
        return recipeInput.type;
    }, (type) -> {
        return RecordCodecBuilder.create((instance) -> {
            return instance.group(Identifier.CODEC.fieldOf("id").forGetter((recipeInput) -> {
                return recipeInput.id;
            })).apply(instance, (id) -> {
                return new RecipeInput(id, type);
            });
        });
    });

    public RecipeInput(Identifier id, String type) {
        this.id = id;
        this.type = type;

        if (this.type.equals("item")) {
            this.list.add(Ingredient.ofItems(Registry.ITEM.get(this.id)));
        } else if (this.type.equals("tag")) {
            Tag<Item> tag = ItemTags.getTagGroup().getTag(this.id);
            if (tag != null) {
                tag.values().forEach((item) -> this.list.add(Ingredient.ofItems(item)));
            }
        }
    }

    public boolean validInput(ItemStack stack) {
        return this.list.stream().anyMatch((ingredient) -> ingredient.test(stack));
    }

    public DefaultedList<Ingredient> previews() {
        return this.list;
    }
}
