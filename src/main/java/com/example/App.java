package com.example;

import com.example.ConfigLoader; // Предполагается, что ConfigLoader находится здесь

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class App {

    // Глобальные ID, чтобы отслеживать записи, созданные в этой транзакции, для последующего обновления и удаления
    private static int newCustomerId = 0;
    private static int newProductId = 0;
    private static int newOrderId = 0;

    private static final ConfigLoader config = new ConfigLoader();

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // 1. Подключение к БД через JDBC (DriverManager)
            connection = DriverManager.getConnection(
                    config.getUrl(),
                    config.getUsername(),
                    config.getPassword()
            );

            // 2. Начало транзакции: отключение автокоммита [1]
            connection.setAutoCommit(false);

            System.out.println("--- Запуск транзакционных CRUD-операций ---");
            System.out.println("Соединение установлено. Начинаем транзакцию.");

            // Выполнение CRUD-операций
            insertNewCustomer(connection);      // Вставка нового покупателя [1]
            insertNewProduct(connection);        // Вставка нового товара [1]
            createNewOrder(connection);          // Создание нового заказа [1]
            readLatestOrders(connection);        // Чтение последних 5 заказов с JOIN [1]
            updateProductPrice(connection);      // Обновление цены товара [1]
            deleteTestRecords(connection);       // Удаление тестовых записей [1]

            // 3. Фиксация изменений, если все операции прошли успешно
            connection.commit();
            System.out.println("\n*** Транзакция успешно завершена и зафиксирована (COMMIT) ***");

        } catch (SQLException e) {
            // 4. Откат изменений в случае ошибки
            System.err.println("\n*** ОШИБКА. Произошла SQL-ошибка. Выполнен откат (ROLLBACK) ***");
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback(); // Откат [1]
                } catch (SQLException ex) {
                    System.err.println("Ошибка при выполнении ROLLBACK: " + ex.getMessage());
                }
            }
        } finally {
            // 5. Закрытие соединения
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Соединение закрыто.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Вставка нового тестового покупателя (INSERT) [1]
     * Использует PreparedStatement для безопасности и getGeneratedKeys() для получения ID.
     */
    private static void insertNewCustomer(Connection connection) throws SQLException {
        String sql = "INSERT INTO customer (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "Тест");
            statement.setString(2, "Покупатель-" + LocalDateTime.now().getSecond());
            statement.setString(3, "+71234567890");
            statement.setString(4, "test.user" + System.currentTimeMillis() + "@java.com");

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newCustomerId = generatedKeys.getInt(1);
                        System.out.printf("\n[INSERT Customer]: Успешно создан новый покупатель с ID: %d\n", newCustomerId);
                    }
                }
            }
        }
    }

    /**
     * Вставка нового тестового товара (INSERT) [1]
     */
    private static void insertNewProduct(Connection connection) throws SQLException {
        String sql = "INSERT INTO product (description, price, quantity, category) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, "Тестовый товар для JDBC");
            statement.setBigDecimal(2, java.math.BigDecimal.valueOf(5000.00));
            statement.setInt(3, 10);
            statement.setString(4, "ТестКатегория");

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newProductId = generatedKeys.getInt(1);
                        System.out.printf("[INSERT Product]: Успешно создан новый товар с ID: %d\n", newProductId);
                    }
                }
            }
        }
    }

    /**
     * Создание нового заказа (CREATE Order) [1]
     * Использует ID, полученные на предыдущих шагах.
     */
    private static void createNewOrder(Connection connection) throws SQLException {
        if (newCustomerId == 0 || newProductId == 0) {
            System.err.println("[CREATE Order]: Невозможно создать заказ, так как не получены ID товара/покупателя.");
            return;
        }

        // status_id = 1 (Предполагаем, что 1 = 'Новый' согласно тестовым данным)
        String sql = "INSERT INTO orders (product_id, customer_id, quantity, status_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, newProductId);
            statement.setInt(2, newCustomerId);
            statement.setInt(3, 1); // Количество
            statement.setInt(4, 1); // Статус 'Новый'

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newOrderId = generatedKeys.getInt(1);
                        System.out.printf("[CREATE Order]: Успешно создан заказ с ID: %d для покупателя %d.\n", newOrderId, newCustomerId);
                    }
                }
            }
        }
    }

    /**
     * Чтение и вывод последних 5 заказов с JOIN [1]
     */
    private static void readLatestOrders(Connection connection) throws SQLException {
        System.out.println("\n[READ]: Вывод информации о последних 5 заказах (с JOIN):");

        String sql = "SELECT o.id, o.quantity, o.date_order, " +
                "c.first_name, c.last_name, p.description, os.name AS status_name " +
                "FROM orders o " +
                "JOIN customer c ON o.customer_id = c.id " +
                "JOIN product p ON o.product_id = p.id " +
                "JOIN order_status os ON o.status_id = os.id " +
                "ORDER BY o.date_order DESC LIMIT 5";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            System.out.println("-------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-5s | %-20s | %-15s | %-30s | %-10s |\n", "ID", "Покупатель", "Статус", "Товар", "Кол-во");
            System.out.println("-------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                String customerName = rs.getString("first_name") + " " + rs.getString("last_name");
                System.out.printf("| %-5d | %-20s | %-15s | %-30s | %-10d |\n",
                        rs.getInt("id"),
                        customerName,
                        rs.getString("status_name"),
                        rs.getString("description"),
                        rs.getInt("quantity")
                );
            }
            System.out.println("-------------------------------------------------------------------------------------------------------");
        }
    }

    /**
     * Обновление цены тестового товара (UPDATE) [1]
     */
    private static void updateProductPrice(Connection connection) throws SQLException {
        if (newProductId == 0) return;

        double newPrice = 5555.55;
        String sql = "UPDATE product SET price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, java.math.BigDecimal.valueOf(newPrice));
            statement.setInt(2, newProductId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.printf("\n[UPDATE]: Цена товара ID %d успешно обновлена до %.2f\n", newProductId, newPrice);
            }
        }
    }

    /**
     * Удаление тестовых записей (DELETE) [1]
     * Удаление производится в обратном порядке из-за внешних ключей (FK).
     */
    private static void deleteTestRecords(Connection connection) throws SQLException {
        System.out.println("\n[DELETE]: Удаление созданных тестовых записей...");

        // 1. Удаление заказа (зависит от товара и покупателя)
        if (newOrderId != 0) {
            String deleteOrderSql = "DELETE FROM orders WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteOrderSql)) {
                ps.setInt(1, newOrderId);
                ps.executeUpdate();
                System.out.printf("  - Удален заказ ID: %d\n", newOrderId);
            }
        }

        // 2. Удаление товара
        if (newProductId != 0) {
            String deleteProductSql = "DELETE FROM product WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteProductSql)) {
                ps.setInt(1, newProductId);
                ps.executeUpdate();
                System.out.printf("  - Удален товар ID: %d\n", newProductId);
            }
        }

        // 3. Удаление покупателя
        if (newCustomerId != 0) {
            String deleteCustomerSql = "DELETE FROM customer WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(deleteCustomerSql)) {
                ps.setInt(1, newCustomerId);
                ps.executeUpdate();
                System.out.printf("  - Удален покупатель ID: %d\n", newCustomerId);
            }
        }
        System.out.println("[DELETE]: Тестовые записи успешно удалены.");
    }
}