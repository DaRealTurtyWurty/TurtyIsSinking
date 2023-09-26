package dev.turtywurty.turtyissinking.client.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBones;
import dev.turtywurty.turtyissinking.capabilities.playerbones.PlayerBonesCapability;
import dev.turtywurty.turtyissinking.networking.PacketHandler;
import dev.turtywurty.turtyissinking.networking.serverbound.SSawBonePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public class BonesawScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TurtyIsSinking.MODID,
        "textures/gui/bonesaw.png");
    
    private int leftPos, topPos;
    private int imageWidth, imageHeight;
    
    private final ItemStack item;
    
    public BonesawScreen(ItemStack item) {
        super(Component.translatable("screen." + TurtyIsSinking.MODID + ".bonesaw"));
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.item = item;
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        
        this.font.draw(pPoseStack, this.title, this.leftPos + 8, this.topPos + 6, 0x404040);
    }
    
    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        
        addRenderableWidget(new PlayerBoneButton(this, PlayerBone.HEAD));
        addRenderableWidget(new PlayerBoneButton(this, PlayerBone.BODY));
        addRenderableWidget(new PlayerBoneButton(this, PlayerBone.LEFT_ARM));
        addRenderableWidget(new PlayerBoneButton(this, PlayerBone.RIGHT_ARM));
        addRenderableWidget(new PlayerBoneButton(this, PlayerBone.LEFT_LEG));
        addRenderableWidget(new PlayerBoneButton(this, PlayerBone.RIGHT_LEG));
    }
    
    public enum PlayerBone {
        HEAD(model -> new ModelPart[] { model.getHead(), model.hat }),
        BODY(model -> new ModelPart[] { model.body, model.jacket }),
        LEFT_ARM("limb", model -> new ModelPart[] { model.leftArm, model.leftSleeve }),
        RIGHT_ARM("limb", model -> new ModelPart[] { model.rightArm, model.rightSleeve }),
        LEFT_LEG("limb", model -> new ModelPart[] { model.leftLeg, model.leftPants }),
        RIGHT_LEG("limb", model -> new ModelPart[] { model.rightLeg, model.rightPants });
        
        private final String name;
        private final ResourceLocation textureLoc;
        private final Function<PlayerModel<?>, ModelPart[]> partGetter;
        
        PlayerBone(Function<PlayerModel<?>, ModelPart[]> partGetter) {
            this.name = name().toLowerCase();
            this.textureLoc = texture(this.name);
            this.partGetter = partGetter;
        }
        
        PlayerBone(String textureName, Function<PlayerModel<?>, ModelPart[]> partGetter) {
            this.name = name().toLowerCase();
            this.textureLoc = texture(textureName);
            this.partGetter = partGetter;
        }
        
        public ModelPart[] getParts(PlayerModel<?> model) {
            return this.partGetter.apply(model);
        }

        private static ResourceLocation texture(String name) {
            return new ResourceLocation(TurtyIsSinking.MODID, "textures/gui/bonesaw/" + name + ".png");
        }
    }
    
    public static class PlayerBoneButton extends AbstractWidget {
        private final BonesawScreen screen;
        private final PlayerBone bone;
        private final Minecraft minecraft;
        
        public PlayerBoneButton(BonesawScreen screen, PlayerBone bone) {
            super(screen.leftPos, screen.topPos, 10, 10, Component.empty());
            this.screen = screen;
            this.bone = bone;
            this.width = bone == PlayerBone.HEAD || bone == PlayerBone.BODY ? 16 : 8;
            this.height = bone == PlayerBone.HEAD ? 16 : 24;
            
            setX(getX() + screen.imageWidth / 2 - this.width / 2);
            setY(getY() + screen.imageHeight / 2 - this.height / 2);
            
            switch (bone) {
                case HEAD:
                    setY(getY() - this.height - 3);
                    break;
                case RIGHT_LEG:
                    setX(getX() + this.width / 2);
                    setY(getY() + this.height);
                    break;
                case LEFT_LEG:
                    setX(getX() - this.width / 2);
                    setY(getY() + this.height);
                    break;
                case LEFT_ARM:
                    setX(getX() - this.width - 4);
                    break;
                case RIGHT_ARM:
                    setX(getX() + this.width + 4);
                    break;
                default:
                    break;
            }
            
            this.minecraft = Minecraft.getInstance();
            
            final PlayerBones cap = this.minecraft.player.getCapability(PlayerBonesCapability.PLAYER_BONES)
                .orElseThrow(() -> new IllegalStateException("Player does not contain a player bones capability."));
            this.active = !cap.getSawn().contains(this.bone);
        }
        
        @Override
        public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
            if (isMouseOver(pMouseX, pMouseY)) {
                this.active = false;
                this.minecraft.player.getCapability(PlayerBonesCapability.PLAYER_BONES)
                    .ifPresent(cap -> cap.saw(this.minecraft.player, this.bone));
                PacketHandler.INSTANCE.sendToServer(new SSawBonePacket(this.bone));
                return true;
            }
            
            return false;
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            defaultButtonNarrationText(pNarrationElementOutput);
        }

        @Override
        public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            RenderSystem.setShaderTexture(0, this.bone.textureLoc);
            float red = 1.0f, green = 1.0f, blue = 1.0f;
            if (isMouseOver(pMouseX, pMouseY)) {
                red = 0.0f;
                green = 1.5f;
                blue = 2f;
            } else if (!this.active) {
                red = 1.0f;
                green = 0.0f;
                blue = 0.0f;
            }
            
            RenderSystem.setShaderColor(red, green, blue, 1.0f);
            blit(pPoseStack, getX(), getY(), 0, 0, this.width, this.height, this.width, this.height);
        }
    }
}
