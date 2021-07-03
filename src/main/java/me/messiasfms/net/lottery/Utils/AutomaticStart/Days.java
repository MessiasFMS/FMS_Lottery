package me.messiasfms.net.lottery.Utils.AutomaticStart;

public class Days {

    public static Integer getDay(String day) {
        switch (day) {
            case "domingo":
                return 1;
            case "segunda":
                return 2;
            case "terca":
                return 3;
            case "quarta":
                return 4;
            case "quinta":
                return 5;
            case "sexta":
                return 6;
            case "sabado":
                return 7;
            case "todos":
                return 8;
            default:
                break;
        }
        return 7;
    }
}
