package me.m0dii.enhancedenchant.listeners;

import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.events.TelepathyEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BlockBreak implements Listener
{
    private final List<String> HEADS = Arrays.asList("PLAYER_HEAD", "SKELETON_SKULL", "CREEPER_HEAD", "WITHER_SKELETON_SKULL",
            "ZOMBIE_HEAD", "CREEPER_WALL_HEAD", "PLAYER_WALL_HEAD", "DRAGON_HEAD", "DRAGON_WALL_HEAD", "ZOMBIE_WALL_HEAD",
            "SKELETON_WALL_SKULL", "WITHER_SKELETON_WALL_SKULL");
    
    private final EnhancedEnchant plugin;
    
    public BlockBreak(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if(!plugin.getCfg().getBoolean("enchants.telepathy.enabled"))
            return;
        
        if(e.isCancelled())
            return;
        
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
        
        if(hand == null)
            return;
        
        if(hand.getItemMeta() == null)
            return;
  
        if(!hand.getItemMeta().hasEnchant(CustomEnchants.TELEPATHY))
            return;

        if((e.getPlayer().getGameMode() == GameMode.CREATIVE)
        || (e.getPlayer().getGameMode() == GameMode.SPECTATOR))
            return;

        if((e.getBlock().getState() instanceof Container))
            return;
        
        Player p = e.getPlayer();
        Block b = e.getBlock();
    
        if(this.HEADS.contains(b.getType().toString()))
            return;
    
        Collection<ItemStack> drops = b.getDrops(hand);
    
        e.setDropItems(false);
        
        if(b.getType().name().contains("BED"))
        {
            p.getInventory().addItem(new ItemStack(b.getType()));
            
            return;
        }
        
        if(drops.isEmpty())
            return;
        
        Bukkit.getPluginManager().callEvent(new TelepathyEvent(p, e));
    }
}
