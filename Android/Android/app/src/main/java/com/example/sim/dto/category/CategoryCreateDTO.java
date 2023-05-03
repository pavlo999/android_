package com.example.sim.dto.category;

public class CategoryCreateDTO {
      private String name;
      private String imageBase64;
      private int priority;
      private String description;
      public CategoryCreateDTO(){
      }

    public CategoryCreateDTO(String name, String imageBase64, int priority, String description) {
        this.name = name;
        this.imageBase64 = imageBase64;
        this.priority = priority;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
