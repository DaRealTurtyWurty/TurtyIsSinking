package dev.turtywurty.turtyissinking.client.handlers;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.client.Keys;
import dev.turtywurty.turtyissinking.client.screens.BonesawScreen.PlayerBone;
import dev.turtywurty.turtyissinking.init.ItemInit;
import dev.turtywurty.turtyissinking.items.BackpackItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.InteractionKeyMappingTriggered;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
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

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void clientTick(ClientTickEvent event) {
        if (Minecraft.getInstance().screen != null || Minecraft.getInstance().player == null
            || Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.CHEST).getItem() != ItemInit.BACKPACK.get())
            return;

        final LocalPlayer player = Minecraft.getInstance().player;
        if (Keys.INSTANCE.openBackpack.isDown()) {
            Keys.INSTANCE.openBackpack.consumeClick();
            BackpackItem
                .openBackpack(player.getInventory().findSlotMatchingItem(player.getItemBySlot(EquipmentSlot.CHEST)));
        }
    }

    @SuppressWarnings("resource")
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

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void renderHand(RenderHandEvent event) {
        final Player player = Minecraft.getInstance().player;
        final PlayerBones cap = player.getCapability(PlayerBonesCapability.PLAYER_BONES).orElse(null);
        if (cap == null)
            return;

        if (cap.getSawn().contains(PlayerBone.LEFT_ARM) && cap.getSawn().contains(PlayerBone.RIGHT_ARM)) {
            event.setCanceled(true);
        }
    }

    @SuppressWarnings("resource")
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

    @SuppressWarnings("resource")
    @SubscribeEvent
    public static void renderHUD(RenderGuiOverlayEvent.Pre event) {
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
        @SuppressWarnings("resource")
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
