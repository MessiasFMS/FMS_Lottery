package me.messiasfms.net.lottery.Utils.AutomaticStart;

import me.messiasfms.net.lottery.Lottery.Lottery;
import me.messiasfms.net.lottery.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.Calendar;

public class CheckStart {

    FileConfiguration config = Main.getInstance().getConfig();
    Lottery lottery = Main.getInstance().getLottery();

    public CheckStart(){
        check();
    }

    private void check(){
        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
             Calendar cal = Calendar.getInstance();
             String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
             String minutes = String.valueOf(cal.get(Calendar.MINUTE));
             int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
             for (String s : config.getStringList("Schedules")){
                 int day = Days.getDay(s.split("-")[1]);
                    if (s.startsWith(hour + ":" + minutes)) {
                             if ((today == day) && !lottery.isOccurring()) {
                                 lottery.start();
                     }
                 }
             }
        },0L, 1000L);
    }
}
