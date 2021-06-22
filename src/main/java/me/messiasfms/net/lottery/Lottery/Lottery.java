package me.messiasfms.net.lottery.Lottery;

import me.messiasfms.net.lottery.Main;
import me.messiasfms.net.lottery.Utils.NumberFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Lottery {

    FileConfiguration config = Main.getInstance().getConfig();

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
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, config.getInt("Others.SecondsAdverts")*20L);
    }

    public void playerWin(Player p) {
        this.setOccurring(false);
        this.setFinished(true);
        for (final String pg : config.getStringList("Messages.PlayerWin")) {
            Bukkit.broadcastMessage(pg.replace("&", "§").replace("%player", p.getName()).replace("%award",
                    String.valueOf(format.formatNumber(award))).replace("%numberc", String.valueOf(this.numberC)));
        }
        Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
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

    public void setAward(double award) {
        this.award = award;
    }

    public void setNumberC(int numberC) {
        this.numberC = numberC;
    }

    public void setOccurring(boolean occurring) {
        this.occurring = occurring;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public double getAward() {
        return award;
    }

    public double getLastAward() {
        return lastAward;
    }

    public int getNumberC() {
        return numberC;
    }

    public boolean isOccurring() {
        return occurring;
    }

    public boolean isFinished() {
        return finished;
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
