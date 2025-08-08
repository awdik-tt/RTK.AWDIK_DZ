package Attestation1_Magaz;

import java.util.Objects;

public class Product {
    private String name;
    private double cost;

    public Product(String name, double cost) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть отрицательной");
        }
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Product{name='" + name + "', cost=" + cost + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.cost, cost) == 0 &&
                name.equals(product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cost);
    }
}
