package com.coding2themax.career.game.careergamebatchservice.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.coding2themax.career.game.careergamebatchservice.dto.CategoryDTO;
import com.coding2themax.career.game.careergamebatchservice.model.Category;

@Service
public class CategoryWebService implements CategoryService {

  private RestClient restClient;

  public CategoryWebService(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void saveCategory(CategoryDTO category) {

    restClient.post().body(category).accept(MediaType.APPLICATION_JSON).retrieve().toEntity(Category.class);

  }

}
