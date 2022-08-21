package dev.turtywurty.turtyissinking.enchantments;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ModEnchantment extends Enchantment {
    private final int maxLevel;
    
    public ModEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots, int maxLevel) {
        super(rarity, category, slots);
        this.maxLevel = Mth.clamp(maxLevel, 1, Integer.MAX_VALUE);
    }

    @Override
    public int getMaxLevel() {
        return this.maxLevel;
    }
}
