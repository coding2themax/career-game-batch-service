package com.coding2themax.career.game.careergamebatchservice.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Bean
  public FixedLengthTokenizer fixedLengthTokenizer() {

    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames("category_code", "category_text", "display_level", "selectable", "sort_sequence");
    tokenizer.setColumns(new Range(1, 2), new Range(3, 203), new Range(204, 205), new Range(205, 205),
        new Range(206, 212));

    return tokenizer;
  }
}
