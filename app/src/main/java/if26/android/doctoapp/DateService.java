package if26.android.doctoapp;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

public class DateService {
    private Context context;
    private Resources resources;

    public DateService(Context context) {
        this.context = context;
        this.resources = this.context.getResources();
    }

    /**
     * Return a map containing the given date data
     * @param time The time
     * @param fullDay Has to be in format: "Day, Month X"
     * @return Date data as a map
     */
    public Map<String,String> GetDateDataFromFullDay(String time, String fullDay) {
        Map<String,String> dateData = new HashMap<>();

        String timeKey = this.resources.getString(R.string.date_service_time);
        String dayName = this.resources.getString(R.string.date_service_day_name);
        String dayNumber = this.resources.getString(R.string.date_service_day_number);
        String monthName = this.resources.getString(R.string.date_service_month_name);

        dateData.put(timeKey, time);
        dateData.put(dayName, this.GetDayNameFromFullDay(fullDay));
        dateData.put(dayNumber, this.GetDayNumberFromFullDay(fullDay));
        dateData.put(monthName, this.GetMonthNameFromFullDay(fullDay));

        return dateData;
    }

    /**
     * Return a map containing date data from the given time tag
     * @param timeTag The time tag
     * @return Data data as a map
     */
    public Map<String,String> GetDateDataFromTimeTag(String timeTag) {
        String timeKey = this.resources.getString(R.string.date_service_time);
        String dayName = this.resources.getString(R.string.date_service_day_name);
        String dayNumber = this.resources.getString(R.string.date_service_day_number);
        String monthName = this.resources.getString(R.string.date_service_month_name);

        Map<String,String> dateData = new HashMap<>();

        dateData.put(timeKey, this.GetTimeFromTimeTag(timeTag));
        dateData.put(dayName, this.GetDayNameFromTimeTag(timeTag));
        dateData.put(dayNumber, this.GetDayNumberFromTimeTag(timeTag));
        dateData.put(monthName, this.GetMonthNameFromTimeTag(timeTag));

        return dateData;
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
        Map<String,String> dateData = this.GetDateDataFromFullDay(time, fullDay);

        return tag
                .replace(this.ToBrace(timeKey), dateData.get(timeKey))
                .replace(this.ToBrace(dayName), dateData.get(dayName))
                .replace(this.ToBrace(dayNumber), dateData.get(dayNumber))
                .replace(this.ToBrace(monthName), dateData.get(monthName));
    }

    private String ToBrace(String key) {
        return "{" + key + "}";
    }

    /**
     * Get the day name from the provided full day string
     * @param fullDay Has to be in format: "Day, Month X"
     * @return The day name
     */
    private String GetDayNameFromFullDay(String fullDay) {
        return fullDay.substring(0, fullDay.indexOf(","));
    }

    /**
     * Get the day number from the provided full day string
     * @param fullDay Has to be in format: "Day, Month X"
     * @return The day number
     */
    private String GetDayNumberFromFullDay(String fullDay) {
        return fullDay.substring(fullDay.lastIndexOf(" ") + 1);
    }

    /**
     * Get the month name from the provided full day string
     * @param fullDay Has to be in format: "Day, Month X"
     * @return The month name
     */
    private String GetMonthNameFromFullDay(String fullDay) {
        String monthName = fullDay.substring(fullDay.indexOf(" ") + 1);

        return monthName.substring(0, fullDay.indexOf(" "));
    }

    /**
     * Get the time from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The time
     */
    private String GetTimeFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [2];
    }

    /**
     * Get the day name from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The day name
     */
    private String GetDayNameFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [3];
    }

    /**
     * Get the day number from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The day number
     */
    private String GetDayNumberFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [4];
    }

    /**
     * Get the month name from the provided time tag
     * @param timeTag Has to be in format: "_tag_{time}_{dayName}_{dayNumber}_{monthName}"
     * @return The month name
     */
    private String GetMonthNameFromTimeTag(String timeTag) {
        return timeTag
                .split("_")
                [5];
    }

}
