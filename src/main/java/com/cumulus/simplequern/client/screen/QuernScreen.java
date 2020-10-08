package com.cumulus.simplequern.client.screen;

import com.cumulus.simplequern.SimpleQuern;
import com.cumulus.simplequern.screen.QuernScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class QuernScreen extends HandledScreen<QuernScreenHandler> {
	private static final Identifier BACKGROUND = SimpleQuern.id("textures/gui/container/quern.png");

	public QuernScreen(QuernScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected void init() {
		super.init();
		this.titleX = (this.backgroundWidth - textRenderer.getWidth(this.title)) / 2;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

		float l = this.handler.getGrindProportion();
		this.drawTexture(matrices, i + 79, j + 34, 176, 14, (int)(l * ((float)23) + 1), 16);
	}
}
