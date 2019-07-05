/*
 * Copyright 2019 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.plugin.base.executors.notification;

import com.google.gson.*;
import com.google.gson.internal.bind.util.ISO8601Utils;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;

    public DefaultDateTypeAdapter(String datePattern) {
        this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
    }

    DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;

        this.enUsFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.localFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // These methods need to be synchronized since JDK DateFormat classes are not thread-safe
    // See issue 162
    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        synchronized (localFormat) {
            String dateFormatAsString = enUsFormat.format(src);
            return new JsonPrimitive(dateFormatAsString);
        }
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        Date date = deserializeToDate(json);
        if (typeOfT == Date.class) {
            return date;
        } else if (typeOfT == Timestamp.class) {
            return new Timestamp(date.getTime());
        } else if (typeOfT == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        } else {
            throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
        }
    }

    private Date deserializeToDate(JsonElement json) {
        synchronized (localFormat) {
            if (json.getAsString().isEmpty()) {
                return null;
            }
            try {
                return localFormat.parse(json.getAsString());
            } catch (ParseException ignored) {
            }
            try {
                return enUsFormat.parse(json.getAsString());
            } catch (ParseException ignored) {
            }
            try {
                return ISO8601Utils.parse(json.getAsString(), new ParsePosition(0));
            } catch (ParseException e) {
                throw new JsonSyntaxException(json.getAsString(), e);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DefaultDateTypeAdapter.class.getSimpleName());
        sb.append('(').append(localFormat.getClass().getSimpleName()).append(')');
        return sb.toString();
    }
}