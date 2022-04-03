package me.m0dii.enhancedenchant.listeners.custom;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.events.CombineEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomCombine implements Listener
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
    
    private final List<String> boots = Arrays.asList(
            "NETHERITE_BOOTS", "DIAMOND_BOOTS", "GOLD_BOOTS",
            "IRON_BOOTS", "LEATHER_BOOTS");
    
    @EventHandler
    public void onCustomCombine(CombineEvent event)
    {
        ItemStack curr = event.clickEvent().getCurrentItem();
        
        if(curr == null)
            return;
        
        String ench = event.getEnchantString();
        
        if(ench.equalsIgnoreCase("bonded"))
        {
            if(ignore(curr, CustomEnchants.BONDED))
                return;
            
            if(curr.getType().equals(Material.ENCHANTED_BOOK))
                return;
            
            event.clickEvent().setCancelled(true);
            
            ItemStack item = new ItemStack(curr.getType());
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.GRAY + "Bonded I");
            
            if(combine(event, curr, CustomEnchants.BONDED, item, lore))
                return;
        }
        
        if(ench.equalsIgnoreCase("telepathy"))
        {
            if(!tools.contains(curr.getType().toString()))
                return;
            
            if(ignore(curr, CustomEnchants.TELEPATHY))
                return;
            
            event.clickEvent().setCancelled(true);
            
            ItemStack tool = new ItemStack(curr.getType());
            
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.GRAY + "Telepathy I");
            
            if(combine(event, curr, CustomEnchants.TELEPATHY, tool, lore))
                return;
        }
        
        if(ench.equalsIgnoreCase("plow"))
        {
            if(!hoes.contains(curr.getType().toString()))
                return;
            
            if(ignore(curr, CustomEnchants.PLOW))
                return;
            
            event.clickEvent().setCancelled(true);
            
            ItemStack tool = new ItemStack(curr.getType());
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.GRAY + "Plow I");
            
            if(combine(event, curr, CustomEnchants.PLOW, tool, lore))
                return;
            
        }
        
        if(ench.equalsIgnoreCase("lava_walker"))
        {
            if(!boots.contains(curr.getType().toString()))
                return;
            
            if(ignore(curr, CustomEnchants.LAVA_WALKER))
                return;
            
            event.clickEvent().setCancelled(true);
            
            ItemStack tool = new ItemStack(curr.getType());
            List<String> lore = new ArrayList<>();
            
            lore.add(ChatColor.GRAY + "Lava Walker I");
            
            if(combine(event, curr, CustomEnchants.LAVA_WALKER, tool, lore))
                return;
        }
        
        event.clickEvent().setCursor(null);
    }
    
    private boolean ignore(ItemStack item, Enchantment ench)
    {
        if(item == null)
        {
            return true;
        }
        
    
        ItemMeta meta = item.getItemMeta();
        
        if(meta == null)
        {
            return true;
        }
        
        return meta.hasEnchant(ench);
    }
    
    private boolean combine(CombineEvent event, ItemStack curr, Enchantment ench, ItemStack tool, List<String> lore)
    {
        ItemMeta meta = curr.getItemMeta();
        
        if(meta == null)
        {
            return false;
        }
        
        if(meta.getLore() != null)
        {
            for(String l : meta.getLore())
                lore.add(this.plugin.format(l));
        }
        
        meta.setLore(lore);
        meta.addEnchant(ench, 1, true);
        tool.addUnsafeEnchantment(ench, 1);
        tool.setItemMeta(meta);
        
        if(curr.getAmount() > 1)
        {
            curr.setAmount(curr.getAmount() - 1);
            event.clickEvent().setCursor(tool);
            
            return true;
        }
        
        event.clickEvent().setCurrentItem(tool);
        
        return false;
    }
}
