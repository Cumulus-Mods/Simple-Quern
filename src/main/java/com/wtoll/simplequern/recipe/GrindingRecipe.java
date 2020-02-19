package com.wtoll.simplequern.recipe;

import com.wtoll.simplequern.block.Blocks;
import com.wtoll.simplequern.item.Handstone;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Random;

public class GrindingRecipe implements Recipe<Inventory> {
    private final Ingredient input;
    private final ItemStack output;
    private final int grindLevel;
    private final int grindTime;
    private final Identifier id;
    private final String group;

    public GrindingRecipe(Identifier id, String group, Ingredient input, int grindLevel, int grindTime, ItemStack output) {
        this.id = id;
        this.group = group;
        this.input = input;
        this.output = output;
        this.grindLevel = grindLevel;
        this.grindTime = grindTime;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public int getGrindLevel() {
        return this.grindLevel;
    }

    public int getGrindTime() {
        return this.grindTime;
    }

    @Override
    public ItemStack getOutput() {
        return this.output;
    }

    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public net.minecraft.recipe.RecipeSerializer<GrindingRecipe> getSerializer() {
        return RecipeSerializer.GRINDING;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        inv.getInvStack(0).decrement(1);
        inv.getInvStack(1).damage(1, new Random(), null);
        return getOutput().copy();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(Blocks.QUERN);
    }

    @Override
    public net.minecraft.recipe.RecipeType<?> getType() {
        return RecipeType.GRINDING;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack item = inv.getInvStack(0);
        ItemStack tool = inv.getInvStack(1);
        if (tool.getItem() instanceof Handstone && (((Handstone) tool.getItem()).grindLevel() >= this.grindLevel)) {
            return this.input.test(item);
        }
        return false;
    }

    @Override
    public DefaultedList<Ingredient> getPreviewInputs() {
        DefaultedList<Ingredient> inputs = DefaultedList.of();
        inputs.add(this.input);
        return inputs;
    }
}
