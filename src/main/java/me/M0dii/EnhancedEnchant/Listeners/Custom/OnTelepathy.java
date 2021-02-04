package me.M0dii.EnhancedEnchant.Listeners.Custom;

import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Events.TelepathyEvent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Random;

public class OnTelepathy implements Listener
{
    private final Random r = new Random();
    
    private final EnhancedEnchant plugin = EnhancedEnchant.getPlugin(EnhancedEnchant.class);
    
    @EventHandler
    public void onTelepathy(TelepathyEvent e)
    {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        PlayerInventory inv = player.getInventory();
        ItemStack hand = inv.getItemInMainHand();
        Damageable itemDam = (Damageable)hand.getItemMeta();
        
        Collection<ItemStack> drops = block.getDrops(hand);
        
        boolean silk = hand.getItemMeta().getEnchants().containsKey(Enchantment.SILK_TOUCH);
    
        boolean fits = doesFit(inv, drops);
    
        if(!fits)
        {
            for(ItemStack i : drops)
                block.getWorld().dropItemNaturally(
                        block.getLocation(), i);
    
            applyDurability(hand, itemDam);
            
            return;
        }
    
        if(block.getBlockData() instanceof Bed)
        {
            if(((Bed)block.getBlockData()).getPart().equals(Bed.Part.FOOT))
                inv.addItem(new ItemStack(block.getType()));
        }
        else if(silk)
        {
            inv.addItem(new ItemStack(block.getType()));
        }
        else if(inv.firstEmpty() == -1)
        {
            ItemStack item = drops.iterator().next();
            
            if(inv.contains(item))
                addToStack(player, drops);
        }
        else
        {
            for(ItemStack i : drops)
                inv.addItem(i);
    
            if(hand.getType().getMaxDurability() <= itemDam.getDamage())
            {
                inv.removeItem(hand);
        
                return;
            }
        }
    
        applyDurability(hand, itemDam);
    }
    
    public boolean doesFit(Inventory inv, Collection<ItemStack> drops)
    {
        for (ItemStack i : inv.getStorageContents())
            if(i == null)
                return true;
    
        return hasSpaceForItem(drops, inv);
    }
    
    private void applyDurability(ItemStack hand, Damageable itemDam)
    {
        boolean contains = hand.getItemMeta().getEnchants().containsKey(Enchantment.DURABILITY);
        
        int unb = 0;
        
        if(contains)
            unb = hand.getItemMeta().getEnchants().get(Enchantment.DURABILITY);
        
        int chance = (100)/(1 + unb);
        
        int res = r.nextInt(100 - 1) + 1;
        
        if(res < chance)
            itemDam.setDamage(itemDam.getDamage() + 1);
        
        hand.setItemMeta((ItemMeta)itemDam);
    }
    
    private void addToStack(Player p, Collection<ItemStack> drops)
    {
        ItemStack item = drops.iterator().next();
        ItemStack[] arrayOfItemStack;
        
        int j = (arrayOfItemStack = p.getInventory().getContents()).length;
        
        for(int i = 0; i < j; i++)
        {
            ItemStack it = arrayOfItemStack[i];
            
            if((it.equals(item)) && (it.getAmount() < 64))
            {
                for(ItemStack itm : drops)
                {
                    p.getInventory().addItem(itm);
                }
                
                break;
            }
        }
    }
    
    private boolean hasSpaceForItem(Collection<ItemStack> drops, Inventory inv)
    {
        ItemStack item = drops.iterator().next();
        
        for(ItemStack it : inv.getStorageContents())
        {
            if((it.equals(item)) && (it.getAmount() < 64))
                return true;
        }
        
        return false;
    }
}