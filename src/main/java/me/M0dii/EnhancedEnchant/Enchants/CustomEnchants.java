package me.M0dii.EnhancedEnchant.Enchants;

import org.bukkit.enchantments.Enchantment;

public class CustomEnchants
{
    public static final Enchantment TELEPATHY =
            new EnchantmentWrapper("telepathy", "Telepathy", 1);
    public static final Enchantment PLOW =
            new EnchantmentWrapper("plow", "Plow", 1);
    
    public static Enchantment parse(String en)
    {
        if(en.equalsIgnoreCase("telepathy"))
            return TELEPATHY;
        
        return PLOW;
    }
}