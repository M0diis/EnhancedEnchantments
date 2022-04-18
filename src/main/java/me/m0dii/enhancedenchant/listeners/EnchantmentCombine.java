package me.m0dii.enhancedenchant.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import me.m0dii.enhancedenchant.events.CombineEvent;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryAction;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantmentCombine implements Listener
{
    @EventHandler
    public void onCombine(final InventoryClickEvent e)
    {
        ItemStack cursor = e.getCursor();
        
        if(cursor == null)
            return;
        
        if(!cursor.hasItemMeta())
            return;
        
        if(!cursor.getType().equals(Material.ENCHANTED_BOOK))
            return;
        
        if(!cursor.getItemMeta().hasEnchants())
            return;
    
        ItemMeta m = cursor.getItemMeta();
        
        if(!m.hasEnchant(CustomEnchants.TELEPATHY)
        && !m.hasEnchant(CustomEnchants.PLOW)
        && !m.hasEnchant(CustomEnchants.LAVA_WALKER)
        && !m.hasEnchant(CustomEnchants.BONDED)
        && !m.hasEnchant(CustomEnchants.OXIDIZING)
        )
            return;
    
        Player p = (Player)e.getWhoClicked();
    
        if(!p.getGameMode().equals(GameMode.CREATIVE))
            if(e.getAction() != InventoryAction.SWAP_WITH_CURSOR)
                return;
            
        if (cursor.getItemMeta().hasEnchant(CustomEnchants.TELEPATHY))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "TELEPATHY"));
        else if (cursor.getItemMeta().hasEnchant(CustomEnchants.PLOW))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "PLOW"));
        else if (cursor.getItemMeta().hasEnchant(CustomEnchants.LAVA_WALKER))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "LAVA_WALKER"));
        else if (cursor.getItemMeta().hasEnchant(CustomEnchants.BONDED))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "BONDED"));
        else if (cursor.getItemMeta().hasEnchant(CustomEnchants.OXIDIZING))
            Bukkit.getPluginManager().callEvent(new CombineEvent(p, e, "OXIDIZING"));
    }
}