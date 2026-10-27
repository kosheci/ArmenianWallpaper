package am.arasce.wallpaper;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/** Classical fixed 365-day Armenian era, epoch 11 July 552 Julian = 13 July Gregorian. */
final class ArmenianCalendar {
    static final LocalDate EPOCH = LocalDate.of(552, 7, 13);
    static final String[] MONTHS = {"Նավասարդ","Հոռի","Սահմի","Տրե","Քաղոց","Արաց","Մեհեկան","Արեգ","Ահեկան","Մարերի","Մարգաց","Հրոտից","Ավելյաց"};
    static String format(LocalDate date) {
        long days = ChronoUnit.DAYS.between(EPOCH, date);
        if (days < 0) return "";
        long year = days / 365 + 1;
        int d = (int)(days % 365);
        return String.format(java.util.Locale.ROOT, "%d %s · Հայոց %d թ.", d % 30 + 1, MONTHS[d / 30], year);
    }
}
