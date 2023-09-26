package dev.turtywurty.turtyissinking.client.renderers.bewlr;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.turtywurty.turtyissinking.blockentities.PlayerBoneBlockEntity;
import dev.turtywurty.turtyissinking.blocks.PlayerBoneBlock;
import dev.turtywurty.turtyissinking.items.PlayerBoneItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerBoneItemRenderer extends BlockEntityWithoutLevelRenderer {
    public static final PlayerBoneItemRenderer INSTANCE = new PlayerBoneItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final Minecraft minecraft = Minecraft.getInstance();

    private final Map<PlayerBoneItem, PlayerBoneBlockEntity> blockEntityMap = new HashMap<>();

    public PlayerBoneItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        this.blockEntityRenderDispatcher = pBlockEntityRenderDispatcher;
    }

    @Override
    public void renderByItem(@NotNull ItemStack pStack, ItemTransforms.@NotNull TransformType pTransformType,
                             @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight,
                             int pPackedOverlay) {
        if(pStack.getItem() instanceof PlayerBoneItem blockItem) {
            if (!this.blockEntityMap.containsKey(pStack.getItem())) {
                if(!PlayerBoneItem.hasPlayer(pStack)) {
                    Player player = this.minecraft.player;
                    if(player != null) {
                        PlayerBoneItem.setPlayer(pStack, player);
                    }
                }

                var blockEntity = new PlayerBoneBlockEntity(BlockPos.ZERO, blockItem.getBlock().defaultBlockState());

                GameProfile profile = PlayerBoneItem.getPlayer(pStack);
                blockEntity.setGameProfile(profile);

                this.blockEntityMap.put(blockItem, blockEntity);
            }
        } else
            return;

        PlayerBoneBlockEntity blockEntity = this.blockEntityMap.get(pStack.getItem());
        if (blockEntity == null)
            return;

        BlockEntityRenderer<PlayerBoneBlockEntity> renderer = this.blockEntityRenderDispatcher.getRenderer(blockEntity);
        if (renderer != null) {
            renderer.render(blockEntity, this.minecraft.getPartialTick(), pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }
    }
}
