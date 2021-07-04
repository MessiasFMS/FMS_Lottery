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

    private final Main instance = Main.getPlugin(Main.class).getInstance();
    private final Lottery lottery = instance.getLottery();
    FileConfiguration config = instance.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            if (p.hasPermission("lottery.admin")) {
                for (String li : config.getStringList("Messages.CommandList")) {
                    p.sendMessage(li.replace("&", "§"));
                }
                return true;
            }
            p.sendMessage(config.getString("Messages.CorrectUseLotteryCmd").replace("&", "§"));
        } else {
            if (!Lottery.isNumber(args[0])) {
                switch (args[0]) {
                    case "start":
                    case "iniciar":
                        if (!p.hasPermission("lottery.admin")) {
                            p.sendMessage(config.getString("Messages.NoPermission").replace("&", "§"));
                            break;
                        }
                        if (lottery.isOccurring()) {
                            p.sendMessage(config.getString("Messages.Admin.LotteryAlreadOpen").replace("&", "§"));
                            break;
                        }
                        lottery.start();
                        break;
                    case "cancel":
                    case "cancelar":
                        if (!p.hasPermission("lottery.admin")) {
                            p.sendMessage(config.getString("Messages.NoPermission").replace("&", "§"));
                            break;
                        }
                        if (!lottery.isOccurring()) {
                            p.sendMessage(config.getString("Messages.ClosedLottery").replace("&", "§"));
                            break;
                        }
                        lottery.setFinished(true);
                        lottery.setOccurring(false);
                        lottery.setLastAward(0);
                        Bukkit.broadcastMessage(config.getString("Messages.Admin.ClosedLotteryByAdmin").replace("&", "§"));
                        break;
                    case "reload":
                        if (!p.hasPermission("lottery.admin")) {
                            p.sendMessage(config.getString("Messages.NoPermission").replace("&", "§"));
                            return true;
                        }
                        instance.reloadConfig();
                        p.sendMessage(config.getString("Messages.Admin.ReloadConfig").replace("&", "§"));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + args[0]);
                }
                return true;
            } else if (lottery.isOccurring()) {
                p.sendMessage(config.getString("Messages.NoNumber").replace("&", "§"));
                return true;
            }
            if (!lottery.isOccurring()) {
                p.sendMessage(config.getString("Messages.ClosedLottery").replace("&", "§"));
                return true;
            }
            int answer = Integer.parseInt(args[0]);
            if (!lottery.isCorrectNumber(answer)) {
                p.sendMessage(config.getString("Messages.TryAgain").replace("&", "§"));
                return true;
            }
            lottery.playerWin(p);
        }
        return true;
    }
}
