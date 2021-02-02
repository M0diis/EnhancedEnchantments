package me.M0dii.EnhancedEnchant.Events;

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
    
    public TelepathyEvent(Player p, BlockBreakEvent e)
    {
        this.player = p;
        this.event = e;
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