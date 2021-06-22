package me.messiasfms.net.lottery.Utils.AutomaticStart;

import me.messiasfms.net.lottery.Main;
import org.bukkit.Bukkit;

public class Days {

    public static int getDay(String day) {
        if (day.equalsIgnoreCase("domingo")) {
            return 1;
        }
        if (day.equalsIgnoreCase("segunda")) {
            return 2;
        }
        if (day.equalsIgnoreCase("terca")) {
            return 3;
        }
        if (day.equalsIgnoreCase("quarda")) {
            return 4;
        }
        if (day.equalsIgnoreCase("quinta")) {
            return 5;
        }
        if (day.equalsIgnoreCase("sexta")) {
            return 6;
        }
        if (day.equalsIgnoreCase("sabado")) {
            return 7;
        }
        if (day.equalsIgnoreCase("todos")) {
            return 8;
        }
        return 7;
    }
}
