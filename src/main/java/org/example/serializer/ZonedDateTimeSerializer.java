package org.example.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeSerializer extends StdSerializer<ZonedDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss.SSS Z");

    public ZonedDateTimeSerializer() {
        this(null);
    }
    public ZonedDateTimeSerializer(Class<ZonedDateTime> zonedDateTime) {
        super(zonedDateTime);
    }

    @Override
    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(formatter.format(zonedDateTime));
    }
}
