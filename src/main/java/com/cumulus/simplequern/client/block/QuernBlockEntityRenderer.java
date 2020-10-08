package com.cumulus.simplequern.client.block;

import com.cumulus.simplequern.block.entity.QuernBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;

public class QuernBlockEntityRenderer extends BlockEntityRenderer<QuernBlockEntity> {
    public QuernBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(QuernBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockState state = entity.getWorld().getBlockState(entity.getPos());

        renderBlocksModel(state, matrices, vertexConsumers, light, overlay);

        ItemStack handstone = entity.getToolStack();
        if (!handstone.isEmpty()) {
            ModelIdentifier itemId = new ModelIdentifier(Registry.ITEM.getId(handstone.getItem()).toString());

            BakedModel model = client.getBlockRenderManager().getModels().getModelManager().getModel(itemId);

            matrices.push();

            matrices.translate(0.5, 0, 0.5);
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(entity.getAnimationProgress(tickDelta) * -90));
            matrices.translate(-0.5, 0, -0.5);
            client.getBlockRenderManager().getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, model, 1, 1,1, light, overlay);

            matrices.pop();
        }

    }

    private void renderBlocksModel(BlockState state, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager renderManager = client.getBlockRenderManager();
        BakedModel bakedModel = renderManager.getModel(state);
        int i = client.getBlockColors().getColor(state, (BlockRenderView)null, (BlockPos)null, 0);
        float f = (float)(i >> 16 & 255) / 255.0F;
        float g = (float)(i >> 8 & 255) / 255.0F;
        float h = (float)(i & 255) / 255.0F;
        renderManager.getModelRenderer().render(matrices.peek(), vertexConsumer.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, bakedModel, f, g, h, light, overlay);
    }
}
