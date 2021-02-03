package me.M0dii.EnhancedEnchant.Listeners;

import com.gmail.nossr50.datatypes.skills.SubSkillType;
import com.gmail.nossr50.util.random.RandomChanceUtil;
import com.gmail.nossr50.util.skills.SkillActivationType;
import me.M0dii.EnhancedEnchant.Enchants.CustomEnchants;
import me.M0dii.EnhancedEnchant.EnhancedEnchant;
import me.M0dii.EnhancedEnchant.Events.TelepathyEvent;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

public class BlockBreak implements Listener
{
    private final List<String> HEADS = Arrays.asList("PLAYER_HEAD", "SKELETON_SKULL", "CREEPER_HEAD", "WITHER_SKELETON_SKULL",
            "ZOMBIE_HEAD", "CREEPER_WALL_HEAD", "PLAYER_WALL_HEAD", "DRAGON_HEAD", "DRAGON_WALL_HEAD", "ZOMBIE_WALL_HEAD",
            "SKELETON_WALL_SKULL", "WITHER_SKELETON_WALL_SKULL");
    
    private final List<String> hoes = Arrays.asList(
            "NETHERITE_HOE", "DIAMOND_HOE", "IRON_HOE",
            "GOLDEN_HOE", "STONE_HOE", "WOODEN_HOE");
    
    private EnhancedEnchant plugin;
    
    public BlockBreak(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
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
        
        this.plugin.getCoAPI().logRemoval(p.getName(), b.getLocation(), b.getType(), b.getBlockData());
    
        if(hoes.contains(hand.getType().toString()))
        {
            boolean success = RandomChanceUtil.isActivationSuccessful(
                    SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP,
                    SubSkillType.HERBALISM_GREEN_THUMB, p);
    
            boolean doubleDrops = RandomChanceUtil.isActivationSuccessful(
                    SkillActivationType.RANDOM_LINEAR_100_SCALE_WITH_CAP,
                    SubSkillType.HERBALISM_DOUBLE_DROPS, p);
    
            if(doubleDrops)
            {
                for(ItemStack drop : drops)
                {
                    p.getInventory().addItem(drop);
                }
            }
    
            if(success)
            {
                e.setDropItems(false);
                
                for(ItemStack drop : drops)
                {
                    p.getInventory().addItem(drop);
                }
        
                return;
            }
        }
        
        if(drops.isEmpty())
            return;
    
        Bukkit.getPluginManager().callEvent(new TelepathyEvent(p, e));
    }
}
