package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
        TurtyIsSinking.MODID);
    
    public static final RegistryObject<BackpackItem> BACKPACK = ITEMS.register("backpack",
        () -> new BackpackItem(defaultProps()));
    
    public static final RegistryObject<BlockItem> EXP_ORE = ITEMS.register("exp_ore",
        () -> new BlockItem(BlockInit.EXP_ORE.get(), defaultProps()));
    
    public static final RegistryObject<BonesawItem> BONESAW = ITEMS.register("bonesaw",
        () -> new BonesawItem(defaultProps().stacksTo(1)));
    
    public static final RegistryObject<PlayerBoneItem> PLAYER_LEFT_ARM = ITEMS.register("player_left_arm",
        () -> new PlayerBoneItem(BlockInit.PLAYER_LEFT_ARM.get(), defaultProps()));
    public static final RegistryObject<PlayerBoneItem> PLAYER_RIGHT_ARM = ITEMS.register("player_right_arm",
        () -> new PlayerBoneItem(BlockInit.PLAYER_RIGHT_ARM.get(), defaultProps()));
    public static final RegistryObject<PlayerBoneItem> PLAYER_LEFT_LEG = ITEMS.register("player_left_leg",
        () -> new PlayerBoneItem(BlockInit.PLAYER_LEFT_LEG.get(), defaultProps()));
    public static final RegistryObject<PlayerBoneItem> PLAYER_RIGHT_LEG = ITEMS.register("player_right_leg",
        () -> new PlayerBoneItem(BlockInit.PLAYER_RIGHT_LEG.get(), defaultProps()));
    public static final RegistryObject<PlayerBoneItem> PLAYER_BODY = ITEMS.register("player_body",
        () -> new PlayerBoneItem(BlockInit.PLAYER_BODY.get(), defaultProps()));
    
    public static final RegistryObject<CameraItem> CAMERA = ITEMS.register("camera",
        () -> new CameraItem(defaultProps().stacksTo(1)));
    
    public static final RegistryObject<BlockItem> QUARRY = ITEMS.register("quarry",
        () -> new BlockItem(BlockInit.QUARRY.get(), defaultProps()));
    
    public static final RegistryObject<SledgehammerItem> SLEDGEHAMMER = ITEMS.register("sledgehammer",
        () -> new SledgehammerItem(Tiers.IRON, 3, 2, defaultProps()));

    public static final RegistryObject<BlockItem> PIANO = ITEMS.register("piano",
        () -> new BlockItem(BlockInit.PIANO.get(), defaultProps()));

    public static final RegistryObject<BlockItem> CLAYMORE = ITEMS.register("claymore",
        () -> new BlockItem(BlockInit.CLAYMORE.get(), defaultProps()));

    public static final RegistryObject<ThighHighsItem> THIGH_HIGHS = ITEMS.register("thigh_highs",
        () -> new ThighHighsItem(defaultProps().stacksTo(1)));

    public static final RegistryObject<WheelchairItem> WHEELCHAIR = ITEMS.register("wheelchair",
        () -> new WheelchairItem(defaultProps().stacksTo(1)));

    public static final RegistryObject<NitroCanisterItem> NITRO_CANISTER = ITEMS.register("nitro_canister",
        () -> new NitroCanisterItem(defaultProps()));
    
    public static Item.Properties defaultProps() {
        return new Item.Properties();
    }
}
