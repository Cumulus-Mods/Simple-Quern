package com.wtoll.simplequern.recipe;

import com.wtoll.simplequern.Utility;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.registry.Registry;

public class RecipeType {
    public static final net.minecraft.recipe.RecipeType<GrindingRecipe> GRINDING;

    static {
        GRINDING = register("grinding");
    }

    public static <T extends Recipe<?>> net.minecraft.recipe.RecipeType<T> register(final String string) {
        return Registry.register(Registry.RECIPE_TYPE, Utility.id(string), new net.minecraft.recipe.RecipeType<T>() {
            public String toString() {
                return string;
            }
        });
    }

    public static void initialize() {  }
}
