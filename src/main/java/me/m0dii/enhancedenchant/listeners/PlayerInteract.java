package me.m0dii.enhancedenchant.listeners;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.utils.Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerInteract implements Listener
{
    private EnhancedEnchant plugin;
    
    public PlayerInteract(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onItemClick(PlayerInteractEvent e)
    {
        if(!e.hasItem())
            return;
        
        if(true)
            return;
        
        ItemStack i = e.getItem();
        
        if(!i.hasItemMeta())
            return;
     
        if(!i.getType().equals(Material.ENCHANTED_BOOK))
            return;
        
        ItemMeta m = i.getItemMeta();
        
        if((!m.hasEnchant(CustomEnchants.TELEPATHY))
        && (!e.getItem().getItemMeta().hasEnchant(CustomEnchants.PLOW)))
            return;
        
        e.getPlayer().sendMessage(Utils.format(e.getItem().getItemMeta().getDisplayName()));
        
        for(String l : e.getItem().getItemMeta().getLore())
            e.getPlayer().sendMessage(Utils.format(l));
    }
}