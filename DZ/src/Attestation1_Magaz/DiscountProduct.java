package Attestation1_Magaz;

import java.time.LocalDate;

public class DiscountProduct extends Product {
    private double discount;
    private LocalDate expirationDate;

    public DiscountProduct(String name, double cost, double discount, LocalDate expirationDate) {
        super(name, cost);
        setDiscount(discount);
        this.expirationDate = expirationDate;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Размер скидки должен быть в пределах от 0 до 100");
        }
        this.discount = discount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isDiscountActive() {
        return expirationDate == null || LocalDate.now().isBefore(expirationDate) || LocalDate.now().equals(expirationDate);
    }

    public double getDiscountedPrice() {
        if (isDiscountActive()) {
            return super.getCost() * (1 - discount / 100);
        } else {
            return super.getCost();
        }
    }

    @Override
    public double getCost() {
        if (isDiscountActive()) {
            return super.getCost() * (1 - discount / 100);
        } else {
            return super.getCost();
        }
    }

    @Override
    public String toString() {
        return "DiscountProduct{name='" + getName() + "', originalCost=" + super.getCost() + 
               ", discountedCost=" + getCost() + ", discount=" + discount + 
               "%, expirationDate=" + expirationDate + ", active=" + isDiscountActive() + '}';
    }
}
