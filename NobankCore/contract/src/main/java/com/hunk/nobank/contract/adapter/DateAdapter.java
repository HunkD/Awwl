package com.hunk.nobank.contract.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(src));
    }

    @Override
    public Date deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String jsonStr = json.getAsString();
        try {
            if (jsonStr.length() != 0) {
                return dateFormat.parse(jsonStr);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDateToString(Date date) {
        return dateFormat.format(date);
    }
}
