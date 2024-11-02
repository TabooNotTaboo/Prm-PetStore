package taboo.com.petstorefood.repository;

import taboo.com.petstorefood.api.ApiClient;
import taboo.com.petstorefood.service.CategoryService;
import taboo.com.petstorefood.service.PetFoodService;

public class CategoryRepository {
    public static CategoryService getCategoryService() {
        return ApiClient.getClient().create(CategoryService.class);
    }
}