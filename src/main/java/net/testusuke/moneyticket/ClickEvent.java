package net.testusuke.moneyticket;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClickEvent implements Listener {
    private final MoneyTicket plugin;
    public ClickEvent(MoneyTicket plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(player.getInventory().getItemInMainHand() == null) return;
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            ItemStack item = player.getInventory().getItemInMainHand();
            if(!checkTicket(item)){
                return;
            }
            int vault = getVault(item);
            int amount = item.getAmount();
            //  VaultManager
            VaultManager.economy.depositPlayer(player,vault);
            //  Message
            String vault_s = getVaultString(item);
            sendMessage(player,"§a" + vault_s + " 入金しました。");
            if(amount == 1){
                item.setAmount(0);
            }else {
                item.setAmount(amount-1);
            }
            plugin.getLogger().info(player.getName() + " receive " + vault_s + " by MoneyTicket");
        }
    }


    private Boolean checkTicket(ItemStack item){
        boolean b = false;
        ItemMeta meta = item.getItemMeta();
        String name;
        List<String> list;
        //  Getting
        name = meta.getDisplayName();
        list = meta.getLore();
        //  If
        if(item.getType() != Material.PAPER)return false;
        if(!name.equals("§a小切手"))return false;
        String code = list.get(list.size() -1);
        if(code.equals("§6[MT]§a§k§mTRUE"))return true;
        return b;
    }

    //  GetVault
    private int getVault(ItemStack item){
        int vault;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        String vault_s = lore.get(0);
        try{
            vault = Integer.parseInt(vault_s.replaceAll("[^0-9]",""));
        }catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }
        return vault;
    }

    private String getVaultString(ItemStack item){
        String vault;
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        vault = lore.get(0);
        return vault;
    }


    private void sendMessage(Player player,String message){
        player.sendMessage(plugin.prefix + message);
    }
}
