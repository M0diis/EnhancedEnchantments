package me.m0dii.enhancedenchant.enchants;

import java.lang.reflect.Field;
import org.bukkit.Bukkit;

import java.util.stream.Collectors;
import java.util.Arrays;
import org.bukkit.enchantments.Enchantment;
import java.util.List;

public class RegisterEnchants
{
    public static void register()
    {
        List<Enchantment> list = Arrays.stream(Enchantment.values()).toList();
        
        registerEnchantment(CustomEnchants.TELEPATHY, list);
    
        registerEnchantment(CustomEnchants.PLOW, list);
    
        registerEnchantment(CustomEnchants.LAVA_WALKER, list);
    
        registerEnchantment(CustomEnchants.BONDED, list);
    
        registerEnchantment(CustomEnchants.OXIDIZING, list);
    }
    
    private static void registerEnchantment(final Enchantment enchantment, List<Enchantment> list)
    {
        if (list.contains(enchantment))
            return;
        
        try
        {
            final Field f = Enchantment.class.getDeclaredField("acceptingNew");
            
            f.setAccessible(true);
            f.set(null, true);
            
            Enchantment.registerEnchantment(enchantment);
        }
        catch (Exception e)
        {
            Bukkit.getLogger().severe("[EnhancedEnchants] Could not register enchantment: " + enchantment.getKey());
        }
    }
}
