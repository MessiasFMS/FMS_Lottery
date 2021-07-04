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

    private Main instance;
    public Lottery lottery;
    ConsoleCommandSender cs = Bukkit.getServer().getConsoleSender();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Plugin successfully enabled. Plugin by: MessiasFMS");
        instance = this;
        lottery = new Lottery();
        new CheckStart();
        getCommand("lottery").setExecutor(new LotteryCommand());

    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin successfully disabled. Plugin by: MessiasFMS");
    }


}