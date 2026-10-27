package am.arasce.wallpaper;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/** Classical fixed 365-day Armenian era, epoch 11 July 552 Julian = 13 July Gregorian. */
final class ArmenianCalendar {
    static final LocalDate EPOCH = LocalDate.of(552, 7, 13);
    static final String[] MONTHS = {"Նավասարդ","Հոռի","Սահմի","Տրե","Քաղոց","Արաց","Մեհեկան","Արեգ","Ահեկան","Մարերի","Մարգաց","Հրոտից","Ավելյաց"};
    static LocalDate westernEaster(int year) {
        int a = year % 19, b = year / 100, c = year % 100;
        int d = b / 4, e = b % 4, f = (b + 8) / 25, g = (b - f + 1) / 3;
        int h = (19*a + b - d - g + 15) % 30, i = c / 4, k = c % 4;
        int l = (32 + 2*e + 2*i - h - k) % 7, m = (a + 11*h + 22*l) / 451;
        int n = h + l - 7*m + 114;
        return LocalDate.of(year, n / 31, n % 31 + 1);
    }
    static String churchHoliday(LocalDate date) {
        if (date.getMonthValue() == 1 && date.getDayOfMonth() == 6) return "ՍՈՒՐԲ ԾՆՈՒՆԴ";
        if (date.getMonthValue() == 2 && date.getDayOfMonth() == 14) return "ՏՅԱՌՆԸՆԴԱՌԱՋ";
        LocalDate easter = westernEaster(date.getYear());
        if (date.equals(easter)) return "ՍՈՒՐԲ ԶԱՏԻԿ";
        if (date.equals(easter.plusDays(98))) return "ՎԱՐԴԱՎԱՌ";
        return "";
    }
    static String format(LocalDate date) {
        long days = ChronoUnit.DAYS.between(EPOCH, date);
        if (days < 0) return "";
        long year = days / 365 + 1;
        int d = (int)(days % 365);
        return String.format(java.util.Locale.ROOT, "%d %s · Հայոց %d թ.", d % 30 + 1, MONTHS[d / 30], year);
    }
}
