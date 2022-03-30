package me.m0dii.enhancedenchant.listeners.custom;

import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OnLavaWalk implements Listener
{
    private final EnhancedEnchant plugin;
    
    public OnLavaWalk(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
        
        tempBlocks = new ArrayList<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block b = e.getBlock();
        
        if(tempBlocks.contains(b))
            e.setDropItems(false);
    }
    
    @EventHandler
    public void lavaWalk(PlayerMoveEvent e)
    {
        if(!plugin.getCfg().getBoolean("enchants.lava_walker.enabled"))
            return;
        
        ItemStack boots = e.getPlayer().getInventory().getBoots();
        
        if (boots != null && boots.containsEnchantment(CustomEnchants.LAVA_WALKER))
        {
            Block b = e.getTo().clone().subtract(0.0D, 1.0D, 0.0D).getBlock();
            
            checkAndSet(b);
            Block blkN = b.getRelative(BlockFace.NORTH);
            checkAndSet(blkN);
            getNorthSouth(blkN);
            
            Block blkE = b.getRelative(BlockFace.EAST);
            checkAndSet(blkE);
            getNorthSouth(blkE);
    
            Block blkS = b.getRelative(BlockFace.SOUTH);
            checkAndSet(blkS);
            getNorthSouth(blkS);
    
            Block blkW = b.getRelative(BlockFace.WEST);
            checkAndSet(blkW);
            getNorthSouth(blkW);
        }
    }
    
    private void getNorthSouth(Block b)
    {
        checkAndSet(b.getRelative(BlockFace.NORTH));
        checkAndSet(b.getRelative(BlockFace.SOUTH));
    }
    
    public List<Block> tempBlocks;
    
    private void checkAndSet(Block b)
    {
        if (b.isLiquid() && b.getType().equals(Material.LAVA))
        {
            tempBlocks.add(b);
            
            removeBlockLater(b, b.getType());
            b.setType(Material.OBSIDIAN);
            
            b.getState().update();
        }
    }
    
    private void removeBlockLater(final Block current, final Material previous)
    {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            
            tempBlocks.remove(current);
            
            current.setType(previous);
        }, 100L);
    }
}
