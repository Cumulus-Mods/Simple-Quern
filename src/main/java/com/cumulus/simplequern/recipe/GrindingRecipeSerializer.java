package com.cumulus.simplequern.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.network.PacketByteBuf;


public class GrindingRecipeSerializer implements RecipeSerializer<GrindingRecipe> {
    private final RecipeFactory recipeFactory;

    public GrindingRecipeSerializer(RecipeFactory recipeFactory) {
        this.recipeFactory = recipeFactory;
    }

    // TODO: Make this a codec!
    @Override
    public GrindingRecipe read(Identifier identifier, JsonObject jsonObject) {
        String group = JsonHelper.getString(jsonObject, "group", "");
        JsonElement object = JsonHelper.hasArray(jsonObject, "ingredient") ? JsonHelper.getArray(jsonObject, "ingredient") : JsonHelper.getObject(jsonObject, "ingredient");
        Ingredient input = Ingredient.fromJson(object);
        int grindLevel = JsonHelper.getInt(jsonObject, "tier");
        int grindTime = JsonHelper.getInt(jsonObject, "time");
        ItemStack output = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
        return this.recipeFactory.create(identifier, group, input, grindLevel, grindTime, output);
    }

    @Override
    public GrindingRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
        String group = packetByteBuf.readString(32767);
        Ingredient input = Ingredient.fromPacket(packetByteBuf);
        int grindLevel =  packetByteBuf.readInt();
        int grindTime = packetByteBuf.readInt();
        ItemStack output = packetByteBuf.readItemStack();
        return this.recipeFactory.create(identifier, group, input, grindLevel, grindTime, output);
    }

    @Override
    public void write(PacketByteBuf packetByteBuf, GrindingRecipe grindingRecipe) {
        packetByteBuf.writeString(grindingRecipe.getGroup());
        grindingRecipe.getInput().write(packetByteBuf);
        packetByteBuf.writeInt(grindingRecipe.getGrindLevel());
        packetByteBuf.writeInt(grindingRecipe.getGrindTime());
        packetByteBuf.writeItemStack(grindingRecipe.getOutput());
    }

    interface RecipeFactory {
        GrindingRecipe create(Identifier id, String group, Ingredient input, int grindLevel, int grindTime, ItemStack output);
    }
}
