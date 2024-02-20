package com.coding2themax.career.game.careergamebatchservice.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;

import com.coding2themax.career.game.careergamebatchservice.dto.CategoryDTO;
import com.coding2themax.career.game.careergamebatchservice.model.Category;
import com.coding2themax.career.game.careergamebatchservice.service.CategoryService;

public class CategoryWriter implements ItemWriter<Category> {
  private static final Logger LOG = LoggerFactory.getLogger(CategoryWriter.class);

  private CategoryService service;

  public CategoryWriter(CategoryService service) {
    this.service = service;
  }

  @Override
  public void write(@NonNull Chunk<? extends Category> categories) throws Exception {
    for (Category category : categories) {

      CategoryDTO dto = new CategoryDTO(Integer.parseInt(category.category_code()), category.category_text(),
          Integer.parseInt(category.display_level().trim()),
          category.selectable());
      LOG.info("saving category {}", dto);

      service.saveCategory(dto);
    }
  }

}
