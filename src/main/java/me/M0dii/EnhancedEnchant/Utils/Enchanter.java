package me.M0dii.EnhancedEnchant.Utils;

import me.M0dii.EnhancedEnchant.Utils.Data.DataManager;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Enchanter
{
    public static ItemStack getBook(String type)
    {
        DataManager data = DataManager.getInstance();
        
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        
        item.addUnsafeEnchantment(CustomEnchants.parse(type), 1);
        
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(data.format(data.getConfig().getString("enchants." + type.toLowerCase() + ".displayname")));
        
        List<String> lore = new ArrayList<>();
        
        for(String l : data.getConfig().getStringList("enchants." + type.toLowerCase() + ".lore"))
        {
            lore.add(data.format(l));
        }
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        return item;
    }
}