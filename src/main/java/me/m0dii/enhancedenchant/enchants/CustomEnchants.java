package me.m0dii.enhancedenchant.enchants;

import org.bukkit.enchantments.Enchantment;

public class CustomEnchants
{
    public static final Enchantment TELEPATHY =
            new EnchantmentWrapper("telepathy", "Telepathy", 1);
    
    public static final Enchantment PLOW =
            new EnchantmentWrapper("plow", "Plow", 1);
    
    public static final Enchantment LAVA_WALKER =
            new EnchantmentWrapper("lava_walker", "Lava Walker", 1);
    
    public static final Enchantment BONDED =
            new EnchantmentWrapper("bonded", "Bonded", 1);
    
    
    public static Enchantment parse(String name)
    {
        if(name.equalsIgnoreCase("telepathy"))
            return TELEPATHY;
    
        if(name.equalsIgnoreCase("lava_walker") || name.equalsIgnoreCase("lavawalker"))
            return LAVA_WALKER;
    
        if(name.equalsIgnoreCase("bonded"))
            return BONDED;
        
        return PLOW;
    }
}