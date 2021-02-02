package me.M0dii.EnhancedEnchant.Listeners;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

public class AnvilCombine implements Listener
{
    private final EnhancedEnchant plugin;
    
    public AnvilCombine(EnhancedEnchant instance)
    {
        this.plugin = instance;
    }
    
    @EventHandler
    public void anvilCombine(PrepareAnvilEvent event)
    {
        if(event.getResult() == null)
            return;
        
        if(!event.getResult().hasItemMeta())
            return;
        
        if(!event.getResult().getItemMeta().hasLore())
            return;
        
        boolean tele = false;
        boolean plow = false;
        
        for(String l : event.getResult().getItemMeta().getLore())
        {
            if(l.contains("Telepathy"))
                tele = true;
            
            if(l.contains("Plow"))
                plow = true;
        }
        
        if(tele)
            event.getResult().addUnsafeEnchantment(CustomEnchants.TELEPATHY, 1);
        
        if(plow)
            event.getResult().addUnsafeEnchantment(CustomEnchants.PLOW, 1);
    }
}