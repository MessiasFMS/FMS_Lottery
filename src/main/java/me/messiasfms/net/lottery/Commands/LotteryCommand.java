package me.messiasfms.net.lottery.Commands;

import me.messiasfms.net.lottery.Lottery.Lottery;
import me.messiasfms.net.lottery.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class LotteryCommand implements CommandExecutor {
    
    FileConfiguration config = Main.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("lottery")){
                if (args.length == 0){
                    if (p.hasPermission("lottery.admin")){
                        for (String li : config.getStringList("Messages.CommandList")) {
                        p.sendMessage(li.replace("&", "§"));
                      }
                    } else {
                        p.sendMessage(config.getString("Messages.CorrectUseLotteryCmd").replace("&", "§"));
                    }
                }
                if (args.length == 1){
                    if (args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("iniciar")){
                        if (!p.hasPermission("lottery.admin")){
                            p.sendMessage(config.getString("Messages.NoPermission").replace("&", "§"));
                            return true;
                        }
                        if (!Main.getLottery().isOccurring()) {
                            Main.getLottery().start();
                        } else {
                            p.sendMessage(config.getString("Messages.Admin.LotteryAlreadOpen").replace("&", "§"));
                        }
                    } else if (args[0].equalsIgnoreCase("cancel") || args[0].equalsIgnoreCase("cancelar")) {
                        if (!p.hasPermission("lottery.admin")){
                            p.sendMessage(config.getString("Messages.NoPermission").replace("&", "§"));
                            return true;
                        }
                        if (Main.getLottery().isOccurring()){
                            Main.getLottery().setFinished(true);
                            Main.getLottery().setOccurring(false);
                            Main.getLottery().setLastAward(0);
                            Bukkit.broadcastMessage(config.getString("Messages.Admin.ClosedLotteryByAdmin").replace("&", "§"));
                        } else {
                            p.sendMessage(config.getString("Messages.ClosedLottery").replace("&", "§"));
                        }
                    } else if (args[0].equalsIgnoreCase("reload")) {
                        if (!p.hasPermission("lottery.admin")){
                            p.sendMessage(config.getString("Messages.NoPermission").replace("&", "§"));
                            return true;
                        }
                        Main.getInstance().reloadConfig();
                        p.sendMessage(config.getString("Messages.Admin.ReloadConfig").replace("&", "§"));

                    }
                        if (Lottery.isNumber(args[0])) {
                            if (Main.getLottery().isOccurring()) {
                            int answer = Integer.parseInt(args[0]);
                            if (Main.getLottery().isCorrectNumber(answer)) {
                                Main.getLottery().playerWin(p);
                            } else {
                                p.sendMessage(config.getString("Messages.TryAgain").replace("&", "§"));
                            }
                        } else {
                            p.sendMessage(config.getString("Messages.ClosedLottery").replace("&", "§"));
                        }
                    } else {
                            if (Main.getLottery().isOccurring()) {
                                p.sendMessage(config.getString("Messages.NoNumber").replace("&", "§"));
                            }
                    }
                }
            }
        return false;
    }
}
