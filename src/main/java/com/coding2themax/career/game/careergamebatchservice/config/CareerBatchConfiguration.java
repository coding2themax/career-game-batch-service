package com.coding2themax.career.game.careergamebatchservice.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClient;

import com.coding2themax.career.game.careergamebatchservice.model.Additive;
import com.coding2themax.career.game.careergamebatchservice.model.Category;
import com.coding2themax.career.game.careergamebatchservice.service.CategoryService;
import com.coding2themax.career.game.careergamebatchservice.writer.CategoryWriter;

@Configuration
public class CareerBatchConfiguration {

  @Value("${data.service.base.url:localhost:12000/category}")
  private String baseDataService;

  @Bean(name = "categoryLengthTokenizer")
  public FixedLengthTokenizer categoryLengthTokenizer() {

    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames("category_code", "category_text", "display_level", "selectable", "sort_sequence");
    tokenizer.setColumns(new Range(1, 3), new Range(4, 204), new Range(205, 206), new Range(207, 207),
        new Range(208, 212));
    return tokenizer;
  }

  @Bean(name = "additiveLengthTokenizer")
  public FixedLengthTokenizer additiveLengthTokenizer() {
    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames("additive_code", "additive_text", "display_level", "selectable", "sort_sequence");
    tokenizer.setColumns(new Range(1, 3), new Range(4, 204), new Range(205, 206), new Range(207, 207),
        new Range(208, 212));
    return tokenizer;
  }

  @Bean
  public FlatFileItemReader<Additive> additiveReader(@NonNull FixedLengthTokenizer additiveLengthTokenizer) {
    return new FlatFileItemReaderBuilder<Additive>().resource(new ClassPathResource("additive.dat"))
        .name("additiveItemReader")
        .lineTokenizer(additiveLengthTokenizer)
        .linesToSkip(1)
        .targetType(Additive.class).build();
  }

  @Bean
  public FlatFileItemReader<Category> categoryItemReader(@NonNull FixedLengthTokenizer categoryLengthTokenizer) {

    return new FlatFileItemReaderBuilder<Category>().resource(new ClassPathResource("category.dat"))
        .name("categoryItemReader")
        .lineTokenizer(categoryLengthTokenizer)
        .linesToSkip(1)
        .targetType(Category.class).build();

  }

  @Bean
  public RestClient restClient() {
    return RestClient.builder().baseUrl(baseDataService).build();
  }

  @Bean
  public CategoryWriter categoryWriter(RestClient restClient, CategoryService service) {
    CategoryWriter categoryWriter = new CategoryWriter(service);
    return categoryWriter;

  }

  @Bean
  public Job importCategoryJob(JobRepository jobRepository, Step step1, JobCompletionNotificationLister listener) {
    return new JobBuilder("categoryJob", jobRepository)
        .listener(listener)
        .start(step1)
        .build();
  }

  @Bean
  public Step step1(JobRepository jobRepository, JdbcTransactionManager jdbcTransactionManager,
      FlatFileItemReader<Category> categoryItemReader,
      CategoryWriter categoryWriter) {

    return new StepBuilder("step1", jobRepository).<Category, Category>chunk(2, jdbcTransactionManager)
        .reader(categoryItemReader)
        .writer(categoryWriter)
        .allowStartIfComplete(true)
        .build();
  }

}
