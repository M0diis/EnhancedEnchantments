package me.M0dii.EnhancedEnchant.Listeners;

import com.gmail.nossr50.events.skills.McMMOPlayerSkillEvent;
import com.gmail.nossr50.events.skills.SkillActivationPerkEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityEvent;
import com.gmail.nossr50.events.skills.secondaryabilities.SubSkillEvent;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;
import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Events.PlowEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TillInteract implements Listener
{
    private final EnhancedEnchant plugin;
    
    public TillInteract(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onTill(PlayerInteractEvent e)
    {
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
        Player p = e.getPlayer();
    
        if(hand == null)
            return;
    
        if(hand.getItemMeta() == null)
            return;
        
        if(!hand.getItemMeta().hasEnchant(CustomEnchants.PLOW))
            return;

        if((p.getGameMode() == GameMode.CREATIVE)
        || (p.getGameMode() == GameMode.SPECTATOR))
            return;

        if(e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        if(e.isCancelled())
            return;
        
        Bukkit.getPluginManager().callEvent(new PlowEvent(p, e));
    }
}
