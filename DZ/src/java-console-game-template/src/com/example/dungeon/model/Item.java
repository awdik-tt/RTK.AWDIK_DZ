package com.example.dungeon.model;

/**
 * Абстрактный класс, представляющий предмет в игре.
 * Определяет базовые свойства и методы для всех предметов.
 */
public abstract class Item {
    // Название предмета
    private final String name;

    /**
     * Конструктор предмета.
     * @param name название предмета
     */
    protected Item(String name) {
        this.name = name;
    }

    /**
     * Возвращает название предмета.
     * @return название предмета
     */
    public String getName() {
        return name;
    }

    /**
     * Абстрактный метод применения предмета.
     * Реализуется в каждом конкретном типе предмета.
     * @param ctx состояние игры, в котором применяется предмет
     */
    public abstract void apply(GameState ctx);
}
