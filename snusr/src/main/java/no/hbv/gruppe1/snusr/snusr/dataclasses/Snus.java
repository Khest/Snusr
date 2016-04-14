package no.hbv.gruppe1.snusr.snusr.dataclasses;

/**
 * Created by Knut Johan Hesten on 2016-04-14.
 * Functions as intermediate between database and view layer. Handles text and number formatting.
 */
public final class Snus {
    private static int id;
    private static String manufacturer, line, taste1, taste2, taste3, type;
    private static Double strenght, nicotinelvl, totalrank;

    private Snus() {}

    public static void setSnus(int id, String manufacturer, String line, String taste1, String taste2, String taste3, String type,
    Double strength, Double nicotinelvl, Double totalrank) {
        Snus.id = 0;
        Snus.manufacturer = Snus.line = Snus.taste1 = Snus.taste2 = Snus.taste3 = Snus.type = "";
        Snus.strenght = Snus.nicotinelvl = Snus.totalrank = 0.0;
        Snus.id = id;
        Snus.manufacturer = manufacturer;
        Snus.line = line;
        Snus.taste1 = taste1;
        Snus.taste2 = taste2;
        Snus.taste3 = taste3;
        Snus.type = type;
        Snus.strenght = strength;
        Snus.nicotinelvl = nicotinelvl;
        Snus.totalrank = totalrank;
    }

    public static int getId() {
        return id;
    }

    public static String getManufacturer() {
        return manufacturer;
    }

    public static String getLine() {
        return line;
    }

    public static String getTaste1() {
        return taste1;
    }

    public static String getTaste2() {
        return taste2;
    }

    public static String getTaste3() {
        return taste3;
    }

    public static String getType() {
        return type;
    }

    public static Double getStrenght() {
        return strenght;
    }

    public static Double getNicotinelvl() {
        return nicotinelvl;
    }

    public static Double getTotalrank() {
        return totalrank;
    }
}
