package com.cumulus.simplequern.mixin;

import com.cumulus.simplequern.recipe.GrindingRecipe;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    @Inject(method = "getGroupForRecipe", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;[Lorg/apache/logging/log4j/util/Supplier;)V"), cancellable = true)
    private static void getGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> callback) {
        if (recipe instanceof GrindingRecipe) callback.setReturnValue(RecipeBookGroup.UNKNOWN);
    }
}
