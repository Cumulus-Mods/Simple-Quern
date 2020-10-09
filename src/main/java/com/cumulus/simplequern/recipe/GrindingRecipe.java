package com.cumulus.simplequern.recipe;

import com.cumulus.simplequern.init.SimpleQuernBlocks;
import com.cumulus.simplequern.init.SimpleQuernRecipeTypes;
import com.cumulus.simplequern.init.SimpleQuernRecipeSerializers;
import com.cumulus.simplequern.item.Handstone;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.*;

public class GrindingRecipe implements Recipe<Inventory> {

    private static final GrindingRecipe EMPTY = new GrindingRecipe(new Identifier(""), 0, 0, new RecipeOutput("item", new Identifier(""), 0), new RecipeInput(new Identifier(""), "item"));

    private final RecipeInput input;
    private final RecipeOutput output;
    private final int grindTier;
    private final int grindTime;
    private final Identifier id;

    public GrindingRecipe(Identifier id, int grindTier, int grindTime, RecipeOutput output, RecipeInput input) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.grindTier = grindTier;
        this.grindTime = grindTime;
    }

    @Override
    public String getGroup() {
        return "";
    }

    @Override
    public Identifier getId() {
        return null;
    }

    public boolean validInput(ItemStack stack) {
        return this.input.validInput(stack);
    }

    public int getGrindTier() {
        return this.grindTier;
    }

    public int getGrindTime() {
        return this.grindTime;
    }

    public List<ItemStack> getOutput(ItemStack tool, ServerWorld world) {
        if (this.output.type.equals("item")) {
            return Collections.singletonList(this.output.getStackIfItem().copy());
        } else if (this.output.type.equals("loot_table")) {
            return world.getServer().getLootManager().getTable(this.output.id).generateLoot(
                new LootContext.Builder(world)
                    .parameter(LootContextParameters.TOOL, tool)
                    .build(new LootContextType.Builder().allow(LootContextParameters.TOOL).build()));
        } else {
            return Collections.singletonList(ItemStack.EMPTY);
        }
    }

    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    }

    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<GrindingRecipe> getSerializer() {
        return SimpleQuernRecipeSerializers.GRINDING;
    }

    @Override
    public ItemStack craft(Inventory inv) {
        inv.getStack(0).decrement(1);
        inv.getStack(1).damage(1, new Random(), null);
        return getOutput().copy();
    }


    @Override
    @Environment(EnvType.CLIENT)
    public ItemStack getRecipeKindIcon() {
        return new ItemStack(SimpleQuernBlocks.QUERN);
    }

    @Override
    public RecipeType<?> getType() {
        return SimpleQuernRecipeTypes.GRINDING;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack item = inv.getStack(0);
        ItemStack tool = inv.getStack(1);
        if (tool.getItem() instanceof Handstone && (((Handstone) tool.getItem()).grindTier() >= this.grindTier)) {
            return validInput(item);
        }
        return false;
    }

    @Override
    public DefaultedList<Ingredient> getPreviewInputs() {
        return this.input.previews();
    }

    public static class Serializer implements RecipeSerializer<GrindingRecipe> {

        private static final Codec<GrindingRecipe> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Identifier.CODEC.fieldOf("id").forGetter((config) -> {
                return config.id;
            }), Codec.INT.fieldOf("tier").forGetter((config) -> {
                return config.grindTier;
            }), Codec.INT.fieldOf("time").forGetter((config) -> {
                return config.grindTime;
            }), RecipeOutput.CODEC.fieldOf("output").forGetter((config) -> {
                return config.output;
            }), RecipeInput.CODEC.fieldOf("input").forGetter((config) -> {
                return config.input;
            })).apply(instance, GrindingRecipe::new);
        });

        public Serializer() { }

        @Override
        public GrindingRecipe read(Identifier id, JsonObject json) {
            json.addProperty("id", id.toString());
            DataResult<GrindingRecipe> result = CODEC.parse(JsonOps.INSTANCE, json);
            if (!result.error().isPresent()) {
                return result.result().get();
            }
            return EMPTY;
        }

        @Override
        public GrindingRecipe read(Identifier id, PacketByteBuf buf) {
            CompoundTag tag = buf.method_30617();
            if (tag != null) {
                tag.putString("id", id.toString());
                DataResult<GrindingRecipe> result = CODEC.parse(NbtOps.INSTANCE, tag);
                if (!result.error().isPresent()) {
                    return result.result().get();
                }
            }
            return EMPTY;
        }

        @Override
        public void write(PacketByteBuf buf, GrindingRecipe recipe) {
            try {
                buf.encode(CODEC, recipe);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
