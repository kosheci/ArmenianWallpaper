package am.arasce.wallpaper;
import java.time.LocalDate;
public class HolidayCheck {
    public static void main(String[] args) {
        if (!ArmenianCalendar.westernEaster(2025).equals(LocalDate.of(2025,4,20))) throw new AssertionError("Easter 2025");
        if (!ArmenianCalendar.westernEaster(2026).equals(LocalDate.of(2026,4,5))) throw new AssertionError("Easter 2026");
        if (!ArmenianCalendar.churchHoliday(LocalDate.of(2026,7,12)).equals("ՎԱՐԴԱՎԱՌ")) throw new AssertionError("Vardavar 2026");
        if (!ArmenianCalendar.churchHoliday(LocalDate.of(2026,1,6)).equals("ՍՈՒՐԲ ԾՆՈՒՆԴ")) throw new AssertionError("Christmas");
        if (!ArmenianCalendar.churchHoliday(LocalDate.of(2026,2,14)).equals("ՏՅԱՌՆԸՆԴԱՌԱՋ")) throw new AssertionError("Presentation");
        if (!ArmenianCalendar.churchHoliday(LocalDate.of(2026,1,7)).isEmpty()) throw new AssertionError("ordinary date");
        System.out.println("Holiday date checks passed");
    }
}
