package taboo.com.petstorefood.repository;

import taboo.com.petstorefood.ApiClient;
import taboo.com.petstorefood.service.AuthenService;

public class AuthenRepository {
    public static AuthenService getAuthenService() {
        return ApiClient.getClient().create(AuthenService.class);
    }
}
