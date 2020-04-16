package net.testusuke.moneyticket;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyTicket extends JavaPlugin {

    //////////////////////////
    //  Plugin Information  //
    //////////////////////////
    //  Prefix
    public String prefix;
    //  plugin
    public String pluginName = "CommandHelper";
    //  Version
    public String version = "1.0.0";


    @Override
    public void onEnable() {
        // Plugin startup logic
        //  Logger
        getLogger().info("==============================");
        getLogger().info("Plugin: " + pluginName);
        getLogger().info("Ver: " + version + "  Author: testusuke");
        getLogger().info("==============================");
        //  config
        this.saveDefaultConfig();
        //  LoadPrefix
        loadPrefix();

        //  Command
        getCommand("mt").setExecutor(new MT_command(this));
        getCommand("moneyticket").setExecutor(new MT_command(this));
        //  Event
        getServer().getPluginManager().registerEvents(new ClickEvent(this),this);
        //  VaultManager
        new VaultManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void loadPrefix(){
        FileConfiguration config = this.getConfig();
        try {
            prefix = config.getString("prefix").replace("&", "§");
        }catch (NullPointerException e){
            e.printStackTrace();
            prefix = "§e[" + pluginName + "]";
        }
    }
}
