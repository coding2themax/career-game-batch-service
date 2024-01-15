package com.coding2themax.career.game.careergamebatchservice.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;

public class CareerBatchConfigurationTest {

  @Test
  void testFixedLengthTokenizer() {

    // String testLine = "001Low postures 0 T23 ";
    String testLine = "001Low postures                                                                                                                                                                                             0 T23546";

    FixedLengthTokenizer tokenizer = fixedLengthTokenizer();

    FieldSet fs = tokenizer.tokenize(testLine);
    fs.getNames();
    Assertions.assertEquals("001", fs.getProperties().getProperty("category_code"));
    Assertions.assertEquals("Low postures", fs.getProperties().getProperty("category_text").trim());
    Assertions.assertEquals("0", fs.getProperties().getProperty("display_level").trim());
    Assertions.assertEquals("T", fs.getProperties().getProperty("selectable").trim());
    Assertions.assertEquals("23546", fs.getProperties().getProperty("sort_sequence").trim());

  }

  private FixedLengthTokenizer fixedLengthTokenizer() {

    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames("category_code", "category_text", "display_level",
        "selectable", "sort_sequence");
    tokenizer.setColumns(new Range(1, 3), new Range(4, 204), new Range(205, 206), new Range(207, 207),
        new Range(208, 212));
    return tokenizer;
  }
}
