package me.m0dii.enhancedenchant.listeners;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.plugin.NBTAPI;
import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
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

import java.util.List;
import java.util.Random;

public class BlockInterract implements Listener
{
    private final EnhancedEnchant plugin;
    
    public BlockInterract(EnhancedEnchant plugin)
    {
        this.plugin = plugin;
    }
    
    private static final List<Material> COPPER_BLOCKS = List.of(Material.COPPER_BLOCK, Material.EXPOSED_COPPER, Material.WEATHERED_COPPER, Material.OXIDIZED_COPPER);
    private static final List<Material> CUT_COPPER_BLOCKS = List.of(Material.CUT_COPPER, Material.EXPOSED_CUT_COPPER, Material.WEATHERED_CUT_COPPER, Material.OXIDIZED_CUT_COPPER);
    private static final List<Material> SLAB_COPPER_BLOCKS = List.of(Material.CUT_COPPER_SLAB, Material.EXPOSED_CUT_COPPER_SLAB, Material.WEATHERED_CUT_COPPER_SLAB, Material.OXIDIZED_CUT_COPPER_SLAB);
    private static final List<Material> STAIR_COPPER_BLOCKS = List.of(Material.CUT_COPPER_STAIRS, Material.EXPOSED_CUT_COPPER_STAIRS, Material.WEATHERED_CUT_COPPER_STAIRS, Material.OXIDIZED_CUT_COPPER_STAIRS);
    
    // WAX
    private static final List<Material> WAX_COPPER_BLOCKS = List.of(Material.WAXED_COPPER_BLOCK, Material.WAXED_EXPOSED_COPPER, Material.WAXED_WEATHERED_COPPER, Material.WAXED_OXIDIZED_COPPER);
    private static final List<Material> WAX_CUT_COPPER_BLOCKS = List.of(Material.WAXED_CUT_COPPER, Material.WAXED_EXPOSED_CUT_COPPER, Material.WAXED_WEATHERED_CUT_COPPER, Material.WAXED_OXIDIZED_CUT_COPPER);
    private static final List<Material> WAX_SLAB_COPPER_BLOCKS = List.of(Material.WAXED_CUT_COPPER_SLAB, Material.WAXED_EXPOSED_CUT_COPPER_SLAB, Material.WAXED_WEATHERED_CUT_COPPER_SLAB, Material.WAXED_OXIDIZED_CUT_COPPER_SLAB);
    private static final List<Material> WAX_STAIR_COPPER_BLOCKS = List.of(Material.WAXED_CUT_COPPER_STAIRS, Material.WAXED_EXPOSED_CUT_COPPER_STAIRS, Material.WAXED_WEATHERED_CUT_COPPER_STAIRS, Material.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e)
    {
        if(!plugin.getCfg().getBoolean("enchants.oxidizing.enabled"))
            return;
        
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
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