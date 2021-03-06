package me.M0dii.EnhancedEnchant.Listeners.Custom;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;
import me.M0dii.EnhancedEnchant.Events.CombineEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomCombine implements org.bukkit.event.Listener
{
    private final EnhancedEnchant plugin;
    
    public CustomCombine(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    private final List<String> tools = Arrays.asList(
            "NETHERITE_AXE", "NETHERITE_SHOVEL", "NETHERITE_PICKAXE", "DIAMOND_AXE",
            "NETHERITE_HOE", "DIAMOND_SHOVEL", "DIAMOND_PICKAXE", "IRON_AXE", "IRON_SHOVEL",
            "IRON_PICKAXE", "GOLDEN_AXE", "GOLDEN_SHOVEL", "GOLDEN_PICKAXE", "STONE_AXE",
            "STONE_SHOVEL", "STONE_PICKAXE", "WOODEN_AXE", "WOODEN_SHOVEL", "WOODEN_PICKAXE",
            "DIAMOND_HOE", "IRON_HOE", "GOLDEN_HOE", "STONE_HOE", "WOODEN_HOE"
    );
    
    private final List<String> hoes = Arrays.asList(
            "NETHERITE_HOE", "DIAMOND_HOE", "IRON_HOE",
            "GOLDEN_HOE", "STONE_HOE", "WOODEN_HOE");
    
    @EventHandler
    public void onCustomCombine(CombineEvent event)
    {
        ItemStack curr = event.breakEvent().getCurrentItem();
    
        if(event.getEnchantString().equalsIgnoreCase("telepathy"))
        {
            if(!tools.contains(curr.getType().toString()))
                return;
            
            if((curr.hasItemMeta()) && (curr.getItemMeta().hasEnchant(CustomEnchants.TELEPATHY)))
                return;

            event.breakEvent().setCancelled(true);
            
            Enchantment ench = CustomEnchants.TELEPATHY;
            
            ItemStack tool = new ItemStack(curr.getType());
            
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.GRAY + "Telepathy I");
    
            if(combine(event, curr, ench, tool, lore))
                return;
        }
        else
        {
            if(!hoes.contains(curr.getType().toString()))
                return;
            
            if((curr.hasItemMeta()) && (curr.getItemMeta().hasEnchant(CustomEnchants.PLOW)))
                return;
            
            event.breakEvent().setCancelled(true);
            
            Enchantment ench = CustomEnchants.PLOW;
            ItemStack tool = new ItemStack(curr.getType());
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.GRAY + "Plow I");
            
            if(combine(event, curr, ench, tool, lore))
                return;
    
        }
        
        event.breakEvent().setCursor(null);
    }
    
    private boolean combine(CombineEvent event, ItemStack curr,
                            Enchantment ench, ItemStack tool, List<String> lore)
    {
        ItemMeta meta = curr.getItemMeta();
        
        if(meta.getLore() != null)
        {
            for(String l : meta.getLore())
                lore.add(this.plugin.format(l));
        }
        
        meta.setLore(lore);
        tool.setItemMeta(meta);
        tool.addUnsafeEnchantment(ench, 1);
        
        if(curr.getAmount() > 1)
        {
            curr.setAmount(curr.getAmount() - 1);
            event.breakEvent().setCursor(tool);
    
            return true;
        }
        
        event.breakEvent().setCurrentItem(tool);
        
        return false;
    }
}
