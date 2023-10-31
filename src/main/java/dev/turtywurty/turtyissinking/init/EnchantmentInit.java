package dev.turtywurty.turtyissinking.init;

import dev.turtywurty.turtyissinking.TurtyIsSinking;
import dev.turtywurty.turtyissinking.enchantments.ModEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EnchantmentInit {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister
        .create(ForgeRegistries.ENCHANTMENTS, TurtyIsSinking.MOD_ID);
    
    public static final RegistryObject<Enchantment> EXPLOSIVE_TOUCH = ENCHANTMENTS.register("explosive_touch",
        () -> new ModEnchantment(Rarity.RARE, EnchantmentCategory.DIGGER,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND }, 3));
    
    public static final RegistryObject<Enchantment> STONE_TOUCH = ENCHANTMENTS.register("stone_touch",
        () -> new ModEnchantment(Rarity.UNCOMMON, EnchantmentCategory.DIGGER,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND }, 2));

    public static final RegistryObject<Enchantment> BLOCK_BREAKER = ENCHANTMENTS.register("block_breaker",
        () -> new ModEnchantment(Rarity.COMMON, EnchantmentCategory.DIGGER,
            new EquipmentSlot[] { EquipmentSlot.MAINHAND }, 2));
}
