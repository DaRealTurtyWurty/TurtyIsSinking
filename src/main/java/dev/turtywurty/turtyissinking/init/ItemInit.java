package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.items.BackpackItem;
import dev.turtywurty.turtyissinking.items.BonesawItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
        TurtyIsSinking.MODID);

    public static final RegistryObject<BackpackItem> BACKPACK = ITEMS.register("backpack",
        () -> new BackpackItem(defaultProps()));

    public static final RegistryObject<BlockItem> EXP_ORE = ITEMS.register("exp_ore",
        () -> new BlockItem(BlockInit.EXP_ORE.get(), defaultProps()));

    public static final RegistryObject<BonesawItem> BONESAW = ITEMS.register("bonesaw",
        () -> new BonesawItem(defaultProps()));

    public static final RegistryObject<BlockItem> PLAYER_LEFT_ARM = ITEMS.register("player_left_arm",
        () -> new BlockItem(BlockInit.PLAYER_LEFT_ARM.get(), defaultProps()));
    public static final RegistryObject<BlockItem> PLAYER_RIGHT_ARM = ITEMS.register("player_right_arm",
        () -> new BlockItem(BlockInit.PLAYER_RIGHT_ARM.get(), defaultProps()));
    public static final RegistryObject<BlockItem> PLAYER_LEFT_LEG = ITEMS.register("player_left_leg",
        () -> new BlockItem(BlockInit.PLAYER_LEFT_LEG.get(), defaultProps()));
    public static final RegistryObject<BlockItem> PLAYER_RIGHT_LEG = ITEMS.register("player_right_leg",
        () -> new BlockItem(BlockInit.PLAYER_RIGHT_LEG.get(), defaultProps()));
    public static final RegistryObject<BlockItem> PLAYER_BODY = ITEMS.register("player_body",
        () -> new BlockItem(BlockInit.PLAYER_BODY.get(), defaultProps()));

    public static Item.Properties defaultProps() {
        return new Item.Properties().tab(TurtyIsSinking.TAB);
    }
}
