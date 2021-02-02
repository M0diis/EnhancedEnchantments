package me.M0dii.EnhancedEnchant.Commands;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Utils.Data.ConfigManager;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;

public class Unenchant implements CommandExecutor
{
    private final EnhancedEnchant plugin;
    private final ConfigManager cfg;
    
    public Unenchant(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
        
        this.cfg = plugin.getCfg();
    }
    
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(ChatColor.RED + "Console cannot perform this command"); return true;
        }
        
        Player player = (Player)sender;
        
        if(!player.hasPermission("enhancedenchant.command.unenchant"))
        {
            sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.no-permission")));
            
            return true;
        }
        
        ItemStack hand = player.getInventory().getItemInMainHand();
        
        if(hand == null)
        {
            sender.sendMessage(ChatColor.RED + "Must be holding an item to do this.");
            
            return true;
        }
        
        if(hand.getItemMeta() == null)
        {
            sender.sendMessage(ChatColor.RED + "Must be holding an enchanted item to do this.");
            
            return true;
        }
        
        if(hand.getItemMeta().hasEnchant(CustomEnchants.TELEPATHY))
        {
            hand.removeEnchantment(CustomEnchants.TELEPATHY);
            
            ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
            
            List<String> lore = new ArrayList<>();
            
            if(meta.getLore() != null)
            {
                for(String l : meta.getLore())
                {
                    if(!l.contains("Telepathy"))
                        lore.add(l);
                }
                
                meta.setLore(lore);
            }
            
            hand.setItemMeta(meta);
            
            sender.sendMessage(ChatColor.RED + "Telepathy enchant removed!");
        }
        
        if(hand.getItemMeta().hasEnchant(CustomEnchants.PLOW))
        {
            hand.removeEnchantment(CustomEnchants.PLOW); ItemMeta meta = hand.getItemMeta();
            
            List<String> lore = new ArrayList<>();
            
            if(meta.getLore() != null)
            {
                for(String l : meta.getLore())
                {
                  if(!l.contains("Plow"))
                  {
                    lore.add(l);
                  }
                } meta.setLore(lore);
            }
            
            hand.setItemMeta(meta);
            
            sender.sendMessage(ChatColor.RED + "Plow enchant removed!");
        }
        
        return true;
    }
}