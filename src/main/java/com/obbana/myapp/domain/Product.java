package com.obbana.myapp.domain;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category")
    private Category category;
    private String image;
    private String full_image;
    private String title;

    private String description;
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desription) {
        this.description = desription;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {

        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullImage() {
        return full_image;
    }

    public void setFullImage(String fullImage) {
        this.full_image = fullImage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product() {
    }

    public Product(Category category, String image, String fullImage, String title, String description, Integer quantity) {
        this.category = category;
        this.image = image;
        this.full_image = fullImage;
        this.title = title;
        this.description = description;
        this.quantity = quantity;
    }

}
