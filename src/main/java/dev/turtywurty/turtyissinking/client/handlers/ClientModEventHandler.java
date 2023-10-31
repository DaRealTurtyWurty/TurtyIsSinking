package dev.turtywurty.turtyissinking.client.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.client.ClientAccess;
import dev.turtywurty.turtyissinking.client.KeyBindings;
import dev.turtywurty.turtyissinking.client.layers.BackpackLayer;
import dev.turtywurty.turtyissinking.client.layers.ThighHighsLayer;
import dev.turtywurty.turtyissinking.client.models.*;
import dev.turtywurty.turtyissinking.client.particles.PlayerSkinParticle;
import dev.turtywurty.turtyissinking.client.renderers.blockentity.BackpackBERenderer;
import dev.turtywurty.turtyissinking.client.renderers.blockentity.ClaymoreBERenderer;
import dev.turtywurty.turtyissinking.client.renderers.blockentity.PlayerBoneBERenderer;
import dev.turtywurty.turtyissinking.client.renderers.entity.BossBabyRenderer;
import dev.turtywurty.turtyissinking.client.renderers.entity.WheelchairRenderer;
import dev.turtywurty.turtyissinking.client.screens.BackpackScreen;
import dev.turtywurty.turtyissinking.client.screens.WheelchairScreen;
import dev.turtywurty.turtyissinking.client.util.RenderingUtils;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import dev.turtywurty.turtyissinking.init.*;
import dev.turtywurty.turtyissinking.menus.BackpackMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent.AddLayers;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterLayerDefinitions;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TurtyIsSinking.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SubscribeEvent
    public static void addEntityLayers(AddLayers event) {
        EntityModelSet modelSet = event.getEntityModels();
        for (final String skin : event.getSkins()) {
            final LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer = event.getSkin(skin);
            if (renderer == null)
                continue;

            renderer.addLayer(new BackpackLayer(renderer));
            ThighHighsLayer.addTo(renderer, modelSet);
        }

        ZombieRenderer zombieRenderer = event.getRenderer(EntityType.ZOMBIE);
        if (zombieRenderer != null) {
            ThighHighsLayer.addTo(zombieRenderer, modelSet);
        }

        HuskRenderer huskRenderer = event.getRenderer(EntityType.HUSK);
        if (huskRenderer != null) {
            ThighHighsLayer.addTo(huskRenderer, modelSet);
        }

        DrownedRenderer drownedRenderer = event.getRenderer(EntityType.DROWNED);
        if (drownedRenderer != null) {
            ThighHighsLayer.addTo(drownedRenderer, modelSet);
        }

        ZombieVillagerRenderer zombieVillagerRenderer = event.getRenderer(EntityType.ZOMBIE_VILLAGER);
        if (zombieVillagerRenderer != null) {
            ThighHighsLayer.addTo(zombieVillagerRenderer, modelSet);
        }

        GiantMobRenderer giantMobRenderer = event.getRenderer(EntityType.GIANT);
        if (giantMobRenderer != null) {
            ThighHighsLayer.addTo(giantMobRenderer, modelSet);
        }

        SkeletonRenderer skeletonRenderer = event.getRenderer(EntityType.SKELETON);
        if (skeletonRenderer != null) {
            ThighHighsLayer.addTo(skeletonRenderer, modelSet);
        }

        StrayRenderer strayRenderer = event.getRenderer(EntityType.STRAY);
        if (strayRenderer != null) {
            ThighHighsLayer.addTo(strayRenderer, modelSet);
        }

        WitherSkeletonRenderer witherSkeletonRenderer = event.getRenderer(EntityType.WITHER_SKELETON);
        if (witherSkeletonRenderer != null) {
            ThighHighsLayer.addTo(witherSkeletonRenderer, modelSet);
        }

        PiglinRenderer piglinRenderer = event.getRenderer(EntityType.PIGLIN);
        if (piglinRenderer != null) {
            ThighHighsLayer.addTo(piglinRenderer, modelSet);
        }

        PiglinRenderer piglinBruteRenderer = event.getRenderer(EntityType.PIGLIN_BRUTE);
        if (piglinBruteRenderer != null) {
            ThighHighsLayer.addTo(piglinBruteRenderer, modelSet);
        }

        ArmorStandRenderer armorStandRenderer = event.getRenderer(EntityType.ARMOR_STAND);
        if (armorStandRenderer != null) {
            ThighHighsLayer.addTo(armorStandRenderer, modelSet);
        }

        EndermanRenderer endermanRenderer = event.getRenderer(EntityType.ENDERMAN);
        if (endermanRenderer != null) {
            ThighHighsLayer.addTo(endermanRenderer, modelSet);
        }
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.<BackpackMenu, BackpackScreen>register(MenuInit.BACKPACK.get(), (menu, inventory, ignored) -> new BackpackScreen(menu, inventory));
            MenuScreens.register(MenuInit.WHEELCHAIR.get(), WheelchairScreen::new);
        });
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.register(ParticleTypeInit.PLAYER_SKIN.get(), PlayerSkinParticle.Provider.INSTANCE);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntityInit.BACKPACK.get(), BackpackBERenderer::new);
        event.registerEntityRenderer(EntityInit.BOSS_BABY.get(), BossBabyRenderer::new);
        event.registerEntityRenderer(EntityInit.WHEELCHAIR.get(), WheelchairRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.PLAYER_BONE.get(), PlayerBoneBERenderer::new);
        event.registerBlockEntityRenderer(BlockEntityInit.CLAYMORE.get(), ClaymoreBERenderer::new);
    }
    
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.INSTANCE.openBackpack);
        event.register(KeyBindings.INSTANCE.openGallery);
        event.register(KeyBindings.INSTANCE.wheelchairBoost);
    }
    
    @SubscribeEvent
    public static void registerLayerDefinitions(RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BackpackModel.LAYER_LOCATION, BackpackModel::createMainLayer);
        event.registerLayerDefinition(BossBabyModel.LAYER_LOCATION, BossBabyModel::createBodyLayer);
        event.registerLayerDefinition(WheelchairModel.LAYER_LOCATION, WheelchairModel::createBodyLayer);
        event.registerLayerDefinition(ThighHighsModel.LAYER_LOCATION, ThighHighsModel::createBodyLayer);
        event.registerLayerDefinition(CameraModel.LAYER_LOCATION, CameraModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("camera", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            final Player player = Minecraft.getInstance().player;
            if (player != null && player.getMainHandItem().is(ItemInit.CAMERA.get())) {
                RenderingUtils.renderVignette(screenWidth, screenHeight, 1.25f);
                if(ClientAccess.isTakingPicture()) {
                    ClientAccess.executeTakePicture();
                    return;
                }
                
                RenderingUtils.renderCorners(poseStack, screenWidth, screenHeight, 10, 10, 40, 2, 0xFFFFFFFF);
                RenderingUtils.renderCorners(poseStack, screenWidth, screenHeight, (int) (screenWidth / 2.5f),
                    (int) (screenHeight / 2.5f), 20, 2, 0xFFFFFFFF);

                RenderingUtils.renderCrosshair(poseStack, screenWidth / 2, screenHeight / 2, 5, 2, 0xFFFFFFFF);
                
                RenderingUtils.renderCircle(poseStack, 20, 20, 20, 255, 0, 0, 1.0f);
            }
        });

        event.registerBelowAll("wheelchair_nitro", (gui, poseStack, partialTick, screenWidth, screenHeight) -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getVehicle() instanceof Wheelchair wheelchair) {
                if(!wheelchair.hasNitro())
                    return;

                int nitro = wheelchair.getNitro();
                int maxNitro = wheelchair.getMaxNitro();
                if(maxNitro == 0)
                    return;

                float percent = (float) nitro / (float) maxNitro;
                if (percent == Float.MAX_VALUE || percent == Float.MIN_VALUE || Float.isNaN(percent))
                    return;

                int width = screenWidth / 2;
                int height = 20;
                int x = screenWidth / 2 - width / 2;
                int y = 20;

                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                GuiComponent.fill(poseStack, x - 1, y - 1, x + width + 1, y + height + 1, 0x80000000);
                RenderSystem.disableBlend();

                int barWidth = (int) (width * percent);
                RenderingUtils.drawGradientRect(x, y, barWidth, height, 0xFFFF0000, 0xFF0000FF);
            }
        });
    }
}
