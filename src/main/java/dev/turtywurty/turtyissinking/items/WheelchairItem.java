package dev.turtywurty.turtyissinking.items;

import dev.turtywurty.turtyissinking.client.renderers.bewlr.WheelchairItemRenderer;
import dev.turtywurty.turtyissinking.entities.Wheelchair;
import dev.turtywurty.turtyissinking.init.EntityInit;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class WheelchairItem extends Item {
    public WheelchairItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos().above();

        Wheelchair wheelchair = EntityInit.WHEELCHAIR.get().create(level);
        if(wheelchair == null)
            return InteractionResult.FAIL;

        wheelchair.setPos(pos.getCenter());
        level.addFreshEntity(wheelchair);

        return InteractionResult.CONSUME;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return WheelchairItemRenderer.INSTANCE;
            }
        });
    }
}
