package me.m0dii.enhancedenchant.commands;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public class Unenchant implements CommandExecutor
{
    private final FileConfiguration cfg;
    
    public Unenchant(EnhancedEnchant plugin)
    {
        this.cfg = plugin.getCfg();
    }
    
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Console cannot perform this command");
            
            return true;
        }
        
        Player player = (Player)sender;
        
        if(!player.hasPermission("enhancedenchant.command.unenchant"))
        {
            sender.sendMessage(this.format(this.cfg.getString("messages.no-permission")));
            
            return true;
        }
        
        ItemStack hand = player.getInventory().getItemInMainHand();
        
        if(hand == null)
        {
            sender.sendMessage(ChatColor.RED + "Must be holding an item to do this.");
            
            return true;
        }
    
        ItemMeta meta = hand.getItemMeta();
        
        if(meta == null)
        {
            sender.sendMessage(ChatColor.RED + "Must be holding an enchanted item to do this.");
            
            return true;
        }
    
        if(meta.hasEnchant(CustomEnchants.LAVA_WALKER))
            unenchant(sender, hand, CustomEnchants.LAVA_WALKER, hand, "Lava Walker");
        
        if(meta.hasEnchant(CustomEnchants.TELEPATHY))
            unenchant(sender, hand, CustomEnchants.TELEPATHY, hand, "Telepathy");
        
        if(meta.hasEnchant(CustomEnchants.PLOW))
            unenchant(sender, hand, CustomEnchants.PLOW, hand, "Plow");
    
        if(meta.hasEnchant(CustomEnchants.BONDED))
            unenchant(sender, hand, CustomEnchants.BONDED, hand, "Bonded");
        
        return true;
    }
    
    private void unenchant(@Nonnull CommandSender pl, ItemStack hand, Enchantment ench,
                           ItemStack itemInMainHand, String s)
    {
        hand.removeEnchantment(ench);
        
        ItemMeta meta = itemInMainHand.getItemMeta();
        
        List<String> lore = new ArrayList<>();
        
        if(meta.getLore() != null)
        {
            for(String l : meta.getLore())
            {
                if(!l.contains(s))
                    lore.add(l);
            }
            
            meta.setLore(lore);
        }
        
        hand.setItemMeta(meta);
        
        pl.sendMessage(format(this.cfg.getString("messages.enchant-removed")
                .replace("%enchant_name%", s)));
    }
    
    public String format(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}