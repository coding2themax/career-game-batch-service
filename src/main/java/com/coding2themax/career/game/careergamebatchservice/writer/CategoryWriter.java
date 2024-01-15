package com.coding2themax.career.game.careergamebatchservice.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;

import com.coding2themax.career.game.careergamebatchservice.dao.CategoryDao;
import com.coding2themax.career.game.careergamebatchservice.model.Category;

public class CategoryWriter implements ItemWriter<Category> {
  private CategoryDao categoryDao;
  private static final Logger LOG = LoggerFactory.getLogger(CategoryWriter.class);

  @Override
  public void write(@NonNull Chunk<? extends Category> categories) throws Exception {
    for (Category category : categories) {
      LOG.info("saving category {}", category);
      categoryDao.saveCategory(category);
    }
  }

  public void setCategoryDao(CategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }
}
