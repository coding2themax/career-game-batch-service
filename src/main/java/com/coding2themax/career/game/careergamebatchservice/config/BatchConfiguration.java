package com.coding2themax.career.game.careergamebatchservice.config;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.web.client.RestClient;

import com.coding2themax.career.game.careergamebatchservice.dao.WebCategoryDao;
import com.coding2themax.career.game.careergamebatchservice.model.Category;
import com.coding2themax.career.game.careergamebatchservice.writer.CategoryWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

  @Value("${data.service.base.url:localhost:12000}")
  private String baseDataService;

  @Bean
  public FixedLengthTokenizer fixedLengthTokenizer() {

    FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
    tokenizer.setNames("category_code", "category_text", "display_level", "selectable", "sort_sequence");
    tokenizer.setColumns(new Range(1, 2), new Range(3, 203), new Range(204, 205), new Range(205, 205),
        new Range(206, 212));

    return tokenizer;
  }

  @Bean
  public FlatFileItemReader<Category> categoryItemReader(
      @Value("#{jobParameters[inputFile]}:/category.txt") Resource resource) {

    return new FlatFileItemReaderBuilder<Category>().resource(resource).lineTokenizer(fixedLengthTokenizer())
        .linesToSkip(1)
        .targetType(Category.class).build();

  }

  @Bean
  public RestClient restClient() {
    return RestClient.builder().baseUrl(baseDataService).build();
  }

  @Bean
  public CategoryWriter categoryWriter(RestClient restClient) {
    CategoryWriter categoryWriter = new CategoryWriter();
    WebCategoryDao categoryDao = new WebCategoryDao();
    categoryDao.setRestClient(restClient);
    categoryWriter.setCategoryDao(categoryDao);
    return categoryWriter;

  }

  @Bean
  public Step categoryLoad(JobRepository jobRepository, JdbcTransactionManager jdbcTransactionManager,
      FlatFileItemReader<Category> categoryItemReader,
      CategoryWriter categoryWriter) {

    return new StepBuilder("categoryLoad", jobRepository).<Category, Category>chunk(2, jdbcTransactionManager)
        .reader(categoryItemReader)
        .writer(categoryWriter)
        .build();
  }

}
