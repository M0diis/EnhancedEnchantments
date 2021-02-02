package me.M0dii.EnhancedEnchant.Commands;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Utils.Data.ConfigManager;
import me.M0dii.EnhancedEnchant.Utils.Enchanter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Enchant implements CommandExecutor, TabCompleter
{
    private final EnhancedEnchant plugin;
    private final ConfigManager cfg;
    
    
    public Enchant(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
        
        this.cfg = plugin.getCfg();
    }
    
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args)
    {
        if(!sender.hasPermission("enhancedenchant.command.enchant"))
        {
            sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.no-permission")));
            
            return true;
        }
        
        if(args.length == 0)
        {
            sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.usage")));
            
            return true;
        }
        
        if(!(sender instanceof Player))
        {
            sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.usage")));
    
            return true;
        }
        
        Player player = (Player)sender;
        
        if(args.length == 1)
        {
            if(args[0].equalsIgnoreCase("telepathy"))
            {
                ItemStack item = Enchanter.getBook("TELEPATHY");
                
                if(player.getInventory().firstEmpty() == -1)
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                    
                    return true;
                }
                
                player.getInventory().addItem(item);
                
                return true;
            }
            
            if(args[0].equalsIgnoreCase("plow"))
            {
                ItemStack item = Enchanter.getBook("PLOW");
                
                if(player.getInventory().firstEmpty() == -1)
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                    
                    return true;
                }
                
                player.getInventory().addItem(item);
                
                return true;
            }
            
            sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.enchantment-list")));
            
            return true;
        }
        
        if(args.length >= 2)
        {
            if(args[0].equalsIgnoreCase("telepathy"))
            {
                Player target = Bukkit.getPlayer(args[1]);
                
                ItemStack item = Enchanter.getBook("TELEPATHY");
                
                if(target == null)
                {
                    sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.player-not-found")));
                    
                    return true;
                }
                
                if(target.getInventory().firstEmpty() == -1)
                {
                    target.getWorld().dropItemNaturally(target.getLocation(), item);
                    
                    return true;
                }
                
                target.getInventory().addItem(item);
                
                return true;
            }
            
            if(args[0].equalsIgnoreCase("plow"))
            {
                Player target = Bukkit.getPlayer(args[1]);
                
                ItemStack item = Enchanter.getBook("PLOW");
                
                if(target == null)
                {
                    sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.player-not-found")));
                    
                    return true;
                }
                
                if(target.getInventory().firstEmpty() == -1)
                {
                    target.getWorld().dropItemNaturally(target.getLocation(), item);
                    
                    return true;
                }
                
                target.getInventory().addItem(item);
                
                return true;
            }
            
            sender.sendMessage(this.plugin.format(this.cfg.getConfig().getString("messages.enchantment-list")));
            
            return true;
        }
        
        return false;
    }
    
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd,
                                      @Nonnull String alias, @Nonnull String[] args)
    {
        List<String> completes = new ArrayList<>();
        
        if(args.length == 1)
        {
            completes.add("plow");
            completes.add("telepathy");
        }
    
        if(args.length == 2)
        {
            for(Player p : Bukkit.getOnlinePlayers())
            {
                completes.add(p.getName());
            }
        }
        
        return completes;
    }
}