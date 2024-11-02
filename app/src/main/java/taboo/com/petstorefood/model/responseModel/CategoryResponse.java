package taboo.com.petstorefood.model.responseModel;

public class CategoryResponse {
    private String id;
    private String name;
    private String description;

    public CategoryResponse(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name; // Để Spinner hiển thị tên của danh mục
    }
}

