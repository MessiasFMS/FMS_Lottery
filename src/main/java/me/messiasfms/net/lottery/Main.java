package me.messiasfms.net.lottery;

import lombok.Getter;
import me.messiasfms.net.lottery.Commands.LotteryCommand;
import me.messiasfms.net.lottery.Lottery.Lottery;
import me.messiasfms.net.lottery.Utils.AutomaticStart.CheckStart;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class Main extends JavaPlugin {

    private static Main plugin;
    public Lottery lottery;
    ConsoleCommandSender cs = Bukkit.getServer().getConsoleSender();

    @Override
    public void onEnable() {
        if (!new File(this.getDataFolder(), "config.yml").exists()){
            saveDefaultConfig();
        }
        cs.sendMessage("§e[FMS_Lottery] §aPlugin successfully enabled. Plugin by: §cMessiasFMS");
        plugin = this;
        lottery = new Lottery();
        new CheckStart();
        getCommand("lottery").setExecutor(new LotteryCommand());

    }

    @Override
    public void onDisable() {
        cs.sendMessage("§e[FMS_Lottery] §aPlugin successfully disabled. Plugin by: §cMessiasFMS");
    }

    public static Main getInstance(){
        return plugin;
    }

}