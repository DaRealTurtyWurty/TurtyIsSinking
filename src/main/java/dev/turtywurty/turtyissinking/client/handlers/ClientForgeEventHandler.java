package dev.turtywurty.turtyissinking.client.handlers;

import java.util.List;
import java.util.SortedSet;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.KeyBindings;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import dev.turtywurty.turtyissinking.client.screens.PictureGalleryScreen;
import dev.turtywurty.turtyissinking.client.screens.ReviewPictureScreen;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.items.BackpackItem;
import dev.turtywurty.turtyissinking.items.SledgehammerItem;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.InteractionKeyMappingTriggered;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent.Stage;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = TurtyIsSinking.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventHandler {
    private static final ResourceLocation HOTBAR = VanillaGuiOverlay.HOTBAR.id();
    private static final ResourceLocation ITEM_NAME = VanillaGuiOverlay.ITEM_NAME.id();
    
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        final LocalPlayer player = mc.player;
        if (player == null)
            return;

        if (KeyBindings.INSTANCE.openBackpack.isDown() && player.getItemBySlot(EquipmentSlot.CHEST).is(ItemInit.BACKPACK.get()) && mc.screen instanceof ReviewPictureScreen) {
            KeyBindings.INSTANCE.openBackpack.consumeClick();
            BackpackItem.openBackpack(player.getInventory().findSlotMatchingItem(player.getItemBySlot(EquipmentSlot.CHEST)));
        }

        if(KeyBindings.INSTANCE.openGallery.isDown() && player.getMainHandItem().is(ItemInit.CAMERA.get())) {
            KeyBindings.INSTANCE.openGallery.consumeClick();
            mc.setScreen(new PictureGalleryScreen());
        }

        if (KeyBindings.INSTANCE.wheelchairBoost.isDown() && player.getVehicle() instanceof Wheelchair wheelchair) {
            KeyBindings.INSTANCE.wheelchairBoost.consumeClick();
            wheelchair.setBoosting(true);
        } else if(!KeyBindings.INSTANCE.wheelchairBoost.isDown() && player.getVehicle() instanceof Wheelchair wheelchair) {
            wheelchair.setBoosting(false);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onClick(InteractionKeyMappingTriggered event) {
        final int key = event.getKeyMapping().getKey().getValue();
        if (key != 0 && key != 1 && key != 2)
            return;
        
        final Player player = Minecraft.getInstance().player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;
        
        if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

    @SubscribeEvent
    public static void renderBlockHighlight(RenderHighlightEvent.Block event) {
        final Player player = Minecraft.getInstance().player;
        if(player == null)
            return;
        
        if (player.getMainHandItem().is(ItemInit.SLEDGEHAMMER.get())) {
            final List<BlockPos> positions = SledgehammerItem.getValidPositions(player, event.getTarget());
            
            final VertexConsumer consumer = event.getMultiBufferSource().getBuffer(RenderType.lines());
            for (final BlockPos blockPos : positions) {
                final BlockState state = player.level.getBlockState(blockPos);
                if (state.isAir() && !player.level.getWorldBorder().isWithinBounds(blockPos)) {
                    continue;
                }
                
                final Vec3 camPos = event.getCamera().getPosition();
                Minecraft.getInstance().levelRenderer.renderHitOutline(event.getPoseStack(), consumer, player,
                    camPos.x(), camPos.y(), camPos.z(), blockPos, state);
            }
        }
    }

    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
        final Player player = Minecraft.getInstance().player;
        if(player == null)
            return;

        if (player.getMainHandItem().is(ItemInit.CAMERA.get())) {
            event.setCanceled(true);
            return;
        }

        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;
        
        if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderHighlight(RenderHighlightEvent event) {
        final Player player = Minecraft.getInstance().player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;
        
        if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderHUD(RenderGuiOverlayEvent.Pre event) {
        Screen screen = Minecraft.getInstance().screen;
        if(screen instanceof ReviewPictureScreen || screen instanceof PictureGalleryScreen) {
            event.setCanceled(true);
            return;
        }

        final Player player = Minecraft.getInstance().player;
        if (player.getMainHandItem().is(ItemInit.CAMERA.get())
            && !(TurtyIsSinking.MODID + ":camera").equals(event.getOverlay().id().toString())) {
            event.setCanceled(true);
            return;
        }
        
        if (event.getOverlay().id().equals(HOTBAR)) {
            final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
            if (cap == null)
                return;
            
            if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
                Hooks.renderHotbar(event, player);
                event.setCanceled(true);
            }
        } else if (event.getOverlay().id().equals(ITEM_NAME)) {
            final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
            if (cap == null)
                return;
            
            if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() == Stage.AFTER_PARTICLES) {
            final Player player = Minecraft.getInstance().player;
            
            if (player.getMainHandItem().is(ItemInit.SLEDGEHAMMER.get())) {
                final List<BlockPos> positions = SledgehammerItem.getValidPositions(player);
                if (Minecraft.getInstance().levelRenderer.destructionProgress.isEmpty())
                    return;
                
                for (final BlockPos blockPos : positions) {
                    final BlockState state = player.level.getBlockState(blockPos);
                    if (state.isAir() && !player.level.getWorldBorder().isWithinBounds(blockPos)) {
                        continue;
                    }
                    
                    for (final Long2ObjectMap.Entry<SortedSet<BlockDestructionProgress>> entry : Minecraft
                        .getInstance().levelRenderer.destructionProgress.long2ObjectEntrySet()) {
                        final Vec3 camPos = event.getCamera().getPosition();
                        final double camX = camPos.x();
                        final double camY = camPos.y();
                        final double camZ = camPos.z();
                        
                        final double diffX = blockPos.getX() - camX;
                        final double diffY = blockPos.getY() - camY;
                        final double diffZ = blockPos.getZ() - camZ;
                        
                        if (diffX * diffX + diffY * diffY + diffZ * diffZ <= 1024.0D) {
                            final SortedSet<BlockDestructionProgress> sortedset1 = entry.getValue();
                            if (sortedset1 != null && !sortedset1.isEmpty()) {
                                final int progress = sortedset1.last().getProgress();
                                final PoseStack poseStack = event.getPoseStack();
                                
                                poseStack.pushPose();
                                poseStack.translate(diffX, diffY, diffZ);
                                
                                final PoseStack.Pose pose = poseStack.last();
                                final VertexConsumer consumer = new SheetedDecalTextureGenerator(
                                    Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(
                                        ModelBakery.DESTROY_TYPES.get(progress)),
                                    pose.pose(), pose.normal(), 1.0f);
                                final ModelData modelData = player.level.getModelDataManager().getAt(blockPos);
                                Minecraft.getInstance().getBlockRenderer().renderBreakingTexture(
                                    player.level.getBlockState(blockPos), blockPos, player.level, poseStack, consumer,
                                    modelData == null ? ModelData.EMPTY : modelData);
                                
                                poseStack.popPose();
                            }
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void renderPlayer(RenderPlayerEvent.Pre event) {
        final PlayerBones cap = event.getEntity().getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;
        
        final List<PlayerBone> bones = cap.getSawn();
        if (bones.isEmpty())
            return;
        
        final PlayerModel model = event.getRenderer().getModel();
        for (final PlayerBone bone : bones) {
            for (final ModelPart part : bone.getParts(model)) {
                part.visible = false;
            }
        }
        
        if (bones.contains(PlayerBone.LEFT_LEG) && bones.contains(PlayerBone.RIGHT_LEG)) {
            event.getPoseStack().translate(0.0f, -0.75f, 0.0f);
        }
    }
    
    private static final class Hooks {
        private static void renderHotbar(RenderGuiOverlayEvent event, Player player) {
            final var gui = (ForgeGui) Minecraft.getInstance().gui;
            if (!gui.getMinecraft().options.hideGui) {
                if (gui.getMinecraft().gameMode.getPlayerMode() == GameType.SPECTATOR) {
                    gui.setupOverlayRenderState(true, false);
                    gui.getSpectatorGui().renderHotbar(event.getPoseStack());
                } else {
                    RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.setShaderColor(1f, 0.3f, 0.3f, 0.4f);
                    RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
                    
                    final ItemStack itemstack = player.getOffhandItem();
                    final HumanoidArm humanoidarm = player.getMainArm().getOpposite();
                    final int i = event.getWindow().getGuiScaledWidth() / 2;
                    final int j = gui.getBlitOffset();
                    gui.setBlitOffset(-90);
                    gui.blit(event.getPoseStack(), i - 91, event.getWindow().getGuiScaledHeight() - 22, 0, 0, 182, 22);
                    gui.blit(event.getPoseStack(), i - 91 - 1 + player.getInventory().selected * 20,
                        event.getWindow().getGuiScaledHeight() - 22 - 1, 0, 22, 24, 22);
                    if (!itemstack.isEmpty()) {
                        if (humanoidarm == HumanoidArm.LEFT) {
                            gui.blit(event.getPoseStack(), i - 91 - 29, event.getWindow().getGuiScaledHeight() - 23, 24,
                                22, 29, 24);
                        } else {
                            gui.blit(event.getPoseStack(), i + 91, event.getWindow().getGuiScaledHeight() - 23, 53, 22,
                                29, 24);
                        }
                    }
                    
                    gui.setBlitOffset(j);
                    RenderSystem.enableBlend();
                    RenderSystem.defaultBlendFunc();
                    int i1 = 1;
                    
                    for (int j1 = 0; j1 < 9; ++j1) {
                        final int k1 = i - 90 + j1 * 20 + 2;
                        final int l1 = event.getWindow().getGuiScaledHeight() - 16 - 3;
                        gui.renderSlot(k1, l1, event.getPartialTick(), player, player.getInventory().items.get(j1),
                            i1++);
                    }
                    
                    if (!itemstack.isEmpty()) {
                        final int j2 = event.getWindow().getGuiScaledHeight() - 16 - 3;
                        if (humanoidarm == HumanoidArm.LEFT) {
                            gui.renderSlot(i - 91 - 26, j2, event.getPartialTick(), player, itemstack, i1);
                        } else {
                            gui.renderSlot(i + 91 + 10, j2, event.getPartialTick(), player, itemstack, i1);
                        }
                    }
                    
                    RenderSystem.disableBlend();
                }
            }
        }
    }
}
