package net.testusuke.moneyticket;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MT_command implements CommandExecutor {
    private final MoneyTicket plugin;

    public MT_command(MoneyTicket plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("you can not use console!");
            return false;
        }

        if(args.length == 0){
            Player player = (Player)sender;
            sendHelp(player);
            return true;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                Player player = (Player)sender;
                if(!player.isOp())return false;
                //  Config
                plugin.reloadConfig();
                plugin.loadPrefix();
                //  Message
                sendMessage(player,"§aコンフィグを再読み込みしました");
                return true;
            }

            //  MoneyTicket
            Player player = (Player)sender;
            if(!player.hasPermission("moneyticket.create")){
                sendMessage(player,"§cあなたには権限がありません");
                return false;
            }
            int money;
            try{
                money = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                sendMessage(player,"§c無効な操作です");
                return false;
            }
            if(money <= 0){
                sendMessage(player,"§c無効な操作です");
                return false;
            }
            int ownerMoney = (int) VaultManager.economy.getBalance(player);
            if(ownerMoney < money){
                sendMessage(player,"§c所持金が不足しています。");
                return false;
            }
            //  Vault
            VaultManager.economy.withdrawPlayer(player,money);
            //  CreateMoneyTicket
            ItemStack item = createTicket(player,money);
            //  SendMessage
            sendMessage(player,"§e小切手を発行します。");
            //  AddItem
            player.getInventory().addItem(item);

            return true;
        }
        if(args.length == 2){
            Player player = (Player)sender;
            if(!player.hasPermission("moneyticket.create")){
                sendMessage(player,"§cあなたには権限がありません");
                return false;
            }
            int money;
            String memo = args[1];
            try{
                money = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                sendMessage(player,"§c無効な操作です");
                return false;
            }
            if(money <= 0){
                sendMessage(player,"§c無効な操作です");
                return false;
            }
            int ownerMoney = (int) VaultManager.economy.getBalance(player);
            if(ownerMoney < money){
                sendMessage(player,"§c所持金が不足しています。");
                return false;
            }
            //  Vault
            VaultManager.economy.withdrawPlayer(player,money);
            //  CreateMoneyTicket
            ItemStack item = createTicket(player,money,memo);
            //  SendMessage
            sendMessage(player,"§e小切手を発行します。");
            //  AddItem
            player.getInventory().addItem(item);

            return true;
        }
        return false;
    }


    private ItemStack createTicket(Player player,int vault){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        //  Information
        String name = "§a小切手";
        ArrayList<String> list = new ArrayList<>();
        String price = String.format("%,d", vault);
        list.add(price);
        list.add("§b発行者: " + player.getName());
        //  偽造防止
        list.add("§6[MT]§a§k§mTRUE");
        //  Set
        meta.setDisplayName(name);
        meta.setLore(list);
        item.setItemMeta(meta);
        //  Logger
        plugin.getLogger().info(player.getName() + " created MoneyTicket. Vault: "  + vault);
        return item;
    }
    //  メモあり
    private ItemStack createTicket(Player player,int vault,String memo){
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        //  Information
        String name = "§a小切手";
        ArrayList<String> list = new ArrayList<>();
        String price = String.format("%,d", vault);
        list.add(price);
        list.add("§b発行者: " + player.getName());
        list.add("§bメモ: " + memo);
        //  偽造防止
        list.add("§6[MT]§a§k§mTRUE");
        //  Set
        meta.setDisplayName(name);
        meta.setLore(list);
        item.setItemMeta(meta);
        //  Logger
        plugin.getLogger().info(player.getName() + " created MoneyTicket. Vault: "  + vault);
        return item;
    }

    private void sendHelp(Player player){
        sendMessageNo(player,"§6============================");
        sendMessageNo(player,"§e/mt </moneyticket> <- ヘルプの表示");
        sendMessageNo(player,"§e/mt <Vault> <- 小切手を発行します");
        sendMessageNo(player,"§e/mt <Vault> <Memo> <- メモありの小切手を発行します");
        sendMessageNo(player,"§e/mt reload <- コンフィグを再読み込みします。");
        sendMessageNo(player,"§e小切手を右クリックすることで換金できます。");
        sendMessageNo(player,"§cIf you want to create MoneyTicket,you need permission'moneyticket.create'");
        sendMessageNo(player,"§d§lCreated by testusuke");
        sendMessageNo(player,"§6============================");
    }

    private void sendMessage(Player player,String message){
        player.sendMessage(plugin.prefix + message);
    }

    private void sendMessageNo(Player player,String message){
        player.sendMessage(message);
    }
}
