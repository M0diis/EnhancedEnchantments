package me.m0dii.enhancedenchant.listeners.custom;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.events.TelepathyEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class OnTelepathy implements Listener
{
    private final Random r = new Random();
    private final EnhancedEnchant plugin;
    
    public OnTelepathy(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onTelepathy(TelepathyEvent e)
    {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        PlayerInventory inv = player.getInventory();
        ItemStack hand = inv.getItemInMainHand();
        Damageable itemDam = (Damageable)hand.getItemMeta();
        
        Collection<ItemStack> drops = block.getDrops(hand);
        
        boolean silk = hand.getItemMeta().getEnchants()
                .containsKey(Enchantment.SILK_TOUCH);
        
        Map<Integer, ItemStack> didntFit = new HashMap<>();
        
        if(silk)
        {
            ItemStack itm = new ItemStack(block.getType());
            
            String name = itm.getType().name();
            
            if(name.contains("WALL_") || name.contains("BANNER"))
            {
                if(name.contains("BANNER"))
                {
                    for(ItemStack i : drops)
                        inv.addItem(i);
                }
                else
                {
                    Material m = Material.getMaterial(name.replace("WALL_", ""));
    
                    if(m != null)
                        didntFit = inv.addItem(new ItemStack(m));
                }
            }
            else didntFit = inv.addItem(itm);
        }
        else
        {
            for(ItemStack i : drops)
                didntFit = inv.addItem(i);
        }
    
        NBTItem item = new NBTItem(hand);
        
        if(!item.getKeys().contains("Unbreakable"))
        {
            if(hand.getType().getMaxDurability() <= itemDam.getDamage())
            {
                inv.removeItem(hand);
            
                return;
            }
        }
        
        if(!didntFit.isEmpty())
        {
            didntFit.forEach((k, v) -> block.getWorld()
                    .dropItemNaturally(block.getLocation(), v));
        }
        
        applyDurability(hand, itemDam, block);
    }
    
    private static final List<String> PLANTS = Arrays.asList(
            "CARROTS",
            "WHEAT",
            "NETHER_WART",
            "POTATOES",
            "PUMPKIN_STEM",
            "ATTACHED_PUMPKIN_STEM",
            "MELON_STEM",
            "ATTACHED_MELON_STEM",
            "GRASS",
            "COCOA",
            "BEETROOTS"
    );
    
    private void applyDurability(ItemStack hand, Damageable itemDam, Block b)
    {
        boolean unbreakingLevel = hand.getItemMeta().getEnchants().containsKey(Enchantment.DURABILITY);
        
        int unb = 0;
    
        if(PLANTS.contains(b.getType().name())
                || b.getType().equals(Material.SUGAR_CANE))
            return;
    
        if(NBTAPI.getInstance() == null)
            return;
        
        NBTItem item = new NBTItem(hand);
    
        if(item.getKeys().contains("Unbreakable"))
        {
            itemDam.setDamage(0);
        }
        else
        {
            if(unbreakingLevel)
                unb = hand.getItemMeta().getEnchants().get(Enchantment.DURABILITY);
    
            int chance = (100)/(1 + unb);
    
            int res = r.nextInt(100 - 1) + 1;
    
            if(res < chance)
                itemDam.setDamage(itemDam.getDamage() + 1);
        }
        
        hand.setItemMeta((ItemMeta)itemDam);
    }
}