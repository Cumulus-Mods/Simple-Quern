package com.wtoll.simplequern.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wtoll.simplequern.Utility;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import sun.java2d.pipe.TextRenderer;

public class QuernScreen extends HandledScreen<QuernScreenHandler>
{
	private static final Identifier BACKGROUND = Utility.id("textures/gui/container/quern.png");
	//private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");
	//private final RecipeBookWidget recipeBook = new RecipeBookWidget();
	//private boolean narrow;

	public QuernScreen(QuernScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
	}

	@Override
	protected void init()
	{
		super.init();
		this.titleX = (this.backgroundWidth - textRenderer.getWidth(this.title)) / 2;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta)
	{
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
//		if (this.recipeBook.isOpen() && this.narrow)
//		{
//			this.drawBackground(matrices, delta, mouseX, mouseY);
//			this.recipeBook.render(matrices, mouseX, mouseY, delta);
//		} else {
//			this.recipeBook.render(matrices, mouseX, mouseY, delta);
//			super.render(matrices, mouseX, mouseY, delta);
//			this.recipeBook.drawGhostSlots(matrices, this.x, this.y, true, delta);
//		}

		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
		//this.recipeBook.drawTooltip(matrices, this.x, this.y, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

		float l = this.handler.getGrindProportion();
		this.drawTexture(matrices, i + 79, j + 34, 176, 14, (int)(l * ((float)23) + 1), 16);
	}
}
