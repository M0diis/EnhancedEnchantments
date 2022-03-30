package me.m0dii.enhancedenchant.Utils;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Enchanter
{
    public static ItemStack getBook(String type)
    {
        EnhancedEnchant plugin = EnhancedEnchant.getPlugin(EnhancedEnchant.class);

        FileConfiguration cfg = plugin.getCfg();
        
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        
        item.addUnsafeEnchantment(CustomEnchants.parse(type), 1);
        
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(plugin.format(cfg.getString("enchants." + type.toLowerCase() + ".displayname")));
        
        List<String> lore = new ArrayList<>();
        
        for(String l : cfg.getStringList("enchants." + type.toLowerCase() + ".lore"))
        {
            lore.add(plugin.format(l));
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
}