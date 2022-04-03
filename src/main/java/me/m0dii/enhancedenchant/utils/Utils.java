package me.m0dii.enhancedenchant.utils;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Utils
{
    private static final Pattern HEX_PATTERN = Pattern.compile("#([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])([A-Fa-f0-9])");
    
    public static String format(String text)
    {
        if(text == null || text.isEmpty())
            return "";
        
        return ChatColor.translateAlternateColorCodes('&', HEX_PATTERN.matcher(text).replaceAll("&x&$1&$2&$3&$4&$5&$6"));
    }
    
    public static String stripColor(String text)
    {
        return ChatColor.stripColor(format(text));
    }
    
    public static ItemStack getBook(String type)
    {
        EnhancedEnchant plugin = EnhancedEnchant.getPlugin(EnhancedEnchant.class);
        
        FileConfiguration cfg = plugin.getCfg();
        
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        
        item.addUnsafeEnchantment(CustomEnchants.parse(type), 1);
        
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(Utils.format(cfg.getString("enchants." + type.toLowerCase() + ".displayname")));
        
        List<String> lore = new ArrayList<>();
        
        for(String l : cfg.getStringList("enchants." + type.toLowerCase() + ".lore"))
        {
            lore.add(Utils.format(l));
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
}
