package it.simonericci97.github.meterpolis.meterpolis.tools;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;

/**
 * Type adapeer to serialize and deserialize timestamp with Gson
 */
@Component
public class ZoneIdTypeAdapter extends TypeAdapter<ZoneId> {
    @Override
    public void write(JsonWriter jsonWriter, ZoneId zoneId) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.endObject();
    }

    @Override
    public ZoneId read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        jsonReader.endObject();
        return ZoneId.systemDefault();
    }
}
