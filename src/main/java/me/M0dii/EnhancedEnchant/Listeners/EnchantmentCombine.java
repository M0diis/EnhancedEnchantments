package me.M0dii.EnhancedEnchant.Listeners;

import org.bukkit.event.EventHandler;
import me.M0dii.EnhancedEnchant.Events.CombineEvent;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryAction;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;

public class EnchantmentCombine implements Listener
{
    @EventHandler
    public void onCombine(final InventoryClickEvent e) {
        if (e.getCursor() != null && !e.getCursor().hasItemMeta())
            return;
        
        if (!e.getCursor().getType().equals(Material.ENCHANTED_BOOK))
            return;
        
        if (!e.getCursor().getItemMeta().hasEnchants())
            return;
        
        if (!e.getCursor().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY)
        && !e.getCursor().getItemMeta().hasEnchant(CustomEnchants.PLOW))
            return;
        
        if (e.getAction() != InventoryAction.SWAP_WITH_CURSOR)
            return;
        
        Player p = (Player)e.getWhoClicked();
        
        if (e.getCursor().getItemMeta().hasEnchant(CustomEnchants.TELEPATHY))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "TELEPATHY"));
        else if (e.getCursor().getItemMeta().hasEnchant(CustomEnchants.PLOW))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "PLOW"));
        
    }
}