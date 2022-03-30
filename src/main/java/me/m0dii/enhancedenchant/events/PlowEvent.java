package me.m0dii.enhancedenchant.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlowEvent extends Event implements org.bukkit.event.Cancellable
{
    private final Player player;
    private boolean isCancelled;
    private final PlayerInteractEvent event;
    
    public PlowEvent(Player p, PlayerInteractEvent e)
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
    
    public PlayerInteractEvent interactEvent()
    {
        return this.event;
    }
}