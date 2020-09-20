package com.cumulus.simplequern.recipe;

import com.cumulus.simplequern.SimpleQuern;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipeSerializer {
    public static final net.minecraft.recipe.RecipeSerializer<GrindingRecipe> GRINDING;

    static {
        GRINDING = register(SimpleQuern.id("grinding"), new GrindingRecipeSerializer(GrindingRecipe::new));
    }

    static <S extends net.minecraft.recipe.RecipeSerializer<T>, T extends Recipe<?>> S register(Identifier id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void initialize() {  }
}
