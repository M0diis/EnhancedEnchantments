package me.M0dii.EnhancedEnchant.Listeners.Custom;

import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.util.random.RandomChanceUtil;
import com.gmail.nossr50.util.skills.SkillActivationType;
import me.M0dii.EnhancedEnchant.Events.TelepathyEvent;

import java.util.Collection;
import java.util.Random;
import java.util.Set;

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

public class OnTelepathy implements Listener
{
    private final Random r = new Random();
    
    @EventHandler
    public void onTelepathy(TelepathyEvent e)
    {
        Player player = e.getPlayer();
        Block block = e.breakEvent().getBlock();
        PlayerInventory inv = player.getInventory();
        ItemStack hand = inv.getItemInMainHand();
        Damageable itemDam = (Damageable)hand.getItemMeta();
        
        Collection<ItemStack> drops = block.getDrops(hand);
    
        boolean silk = hand.getItemMeta().getEnchants().containsKey(Enchantment.SILK_TOUCH);
    
        e.breakEvent().setDropItems(false);
    
        boolean doubleDrops = RandomChanceUtil.isActivationSuccessful(
                SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP,
                SubSkillType.MINING_DOUBLE_DROPS, player);
        
        if(silk)
        {
            if(doubleDrops)
                inv.addItem(new ItemStack(block.getType()));
            
            inv.addItem(new ItemStack(block.getType()));
        }
        else if(inv.firstEmpty() == -1)
        {
            ItemStack item = drops.iterator().next();
            
            if(inv.contains(item))
            {
                if(doubleDrops)
                    addToStack(player, drops);
                
                addToStack(player, drops);
            }
        }
        else
        {
            for(ItemStack i : drops)
                inv.addItem(i);
    
            if(doubleDrops)
                for(ItemStack i : drops)
                    inv.addItem(i);
    
            if(hand.getType().getMaxDurability() <= itemDam.getDamage())
            {
                inv.removeItem(hand);
        
                return;
            }
        }
    
        block.setType(Material.AIR);
    
        applyDurability(hand, itemDam);
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
}