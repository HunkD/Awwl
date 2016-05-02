package com.hunk.nobank.contract.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class DateAdapter extends TypeAdapter<Date> {

    public static DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    @Override
    public void write(JsonWriter jsonWriter, final Date date) throws IOException {
        if (date != null) {
            jsonWriter.value(dateFormat.format(date));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            String jsonStr = jsonReader.nextString();
            try {
                if (jsonStr.length() != 0) {
                    return dateFormat.parse(jsonStr);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
