package com.cumulus.simplequern.client;

import com.cumulus.simplequern.client.block.QuernBlockEntityRenderer;
import com.cumulus.simplequern.client.screen.QuernScreen;
import com.cumulus.simplequern.init.SimpleQuernBlockEntityTypes;
import com.cumulus.simplequern.init.SimpleQuernScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;

public class SimpleQuernClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScreenRegistry.register(SimpleQuernScreenHandlers.QUERN, QuernScreen::new);
		BlockEntityRendererRegistry.INSTANCE.register(SimpleQuernBlockEntityTypes.QUERN, QuernBlockEntityRenderer::new);
		ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
			out.accept(new ModelIdentifier("simplequern:handstone"));
			out.accept(new ModelIdentifier("simplequern:obsidian_handstone"));
			out.accept(new ModelIdentifier("simplequern:diamond_handstone"));
		});
	}
}
