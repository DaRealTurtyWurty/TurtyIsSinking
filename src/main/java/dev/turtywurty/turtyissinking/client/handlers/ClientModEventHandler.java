package dev.turtywurty.turtyissinking.client.handlers;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.Keys;
import dev.turtywurty.turtyissinking.client.layers.BackpackLayer;
import dev.turtywurty.turtyissinking.client.models.BackpackModel;
import dev.turtywurty.turtyissinking.client.models.BossBabyModel;
import dev.turtywurty.turtyissinking.client.models.WheelchairModel;
import dev.turtywurty.turtyissinking.client.renderers.blockentity.BackpackBERenderer;
import dev.turtywurty.turtyissinking.client.renderers.blockentity.PlayerBoneBERenderer;
import dev.turtywurty.turtyissinking.client.renderers.entity.BossBabyRenderer;
import dev.turtywurty.turtyissinking.client.renderers.entity.WheelchairRenderer;
import dev.turtywurty.turtyissinking.client.screens.BackpackScreen;
import dev.turtywurty.turtyissinking.client.util.RenderingUtils;
import dev.turtywurty.turtyissinking.init.BlockEntityInit;
import dev.turtywurty.turtyissinking.init.EntityInit;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.init.MenuInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent.AddLayers;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TurtyIsSinking.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void addEntityLayers(AddLayers event) {
        for (final String skin : event.getSkins()) {
            final LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = event
                .getSkin(skin);
            renderer.addLayer(new BackpackLayer(renderer));
        }
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(MenuInit.BACKPACK.get(), (menu, inventory, $) -> new BackpackScreen(menu, inventory));
        });
    }
    
    @SubscribeEvent
    public static void registerEntityRenderers(RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.BACKPACK.get(), BackpackBERenderer::new);
        event.registerEntityRenderer(EntityInit.BOSS_BABY.get(), BossBabyRenderer::new);
        event.registerEntityRenderer(EntityInit.WHEELCHAIR.get(), WheelchairRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.PLAYER_BONE.get(), PlayerBoneBERenderer::new);
    }
    
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keys.INSTANCE.openBackpack);
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BackpackModel.LAYER_LOCATION, BackpackModel::createMainLayer);
        event.registerLayerDefinition(BossBabyModel.LAYER_LOCATION, BossBabyModel::createBodyLayer);
        event.registerLayerDefinition(WheelchairModel.LAYER_LOCATION, WheelchairModel::createBodyLayer);
    }
    
    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("camera", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            final Player player = Minecraft.getInstance().player;
            if (player.getMainHandItem().is(ItemInit.CAMERA.get())) {
                RenderingUtils.renderVignette(screenWidth, screenHeight, 1.25f);
                
                RenderingUtils.renderCorners(poseStack, screenWidth, screenHeight, 10, 10, 40, 2, 0xFFFFFFFF);
                RenderingUtils.renderCorners(poseStack, screenWidth, screenHeight, (int) (screenWidth / 2.5f),
                    (int) (screenHeight / 2.5f), 20, 2, 0xFFFFFFFF);

                RenderingUtils.renderCrosshair(poseStack, screenWidth / 2, screenHeight / 2, 5, 2, 0xFFFFFFFF);
                
                RenderingUtils.renderCircle(poseStack, 20, 20, 20, 255, 0, 0, 1.0f);
            }
        });
    }

}
