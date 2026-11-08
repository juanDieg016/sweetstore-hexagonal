package usta.sweetstore.domain.model;

import java.time.LocalDateTime;

public class InventoryMovement {

    private Long id;
    private Product product;
    private MovementType type;
    private int quantity;
    private LocalDateTime date;
    private String description;

    public InventoryMovement() {
    }

    public InventoryMovement(
            Long id,
            Product product,
            MovementType type,
            int quantity,
            LocalDateTime date,
            String description
    ) {
        this.id = id;
        this.product = product;
        this.type = type;
        this.quantity = quantity;
        this.date = date;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}