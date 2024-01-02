package com.coding2themax.career.game.careergamebatchservice.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.coding2themax.career.game.careergamebatchservice.model.Category;

@Configuration
@EnableBatchProcessing
public class CareerBatchConfiguration {

  @Bean
  public FixedLengthTokenizer fixedLengthTokenizer() {

    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames("category_code", "category_text", "display_level", "selectable", "sort_sequence");
    tokenizer.setColumns(new Range(1, 2), new Range(3, 203), new Range(204, 205), new Range(205, 205),
        new Range(206, 212));

    return tokenizer;
  }

  @Bean
  public FlatFileItemReader<Category> categoryItemReader() {

    return new FlatFileItemReaderBuilder<Category>()
        .resource(new ClassPathResource("category.txt"))
        .name("cat")
        .linesToSkip(1)
        .lineTokenizer(fixedLengthTokenizer())
        .targetType(Category.class)
        .build();

  }

  @Bean
  public JdbcBatchItemWriter<Category> categoryWriter(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Category>()
        .sql(
            "INSERT INTO categorytext, displaylevel, selectable values(:category_text, :display_level, :selectable, :sort_sequence)")
        .dataSource(dataSource)
        .beanMapped()
        .build();

  }

  @Bean
  public Step categoryLoad(JobRepository jobRepository, JdbcTransactionManager jdbcTransactionManager,
      FlatFileItemReader<Category> categoryItemReader,
      JdbcBatchItemWriter<Category> categoryWriter) {

    return new StepBuilder("categoryLoad", jobRepository).<Category, Category>chunk(2, jdbcTransactionManager)
        .reader(categoryItemReader)
        .writer(categoryWriter)
        .build();
  }

  @Bean
  public Job job(JobRepository jobRepository, Step categoryLoad, JobCompletionNotificationLister listener) {

    return new JobBuilder("careerJob", jobRepository).listener(listener).start(categoryLoad).build();
  }
}
