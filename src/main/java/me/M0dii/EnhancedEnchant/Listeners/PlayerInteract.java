package me.M0dii.EnhancedEnchant.Listeners;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements org.bukkit.event.Listener
{
    private EnhancedEnchant plugin;
    
    public PlayerInteract(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @org.bukkit.event.EventHandler
    public void onItemClick(PlayerInteractEvent e)
    {
        if(!e.hasItem())
            return;
        
        if(!e.getItem().hasItemMeta())
            return;
     
        if(!e.getItem().getType().equals(Material.ENCHANTED_BOOK))
            return;
        
        if((!e.getItem().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY))
        && (!e.getItem().getItemMeta().hasEnchant(CustomEnchants.PLOW)))
            return;
        
        e.getPlayer().sendMessage(this.plugin.format(e.getItem().getItemMeta().getDisplayName()));
        
        for(String l : e.getItem().getItemMeta().getLore())
        {
            e.getPlayer().sendMessage(this.plugin.format(l));
        }
    }
}