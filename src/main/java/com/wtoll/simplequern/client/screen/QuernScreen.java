package com.wtoll.simplequern.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wtoll.simplequern.Utility;
import com.wtoll.simplequern.container.QuernContainer;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

//TODO: Finalize
public class QuernScreen extends ContainerScreen {
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("textures/gui/recipe_button.png");
    private boolean narrow;
    private final Identifier background;

    public QuernScreen(int syncId, PlayerEntity player) {
        super(new QuernContainer(syncId, player.inventory), player.inventory, new TranslatableText("container.quern"));
        // TODO: Change this
        this.background = Utility.id("textures/gui/container/quern.png");
    }

    public void init() {
        super.init();
        this.narrow = this.width < 379;
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        super.render(mouseX, mouseY, delta);
        this.drawMouseoverTooltip(mouseX, mouseY);
    }

    protected void drawForeground(int mouseX, int mouseY) {
        String string = this.title.asFormattedString();
        this.font.draw(string, (float)(this.containerWidth / 2 - this.font.getStringWidth(string) / 2), 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, (float)(this.containerHeight - 96 + 2), 4210752);
    }

    protected void drawBackground(float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.background);
        int i = this.x;
        int j = this.y;
        this.blit(i, j, 0, 0, this.containerWidth, this.containerHeight);
        int l;

        l = ((QuernContainer)this.container).getGrindProgress();
        this.blit(i + 79, j + 34, 176, 14, (int) (l*((float)23/5) + 1), 16);
    }
}
