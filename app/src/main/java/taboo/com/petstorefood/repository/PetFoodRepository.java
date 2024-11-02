package taboo.com.petstorefood.repository;

import taboo.com.petstorefood.api.ApiClient;
import taboo.com.petstorefood.service.AuthenService;
import taboo.com.petstorefood.service.PetFoodService;

public class PetFoodRepository {
    public static PetFoodService getPetFoodService() {
        return ApiClient.getClient().create(PetFoodService.class);
    }
}
