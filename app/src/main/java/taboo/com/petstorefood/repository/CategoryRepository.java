package taboo.com.petstorefood.repository;

import taboo.com.petstorefood.ApiClient;
import taboo.com.petstorefood.service.CategoryService;

public class CategoryRepository {
    public static CategoryService getCategoryService() {
        return ApiClient.getClient().create(CategoryService.class);
    }
}
