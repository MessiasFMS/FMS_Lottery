package me.messiasfms.net.lottery.Utils.AutomaticStart;

import me.messiasfms.net.lottery.Main;
import org.bukkit.plugin.Plugin;

import java.util.Calendar;

public class CheckStart {

    public static void check(){
        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously((Plugin) Main.getInstance(),(Runnable) new Runnable() {
            @Override
            public void run() {
                 Calendar cal = Calendar.getInstance();
                 String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
                 String minutes = String.valueOf(cal.get(Calendar.MINUTE));
                 int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                 for (String s : Main.getInstance().getConfig().getStringList("Schedules")){
                     int day = Days.getDay(s.split("-")[1]);
                        if (s.startsWith(hour + ":" + minutes)) {
                                 if ((today == day) && !Main.getLottery().isOccurring()) {
                                     Main.getLottery().start();
                         }
                     }
                 }
            }
        },0L, 1000L);
    }
}
