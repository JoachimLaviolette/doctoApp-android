package if26.android.doctoapp.Services;

import android.content.Context;
import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import if26.android.doctoapp.R;

public class DateTimeService {
    private Context context;
    private Resources resources;

    public DateTimeService(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
    }

    /**
     * Return a map containing the given date data
     * @param time The time
     * @param fullDay Has to be in format: "Day, Month X"
     * @return Date data as a map
     */
    public Map<String,String> GetDateTimeDataFromFullDay(String time, String fullDay) {
        Map<String,String> dateTimeData = new LinkedHashMap<>();

        String timeKey = this.resources.getString(R.string.date_service_time);
        String dayName = this.resources.getString(R.string.date_service_day_name);
        String dayNumber = this.resources.getString(R.string.date_service_day_number);
        String monthName = this.resources.getString(R.string.date_service_month_name);

        dateTimeData.put(timeKey, time);
        dateTimeData.put(dayName, this.GetDayNameFromFullDay(fullDay));
        dateTimeData.put(dayNumber, this.GetDayNumberFromFullDay(fullDay));
        dateTimeData.put(monthName, this.GetMonthNameFromFullDay(fullDay));

        return dateTimeData;
    }

    /**
     * Return a map containing date data from the given time tag
     * @param timeTag The time tag
     * @return Data data as a map
     */
    public Map<String,String> GetDateTimeDataFromTimeTag(String timeTag) {
        String timeKey = this.resources.getString(R.string.date_service_time);
        String dayName = this.resources.getString(R.string.date_service_day_name);
        String dayNumber = this.resources.getString(R.string.date_service_day_number);
        String monthName = this.resources.getString(R.string.date_service_month_name);

        Map<String,String> dateTimeData = new LinkedHashMap<>();

        dateTimeData.put(timeKey, this.GetTimeFromTimeTag(timeTag));
        dateTimeData.put(dayName, this.GetDayNameFromTimeTag(timeTag));
        dateTimeData.put(dayNumber, this.GetDayNumberFromTimeTag(timeTag));
        dateTimeData.put(monthName, this.GetMonthNameFromTimeTag(timeTag));

        return dateTimeData;
    }

    /**
     * Create a time tag for a programmatically-created text view displaying a time
     * @param time The time
     * @param fullDay Has to be in format: "Day, Month X"
     * @return
     */
    public String CreateTimeTag(String time, String fullDay) {
        String timeKey = this.resources.getString(R.string.date_service_time);
        String dayName = this.resources.getString(R.string.date_service_day_name);
        String dayNumber = this.resources.getString(R.string.date_service_day_number);
        String monthName = this.resources.getString(R.string.date_service_month_name);
        String tag = this.resources.getString(R.string.date_service_tag);
        Map<String,String> dateTimeData = this.GetDateTimeDataFromFullDay(time, fullDay);

        return tag
                .replace(this.ToBrace(timeKey), dateTimeData.get(timeKey))
                .replace(this.ToBrace(dayName), dateTimeData.get(dayName))
                .replace(this.ToBrace(dayNumber), dateTimeData.get(dayNumber))
                .replace(this.ToBrace(monthName), dateTimeData.get(monthName))
                .trim();
    }

    /**
     * Return the full day in format "Day, Month X"
     * @param dateTimeData Map containing datetime data
     * @return The full day
     */
    public String GetFullDayFromData(Map<String,String> dateTimeData) {
        String dayName = this.resources.getString(R.string.date_service_day_name);
        String dayNumber = this.resources.getString(R.string.date_service_day_number);
        String monthName = this.resources.getString(R.string.date_service_month_name);
        String fullDay = this.resources.getString(R.string.date_service_full_day).trim();

        return fullDay
                .replace(this.ToBrace(dayName), dateTimeData.get(dayName))
                .replace(this.ToBrace(dayNumber), dateTimeData.get(dayNumber))
                .replace(this.ToBrace(monthName), dateTimeData.get(monthName))
                .trim();
    }

    /**
     * Return the time in format hh:mm
     * @param dateTimeData Map containing datetime data
     * @return The time
     */
    public String GetTimeFromData(Map<String,String> dateTimeData) {
        String timeKey = this.resources.getString(R.string.date_service_time);
        
        return dateTimeData.get(timeKey).trim();
    }

    /**
     * Put braces around a key
     * @param key The key to put braces around
     * @return The braced key
     */
    private String ToBrace(String key) {
        return "{" + key + "}";
    }

    /**
     * Get the day name from the provided full day string
     * @param fullDay Has to be in format: "Day, Month X"
     * @return The day name
     */
    private String GetDayNameFromFullDay(String fullDay) {
        return fullDay.substring(0, fullDay.indexOf(",")).trim();
    }

    /**
     * Get the day number from the provided full day string
     * @param fullDay Has to be in format: "Day, Month X"
     * @return The day number
     */
    private String GetDayNumberFromFullDay(String fullDay) {
        return fullDay.substring(fullDay.lastIndexOf(" ") + 1).trim();
    }

    /**
     * Get the month name from the provided full day string
     * @param fullDay Has to be in format: "Day, Month X"
     * @return The month name
     */
    private String GetMonthNameFromFullDay(String fullDay) {
        String monthName = fullDay.substring(fullDay.indexOf(" ") + 1);

        return monthName.substring(0, monthName.indexOf(" ")).trim();
    }

    /**
     * Get the time from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The time
     */
    private String GetTimeFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [2]
                .trim();
    }

    /**
     * Get the day name from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The day name
     */
    private String GetDayNameFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [3]
                .trim();
    }

    /**
     * Get the day number from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The day number
     */
    private String GetDayNumberFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [4]
                .trim();
    }

    /**
     * Get the month name from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The month name
     */
    private String GetMonthNameFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [5]
                .trim();
    }

    /**
     * Return the current date time
     * @return The current date time
     */
    public static String GetCurrentDateTime() {
        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();

        return dateFormat.format(today);
    }

    /**
     * Return the current date time in the given format
     * @param format The format to get the date in
     * @return The current date
     */
    public static String GetCurrentDateTime(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();

        return dateFormat.format(today);
    }

    /**
     * Get the current date + the number of given days
     * @param daysToAdd The number of days to add
     * @return The new date
     */
    public static String GetDateFromCurrent(int daysToAdd) {
        String DATE_FORMAT = "EEEE, MMMM d";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar c = Calendar.getInstance();
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.add(Calendar.DAY_OF_MONTH, daysToAdd);
        String date = dateFormat.format(c.getTime());

        return date;
    }

    /**
     * Must be in format {Day, Month 7}
     * @param date The date to extract the day from
     * @return The day
     */
    public static String GetDayFromDate(String date) {
        return date.substring(0, date.indexOf(","));
    }
}
