package me.M0dii.EnhancedEnchant;

import me.M0dii.EnhancedEnchant.Commands.Enchant;
import me.M0dii.EnhancedEnchant.Commands.Unenchant;
import me.M0dii.EnhancedEnchant.Enchants.RegisterEnchants;
import me.M0dii.EnhancedEnchant.Listeners.AnvilCombine;
import me.M0dii.EnhancedEnchant.Listeners.BlockBreak;
import me.M0dii.EnhancedEnchant.Listeners.EnchantmentCombine;
import me.M0dii.EnhancedEnchant.Listeners.PlayerInteract;
import me.M0dii.EnhancedEnchant.Listeners.TillInteract;
import me.M0dii.EnhancedEnchant.Listeners.Custom.CustomCombine;
import me.M0dii.EnhancedEnchant.Listeners.Custom.OnTelepathy;
import me.M0dii.EnhancedEnchant.Listeners.Custom.OnTill;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnhancedEnchant extends JavaPlugin
{
    private PluginManager pm;
    
    public void onEnable()
    {
        RegisterEnchants.register();
        
        this.pm = this.getServer().getPluginManager();
        
        this.getLogger().info("EnhancedEnchantments has been enabled.");
        
        registerEvents();
        registerCommands();
    }
    
    public void onDisable()
    {
        this.getLogger().info("EnhancedEnchantments has been disabled.");
    }
    
    private void registerEvents()
    {
        this.pm.registerEvents(new PlayerInteract(this), this);
        this.pm.registerEvents(new OnTelepathy(), this);
        this.pm.registerEvents(new BlockBreak(), this);
        this.pm.registerEvents(new EnchantmentCombine(), this);
        this.pm.registerEvents(new CustomCombine(this), this);
        this.pm.registerEvents(new TillInteract(this), this);
        this.pm.registerEvents(new OnTill(), this);
        this.pm.registerEvents(new AnvilCombine(this), this);
    }
    
    private void registerCommands()
    {
        PluginCommand ee = getCommand("enhancedenchant");
        
        if(ee != null)
            ee.setExecutor(new Enchant());
    
        PluginCommand ue = getCommand("unenchant");
    
        if(ue != null)
            ue.setExecutor(new Unenchant());
    }
    
    public String format(String msg)
    {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}