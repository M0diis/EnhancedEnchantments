package me.m0dii.enhancedenchant.listeners;

import me.m0dii.enhancedenchant.EnhancedEnchant;
import me.m0dii.enhancedenchant.enchants.CustomEnchants;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilCombine implements Listener
{
    private final EnhancedEnchant plugin;

    public AnvilCombine(EnhancedEnchant instance)
    {
        this.plugin = instance;
    }

    @EventHandler
    public void anvilCombine(PrepareAnvilEvent event)
    {
        ItemStack result = event.getResult();
        
        if (result == null)
            return;

        if (!result.hasItemMeta())
            return;

        if (!result.getItemMeta().hasLore())
            return;

        for (String l : result.getItemMeta().getLore())
        {
            if (l.contains("Telepathy"))
                addEnchant(result, CustomEnchants.TELEPATHY);

            if (l.contains("Plow"))
                addEnchant(result, CustomEnchants.PLOW);
            
            if(l.contains("Bonded"))
                addEnchant(result, CustomEnchants.BONDED);
            
            if(l.contains("Lava Walker"))
                addEnchant(result, CustomEnchants.LAVA_WALKER);
        }
    }
    
    private void addEnchant(ItemStack item, Enchantment enchant)
    {
        ItemMeta meta = item.getItemMeta();
        
        if(meta == null)
        {
            return;
        }
        
        if(meta.hasEnchant(enchant))
        {
            return;
        }

        meta.addEnchant(enchant, 1, true);
        item.addUnsafeEnchantment(enchant, 1);
    }
}