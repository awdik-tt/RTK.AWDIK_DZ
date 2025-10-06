package com.example;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилита для загрузки параметров подключения к БД из файла application.properties.
 * Соответствует требованию вынести параметры подключения в отдельный файл [2].
 */
public class ConfigLoader {

    private static final String PROPERTIES_FILE = "application.properties";
    private final Properties properties;

    public ConfigLoader() {
        this.properties = loadProperties();
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        // Используем ClassLoader для доступа к файлу, расположенному в src/main/resources
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                // Если файл не найден, выбрасываем ошибку, поскольку без него приложение работать не может
                throw new RuntimeException("Файл конфигурации '" + PROPERTIES_FILE + "' не найден в classpath.");
            }
            // Загрузка свойств из потока
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Ошибка при загрузке файла конфигурации: " + ex.getMessage(), ex);
        }
        return props;
    }

    /**
     * Возвращает JDBC URL для подключения.
     */
    public String getUrl() {
        // Ключ db.url должен соответствовать ключу в файле application.properties
        return properties.getProperty("db.url");
    }

    /**
     * Возвращает имя пользователя БД.
     */
    public String getUsername() {
        // Ключ db.username должен соответствовать ключу в файле application.properties
        return properties.getProperty("db.username");
    }

    /**
     * Возвращает пароль БД.
     */
    public String getPassword() {
        // Ключ db.password должен соответствовать ключу в файле application.properties
        return properties.getProperty("db.password");
    }
}