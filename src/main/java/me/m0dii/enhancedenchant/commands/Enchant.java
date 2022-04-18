package me.m0dii.enhancedenchant.commands;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Enchant implements CommandExecutor, TabCompleter
{
    private final FileConfiguration cfg;
    
    public Enchant(EnhancedEnchant plugin)
    {
        this.cfg = plugin.getCfg();
    }
    
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd,
                             @Nonnull String label, @Nonnull String[] args)
    {
        if(!sender.hasPermission("enhancedenchant.command.enchant"))
        {
            sender.sendMessage(Utils.format(this.cfg.getString("messages.no-permission")));
            
            return true;
        }
        
        if(args.length == 0)
        {
            sender.sendMessage(Utils.format(this.cfg.getString("messages.usage")));
            
            return true;
        }
    
        if(args.length == 2 && args[0].equalsIgnoreCase("apply"))
        {
            if(!(sender instanceof Player player))
            {
                sender.sendMessage(Utils.format(this.cfg.getString("messages.usage")));
    
                return true;
            }
    
            Enchantment ench = CustomEnchants.parse(args[1]);
            
            ItemStack curr = player.getInventory().getItemInMainHand();
            
            if((curr.hasItemMeta()) && (curr.getItemMeta().hasEnchant(ench)))
            {
                player.sendMessage(Utils.format("&7Item already has the enchant."));
                
                return true;
            }
            
            List<String> lore = new ArrayList<>();
    
            lore.add(ChatColor.GRAY + ench.getName() + " I");
    
            combine(curr, ench, curr, lore);
        }
        
        if(args.length == 2 && args[0].equalsIgnoreCase("give"))
        {
            if(!(sender instanceof Player player))
            {
                sender.sendMessage(Utils.format(this.cfg.getString("messages.usage")));
        
                return true;
            }
    
            if(args[1].equalsIgnoreCase("telepathy"))
                giveBook(player, "TELEPATHY", 1);
            
            if(args[1].equalsIgnoreCase("plow"))
                giveBook(player, "PLOW", 1);
    
            if(args[1].equalsIgnoreCase("lavawalker"))
                giveBook(player, "LAVA_WALKER", 1);
            
            if(args[1].equalsIgnoreCase("bonded"))
                giveBook(player, "BONDED", 1);
            
            if(args[1].equalsIgnoreCase("oxidizing"))
                giveBook(player, "OXIDIZING", 1);
            
            sender.sendMessage(Utils.format(this.cfg.getString("messages.enchantment-list")));
            
            return true;
        }
        
        if(args.length >= 3 && args[0].equalsIgnoreCase("give"))
        {
            int amount = 1;
            
            if(args.length == 4)
            {
                try
                {
                    amount = Integer.parseInt(args[3]);
                }
                catch(NumberFormatException ignore) { }
            }
    
            Player target = Bukkit.getPlayer(args[2]);
    
            if(target == null)
            {
                sender.sendMessage(Utils.format(this.cfg.getString("messages.player-not-found")));
        
                return true;
            }
    
            if(args[1].equalsIgnoreCase("bonded"))
            {
                giveBook(target, "BONDED", amount);
        
                return true;
            }
            
            if(args[1].equalsIgnoreCase("telepathy"))
            {
                giveBook(target, "TELEPATHY", amount);
                
                return true;
            }
            
            if(args[1].equalsIgnoreCase("plow"))
            {
                giveBook(target, "PLOW", amount);
                
                return true;
            }
    
            if(args[1].equalsIgnoreCase("lavawalker"))
            {
                giveBook(target, "LAVA_WALKER", amount);
        
                return true;
            }
            
            if(args[1].equalsIgnoreCase("oxidizing"))
            {
                giveBook(target, "OXIDIZING", amount);
        
                return true;
            }
            
            sender.sendMessage(Utils.format(this.cfg.getString("messages.enchantment-list")));
            
            return true;
        }
        
        return false;
    }
    
    private void giveBook(Player p, String name, int amount)
    {
        ItemStack item = Utils.getBook(name);
    
        for(int i = 0; i < amount; i++)
        {
            if(p.getInventory().firstEmpty() == -1)
                p.getWorld().dropItemNaturally(p.getLocation(), item);
            else p.getInventory().addItem(item);
        }
    }
    
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd,
                                      @Nonnull String alias, @Nonnull String[] args)
    {
        List<String> completes = new ArrayList<>();
    
        if(args.length == 1)
        {
            completes.add("give");
            completes.add("apply");
        }
        
        if(args.length == 2)
        {
            completes.add("plow");
            completes.add("telepathy");
            completes.add("lavawalker");
            completes.add("bonded");
            completes.add("oxidizing");
        }
    
        if(args.length == 3)
        {
            for(Player p : Bukkit.getOnlinePlayers())
                if(p.getName().toLowerCase().startsWith(args[2].toLowerCase()))
                    completes.add(p.getName());
        }
        
        return completes;
    }
    
    private void combine(ItemStack curr, Enchantment ench,
                         ItemStack tool, List<String> lore)
    {
        ItemMeta meta = curr.getItemMeta();
        
        if(meta.getLore() != null)
        {
            for(String l : meta.getLore())
                lore.add(Utils.format(l));
        }
        
        meta.setLore(lore);
        tool.setItemMeta(meta);
        tool.addUnsafeEnchantment(ench, 1);
        
        if(curr.getAmount() > 1)
            curr.setAmount(curr.getAmount() - 1);
    }
}