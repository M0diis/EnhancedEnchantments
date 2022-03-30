package me.m0dii.enhancedenchant.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class TelepathyEvent extends Event implements Cancellable
{
    private final Player player;
    private boolean isCancelled;
    private final BlockBreakEvent event;
    private final Block block;
    
    public TelepathyEvent(Player p, BlockBreakEvent e)
    {
        this.player = p;
        this.event = e;
        this.block = e.getBlock();
    }
    
    public Block getBlock()
    {
        return this.block;
    }
    
    public boolean isCancelled()
    {
        return this.isCancelled;
    }
    
    public void setCancelled(boolean isCancelled)
    {
        this.isCancelled = isCancelled;
    }
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    public HandlerList getHandlers()
    {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList()
    {
        return HANDLERS;
    }
    
    public Player getPlayer()
    {
        return this.player;
    }
    
    public BlockBreakEvent breakEvent()
    {
        return this.event;
    }
}