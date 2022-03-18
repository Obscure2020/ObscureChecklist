import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.*;
import java.util.Locale;

public class DateHandler {

    public static int monthLength(int year, int month){
        LocalDateTime date = LocalDateTime.of(year, month, 1, 0, 0);
        ValueRange range = date.range(ChronoField.DAY_OF_MONTH);
        return (int) range.getMaximum();
    }

    public static String weekInfo(LocalDateTime date){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime today = now.truncatedTo(ChronoUnit.DAYS);
        if(date.isBefore(today)){
            LocalDateTime yesterday = today.minusDays(1);
            if(date.isBefore(yesterday)){
                LocalDateTime pastWeek = today.minusDays(6);
                if(date.isBefore(pastWeek)){
                    LocalDateTime lastSunday = today.minusWeeks(1);
                    int dayOfWeek = lastSunday.getDayOfWeek().getValue();
                    if(dayOfWeek != 7) lastSunday = lastSunday.minusDays(dayOfWeek);
                    if(date.isBefore(lastSunday)) return null;
                    return "Last ";
                }
                return "This past ";
            }
            return "Yesterday, ";
        }
        LocalDateTime tomorrow = today.plusDays(1);
        if(date.isBefore(tomorrow)) return "Today, ";
        LocalDateTime coming = today.plusDays(2);
        if(date.isBefore(coming)) return "Tomorrow, ";
        LocalDateTime nextWeek = today.plusWeeks(1);
        if(date.isBefore(nextWeek)) return "This coming ";
        LocalDateTime nextnextSunday = today.plusWeeks(1);
        int dayOfWeek = nextnextSunday.getDayOfWeek().getValue();
        if(dayOfWeek == 7){
            nextnextSunday = nextnextSunday.plusWeeks(1);
        } else {
            nextnextSunday = nextnextSunday.plusDays(7-dayOfWeek);
        }
        if(date.isBefore(nextnextSunday)) return "Next ";
        return null;
    }

    //Copied from StackOverFlow. https://stackoverflow.com/a/4011116
    private static String[] suffixes = {"0th",  "1st",  "2nd",  "3rd",  "4th",  "5th",  "6th",  "7th",  "8th",  "9th",
                                "10th", "11th", "12th", "13th", "14th", "15th", "16th", "17th", "18th", "19th",
                                "20th", "21st", "22nd", "23rd", "24th", "25th", "26th", "27th", "28th", "29th",
                                "30th", "31st"};

    public static String englishDay(int month, int day){
        String realMonth = Month.of(month).getDisplayName(TextStyle.FULL, Locale.US);
        return suffixes[day] + " of " + realMonth;
    }

}