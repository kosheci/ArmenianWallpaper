package am.arasce.wallpaper;
import java.time.LocalDate;
public class CalendarCheck {
    public static void main(String[] args) {
        LocalDate e = LocalDate.of(552, 7, 13);
        if (!ArmenianCalendar.format(e).equals("1 Նավասարդ · Հայոց 1 թ.")) throw new AssertionError("epoch");
        if (!ArmenianCalendar.format(e.plusDays(359)).equals("30 Հրոտից · Հայոց 1 թ.")) throw new AssertionError("last regular month");
        if (!ArmenianCalendar.format(e.plusDays(360)).equals("1 Ավելյաց · Հայոց 1 թ.")) throw new AssertionError("extra days");
        if (!ArmenianCalendar.format(e.plusDays(364)).equals("5 Ավելյաց · Հայոց 1 թ.")) throw new AssertionError("last day");
        if (!ArmenianCalendar.format(e.plusDays(365)).equals("1 Նավասարդ · Հայոց 2 թ.")) throw new AssertionError("new year");
        System.out.println("Armenian calendar boundary checks passed");
    }
}
