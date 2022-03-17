import java.time.LocalDateTime;
import java.time.temporal.*;

public class DateHandler {

    public static int monthLength(int year, int month){
        LocalDateTime date = LocalDateTime.of(year, month, 1, 0, 0);
        ValueRange range = date.range(ChronoField.DAY_OF_MONTH);
        return (int) range.getMaximum();
    }

}