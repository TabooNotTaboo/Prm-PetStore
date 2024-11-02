package taboo.com.petstorefood.model.entity;

import java.util.UUID;

public class Category {
        private UUID id;
        private String name;
        private String description;

        // Constructor
        public Category(UUID id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        // Getters and Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    @Override
    public String toString() {
        return name; // Trả về tên danh mục để hiển thị trong spinner
    }
}
