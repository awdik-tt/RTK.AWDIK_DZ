package com.example.dungeon.core;

/**
 * Класс для отслеживания информации о загрузке классов и инициализации приложения.
 * Используется для демонстрации работы ClassLoader и статической инициализации.
 */
public final class WorldInfo {
    // Лог для отслеживания инициализации и взаимодействий с классом
    private static final StringBuilder log = new StringBuilder();

    // Статический блок инициализации, выполняется при загрузке класса
    static {
        log.append("[static init WorldInfo]\n");
        ClassLoader cl = WorldInfo.class.getClassLoader();
        log.append("ClassLoader: ").append(cl).append("\n");
        if (cl != null) log.append("Parent: ").append(cl.getParent()).append("\n");
    }

    /**
     * Метод для регистрации взаимодействия с классом.
     * @param who имя субъекта, вызвавшего метод
     */
    public static void touch(String who) {
        log.append("touched by ").append(who).append("\n");
    }

    // Приватный конструктор предотвращает создание экземпляров класса
    private WorldInfo() {
    }
}
