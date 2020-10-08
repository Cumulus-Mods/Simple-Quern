package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.recipe.GrindingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SimpleQuernRecipeSerializers {
    public static final RecipeSerializer<GrindingRecipe> GRINDING = register(SimpleQuern.id("grinding"), new GrindingRecipe.Serializer());

    static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(Identifier id, S serializer) {
        return Registry.register(Registry.RECIPE_SERIALIZER, id, serializer);
    }

    public static void onInitialize() {  }
}
