package com.paypal.bfs.test.employeeserv.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class JsonDateDeserializer extends JsonDeserializer<LocalDate> {

  private final DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("uuuu-MM-dd")
      .withResolverStyle(ResolverStyle.STRICT);


  @Override
  public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
    try {
      TextNode node = jsonParser.getCodec().readTree(jsonParser);
      String dateString = node.textValue();
      return LocalDate.parse(dateString, formatter);
    } catch (DateTimeParseException | IOException e) {
      throw new JsonParseException(jsonParser, "error parsing date",e);
    }
  }
}
