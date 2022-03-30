package me.m0dii.enhancedenchant.listeners;

import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemDrop implements Listener
{
    private final EnhancedEnchant plugin;
    
    public ItemDrop(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        if(!plugin.getCfg().getBoolean("enchants.bonded.enabled"))
            return;
        
        ItemStack i = e.getItemDrop().getItemStack();
        
        if(i.getType() == Material.ENCHANTED_BOOK)
            return;
        
        if(i.hasItemMeta())
        {
            ItemMeta m = i.getItemMeta();
            
            if(m.hasEnchant(CustomEnchants.BONDED))
                    e.setCancelled(true);
        }
    }
    
}
