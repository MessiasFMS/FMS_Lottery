package me.messiasfms.net.lottery.Lottery;

import lombok.Getter;
import lombok.Setter;
import me.messiasfms.net.lottery.Main;
import me.messiasfms.net.lottery.Utils.NumberFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

@Getter @Setter
public class Lottery {

    private final Main instance = Main.getPlugin(Main.class).getInstance();
    FileConfiguration config = instance.getConfig();

    private double award, lastAward;
    private int numberC;
    private boolean occurring, finished;

    NumberFormatter format = new NumberFormatter();

    public void start(){
        this.setAward();
        this.setNumberC();
        this.setFinished(false);
        this.setOccurring(true);
        new BukkitRunnable() {

            int adverts = config.getInt("Others.Adverts");
            @Override
            public void run(){
                if (!isOccurring()){
                    cancel();
                }
                if (!isFinished()){
                    if (adverts > 0) {
                        for (final String li : config.getStringList("Messages.OpenLottery")) {
                            Bukkit.broadcastMessage(li.replace("&", "§").replace("%award", String.valueOf(format.formatNumber(award)))
                                    .replace("%adverts", String.valueOf(this.adverts)));
                        }
                        adverts--;
                    } else {
                        cancel();
                        for (final String lsv : config.getStringList("Messages.NoWinner")) {
                            Bukkit.broadcastMessage(lsv.replace("&", "§").replace("%award", String.valueOf(format.formatNumber(award))).replace("%numberc", String.valueOf(numberC)));
                        }
                        if (config.getBoolean("Others.AwardAccumulate")) {
                            setLastAward(award);
                        }
                        setFinished(true);
                        setOccurring(false);
                    }
                } else {
                    cancel();
                }

            }
        }.runTaskTimerAsynchronously(instance, 0, config.getInt("Others.SecondsAdverts")*20L);
    }

    public void playerWin(Player p) {
        this.setOccurring(false);
        this.setFinished(true);
        for (final String pg : config.getStringList("Messages.PlayerWin")) {
            Bukkit.broadcastMessage(pg.replace("&", "§").replace("%player", p.getName()).replace("%award",
                    String.valueOf(format.formatNumber(award))).replace("%numberc", String.valueOf(this.numberC)));
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                config.getString("Others.WinCommand").replace("%player",
                        p.getName()).replace("%award", String.valueOf(award)));
        p.sendMessage(config.getString("Messages.MoneyReceived")
                .replace("&", "§").replace("%award", String.valueOf(award + lastAward)));
        setLastAward(0);
        this.numberC = 0;
    }

    public void setAward() {
        this.award = getDNumber(config.getInt("Others.Award.Min"),
                config.getInt("Others.Award.Max")) + lastAward;
    }

    public void setLastAward(double lastAward) {
        this.lastAward = lastAward;
    }

    public void setNumberC() {
        this.numberC = getINumber(config.getInt("Others.Number.Min"),
                config.getInt("Others.Number.Max"));
    }

    public boolean isCorrectNumber(int numberR){
        return numberR == this.numberC;
    }

    public double getDNumber(int min, int max){
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    public int getINumber(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }

    public static boolean isNumber(final String args) {
        try {
            Integer.parseInt(args);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
