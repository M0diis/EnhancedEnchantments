package me.m0dii.enhancedenchant.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.permissions.PermissionNodes;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.*;

public class BlockInterract implements Listener
{
    private final EnhancedEnchant plugin;
    
    public BlockInterract(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    private static final List<Material> COPPER_BLOCKS = List.of(
            Material.COPPER_BLOCK, Material.EXPOSED_COPPER, Material.WEATHERED_COPPER, Material.OXIDIZED_COPPER);
    private static final List<Material> CUT_COPPER_BLOCKS = List.of(
            Material.CUT_COPPER, Material.EXPOSED_CUT_COPPER, Material.WEATHERED_CUT_COPPER, Material.OXIDIZED_CUT_COPPER);
    private static final List<Material> SLAB_COPPER_BLOCKS = List.of(
            Material.CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_SLAB);
    private static final List<Material> STAIR_COPPER_BLOCKS = List.of(
            Material.CUT_COPPER_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS);
    
    // WAX
    private static final List<Material> WAX_COPPER_BLOCKS = List.of(
            Material.WAXED_COPPER_BLOCK, Material.WAXED_EXPOSED_COPPER, Material.WAXED_WEATHERED_COPPER, Material.WAXED_OXIDIZED_COPPER);
    private static final List<Material> WAX_CUT_COPPER_BLOCKS = List.of(
            Material.WAXED_CUT_COPPER, Material.WAXED_EXPOSED_CUT_COPPER, Material.WAXED_WEATHERED_CUT_COPPER, Material.WAXED_OXIDIZED_CUT_COPPER);
    private static final List<Material> WAX_SLAB_COPPER_BLOCKS = List.of(
            Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER_SLAB, Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER_SLAB);
    private static final List<Material> WAX_STAIR_COPPER_BLOCKS = List.of(
            Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

    Map<UUID, Long> lastClick = new HashMap<>();
    
    private boolean canInteract(Location loc)
    {
        if(Bukkit.getPluginManager().getPlugin("WorldGuard") == null)
        {
            return true;
        }
        
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(loc));
        
        return set.queryValue(null, Flags.INTERACT) == StateFlag.State.ALLOW;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e)
    {
        if(!plugin.getCfg().getBoolean("enchants.oxidizing.enabled"))
            return;
        
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;
        
        if(lastClick.containsKey(e.getPlayer().getUniqueId())
                && System.currentTimeMillis() - lastClick.get(e.getPlayer().getUniqueId()) < 200)
            return;

        Player player = e.getPlayer();
        Block clicked = e.getClickedBlock();
    
        if (!player.isSneaking())
            return;
        
        if (clicked == null)
            return;
        
        ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
    
        if(hand == null)
            return;
        
        if(hand.getItemMeta() == null)
            return;
        
        if(!hand.getItemMeta().hasEnchant(CustomEnchants.OXIDIZING))
            return;
        
        if(!canInteract(clicked.getLocation()))
            return;
    
        if (!TownyAPI.getInstance().isWilderness(clicked.getLocation()))
        {
            try
            {
                TownBlock tb = TownyAPI.getInstance().getTownBlock(clicked.getLocation());
    
                if(tb != null && tb.hasTown())
                {
                    Town town = tb.getTownOrNull();
                    
                    if (town != null && !town.hasResident(player.getName()))
                    {
                        return;
                    }
                }
            }
            catch(NullPointerException ex)
            {
                ex.printStackTrace();
            }
        }
        
        List<Material> blocks = getList(clicked.getType());
        
        if (blocks == null)
            return;
        
        int index = blocks.indexOf(clicked.getType());
        
        if (index == 3)
            return;

        Material next = blocks.get(index + 1);
        
        BlockData currentData = clicked.getBlockData();
        String stringData = currentData.getAsString();
        
        int dataIndex = stringData.indexOf('[');
        
        if (dataIndex != -1)
            stringData = stringData.substring(dataIndex);
        
        clicked.setType(next, false);
        
        if (dataIndex != -1)
        {
            BlockData newData = next.createBlockData(stringData);
            clicked.setBlockData(newData, false);
        }
        
        if(plugin.getCfg().getBoolean("enchants.oxidizing.durability"))
        {
            applyDurability(player, hand);
        }
        
        lastClick.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    private final Random r = new Random();
    
    private void applyDurability(Player player, ItemStack hand)
    {
        Inventory inv = player.getInventory();
        
        Damageable dmg = (Damageable)hand.getItemMeta();
        
        if(NBTAPI.getInstance() != null && dmg != null)
        {
            NBTItem item = new NBTItem(hand);
    
            if(!item.getKeys().contains("Unbreakable"))
            {
                if(hand.getType().getMaxDurability() <= dmg.getDamage())
                {
                    inv.removeItem(hand);
            
                    return;
                }
            }
    
            if(item.getKeys().contains("Unbreakable"))
            {
                dmg.setDamage(0);
    
                hand.setItemMeta(dmg);
                
                return;
            }
        }
        
        boolean unbreakingLevel = hand.getItemMeta().getEnchants().containsKey(Enchantment.DURABILITY);
        
        int unb = 0;
        
        if(unbreakingLevel)
            unb = hand.getItemMeta().getEnchants().get(Enchantment.DURABILITY);
        
        int chance = (100)/(1 + unb);
        
        int res = r.nextInt(100 - 1) + 1;
        
        if(res < chance)
            dmg.setDamage(dmg.getDamage() + 1);
        
        hand.setItemMeta(dmg);
    }
    
    private List<Material> getList(Material material)
    {
        if (COPPER_BLOCKS.contains(material))
            return COPPER_BLOCKS;
        
        if (CUT_COPPER_BLOCKS.contains(material))
            return CUT_COPPER_BLOCKS;
        
        if (SLAB_COPPER_BLOCKS.contains(material))
            return SLAB_COPPER_BLOCKS;
        
        if (STAIR_COPPER_BLOCKS.contains(material))
            return STAIR_COPPER_BLOCKS;
        
        if (WAX_COPPER_BLOCKS.contains(material))
            return WAX_COPPER_BLOCKS;
        
        if (WAX_CUT_COPPER_BLOCKS.contains(material))
            return WAX_CUT_COPPER_BLOCKS;
        
        if (WAX_SLAB_COPPER_BLOCKS.contains(material))
            return WAX_SLAB_COPPER_BLOCKS;
        
        if (WAX_STAIR_COPPER_BLOCKS.contains(material))
            return WAX_STAIR_COPPER_BLOCKS;
        
        return null;
    }
    
}