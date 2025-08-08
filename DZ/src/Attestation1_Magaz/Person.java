package Attestation1_Magaz;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    private String name;
    private double money;
    private List<Product> packageProducts;

    public Person(String name, double money) {
        setName(name);
        setMoney(money);
        this.packageProducts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов");
        }
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        if (money < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательными");
        }
        this.money = money;
    }

    public List<Product> getPackageProducts() {
        return packageProducts;
    }

    public boolean buyProduct(Product product) {
        if (product.getCost() <= this.money) {
            this.money -= product.getCost();
            packageProducts.add(product);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Person{name='").append(name).append("', money=").append(money);
        sb.append(", products=").append(packageProducts).append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Double.compare(person.money, money) == 0 &&
                name.equals(person.name) &&
                packageProducts.equals(person.packageProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, money, packageProducts);
    }
}
