package Attestation1_Magaz;

/* Промежуточная аттестация Модуль 1 «Введение в разработку/Введение в Java»
Формулировка задания: Необходимо реализовать приложение, принимающее список пользователей,
продуктов и обрабатывающее покупку пользователя.*/

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> people = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        // Ввод покупателей
        System.out.println("Введите покупателей. Формат: имя деньги. Для завершения введите END");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                System.out.println("Неверный формат. Повторите ввод.");
                continue;
            }
            String name = parts[0];
            String moneyStr = parts[1];

            try {
                double money = Double.parseDouble(moneyStr);
                Person person = new Person(name, money);
                people.add(person);
            } catch (NumberFormatException e) {
                System.out.println("Некорректное значение денег. Повторите ввод.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Ввод продуктов
        System.out.println("Введите продукты. Формат: 'обычный продукт' - 'название стоимость' или 'скидочный продукт' - 'название стоимость скидка дата_окончания(гггг-мм-дд)'. Для завершения введите END");
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            
            String[] parts = line.split("\\s+");
            if (parts.length < 2) {
                System.out.println("Неверный формат. Повторите ввод.");
                continue;
            }
            
            String productName = parts[0];
            try {
                if (parts.length == 2) {
                    // Обычный продукт
                    double cost = Double.parseDouble(parts[1]);
                    Product product = new Product(productName, cost);
                    products.add(product);
                    System.out.println("Добавлен обычный продукт: " + product);
                } else if (parts.length == 4) {
                    // Скидочный продукт
                    double cost = Double.parseDouble(parts[1]);
                    double discount = Double.parseDouble(parts[2]);
                    LocalDate expirationDate = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    
                    DiscountProduct discountProduct = new DiscountProduct(productName, cost, discount, expirationDate);
                    products.add(discountProduct);
                    System.out.println("Добавлен скидочный продукт: " + discountProduct);
                } else {
                    System.out.println("Неверный формат. Повторите ввод.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Некорректное числовое значение. Повторите ввод.");
            } catch (DateTimeParseException e) {
                System.out.println("Некорректный формат даты. Используйте формат гггг-мм-дд. Повторите ввод.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        // Обработка покупок
        for (Person person : people) {
            System.out.println("\nПокупки для " + person.getName() + ":");
            boolean boughtSomething = false;
            while (true) {
                System.out.println("Введите название продукта для покупки или END для завершения:");
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("END")) {
                    break;
                }
                Product selectedProduct = null;
                for (Product p : products) {
                    if (p.getName().equalsIgnoreCase(input)) {
                        selectedProduct = p;
                        break;
                    }
                }
                if (selectedProduct == null) {
                    System.out.println("Такого продукта не найдено, попробуйте еще раз.");
                    continue;
                }
                // Попытка покупки
                if (person.buyProduct(selectedProduct)) {
                    System.out.println(person.getName() + " купил(а) " + selectedProduct.getName());
                    System.out.println("Цена: " + selectedProduct.getCost());
                    if (selectedProduct instanceof DiscountProduct) {
                        DiscountProduct dp = (DiscountProduct) selectedProduct;
                        System.out.println("Это скидочный продукт. Скидка: " + dp.getDiscount() + "%. Активна: " + dp.isDiscountActive());
                    }
                    boughtSomething = true;
                } else {
                    System.out.println(person.getName() + " не может позволить себе " + selectedProduct.getName() + " за " + selectedProduct.getCost());
                }
            }
            if (person.getPackageProducts().isEmpty()) {
                System.out.println(person.getName() + " Ничего не куплено");
            }
        }

        // Итог
        System.out.println("\nИтоги покупок:");
        for (Person person : people) {
            System.out.println(person.getName() + " купил(а):");
            if (person.getPackageProducts().isEmpty()) {
                System.out.println("  Ничего не куплено");
            } else {
                for (Product p : person.getPackageProducts()) {
                    System.out.println("  " + p.getName() + " за " + p.getCost());
                    if (p instanceof DiscountProduct) {
                        DiscountProduct dp = (DiscountProduct) p;
                        System.out.println("    (Скидочный продукт, скидка: " + dp.getDiscount() + "%, активна: " + dp.isDiscountActive() + ")");
                    }
                }
            }
            System.out.println("Оставшиеся деньги: " + person.getMoney());
        }

        scanner.close();
    }
}
