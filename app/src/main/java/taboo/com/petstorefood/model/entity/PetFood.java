package taboo.com.petstorefood.model.entity;

import android.net.Uri;

import java.util.UUID;

import retrofit2.http.Url;

public class PetFood {
        private UUID id;
        private String name;
        private String description;
        private int price;
        private Uri image;
        private int quantity;
        private String brand;
        private Category category;

    public PetFood() {
    }

    // Constructor
        public PetFood(UUID id, String name, String description, int price, Uri image, int quantity, String brand, Category category) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.image = image;
            this.quantity = quantity;
            this.brand = brand;
            this.category = category;
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

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public Uri getImage() {
            return image;
        }

        public void setImage(Uri image) {
            this.image = image;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

