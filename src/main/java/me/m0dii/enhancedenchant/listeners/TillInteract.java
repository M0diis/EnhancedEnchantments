package me.m0dii.enhancedenchant.listeners;

import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.events.PlowEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TillInteract implements Listener
{
    private final EnhancedEnchant plugin;
    
    public TillInteract(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onTill(PlayerInteractEvent e)
    {
        if(!plugin.getCfg().getBoolean("enchants.plow.enabled"))
            return;
        
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
        Player p = e.getPlayer();
        
        if(hand == null)
            return;
    
        if(hand.getItemMeta() == null)
            return;
        
        if(!hand.getItemMeta().hasEnchant(CustomEnchants.PLOW))
            return;

        if((p.getGameMode() == GameMode.CREATIVE)
        || (p.getGameMode() == GameMode.SPECTATOR))
            return;

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        if(e.isCancelled())
            return;
        
        Bukkit.getPluginManager().callEvent(new PlowEvent(p, e));
    }
}
