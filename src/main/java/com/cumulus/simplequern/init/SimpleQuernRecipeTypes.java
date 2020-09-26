package com.cumulus.simplequern.init;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.recipe.GrindingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class SimpleQuernRecipeTypes {
    public static final RecipeType<GrindingRecipe> GRINDING = register("grinding");

    public static <T extends Recipe<?>> RecipeType<T> register(final String string) {
        return Registry.register(Registry.RECIPE_TYPE, SimpleQuern.id(string), new RecipeType<T>() {
            public String toString() {
                return string;
            }
        });
    }

    public static void onInitialize() {  }
}
