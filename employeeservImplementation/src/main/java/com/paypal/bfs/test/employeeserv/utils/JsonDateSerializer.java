package com.paypal.bfs.test.employeeserv.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class JsonDateSerializer extends JsonSerializer<LocalDate> {

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd")
      .withResolverStyle(ResolverStyle.STRICT);

  @Override
  public void serialize(LocalDate date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(date.format(this.formatter));
  }
}
