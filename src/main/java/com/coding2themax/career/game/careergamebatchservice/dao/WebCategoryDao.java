package com.coding2themax.career.game.careergamebatchservice.dao;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.coding2themax.career.game.careergamebatchservice.model.Category;

public class WebCategoryDao implements CategoryDao {

  private RestClient restClient;

  public void setRestClient(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void saveCategory(Category category) {

    restClient.post().body(category).accept(MediaType.APPLICATION_JSON).retrieve().toEntity(Category.class);

  }

}
