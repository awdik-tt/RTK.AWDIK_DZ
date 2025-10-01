package com.example.dungeon.model;

/**
 * Абстрактный класс, представляющий сущность в игре.
 * Используется как базовый класс для игроков и монстров.
 * Содержит общие свойства: имя и здоровье.
 */
public abstract class Entity {
    // Имя сущности
    private String name;
    // Здоровье сущности
    private int hp;

    /**
     * Конструктор сущности.
     * @param name имя сущности
     * @param hp начальное здоровье сущности
     */
    public Entity(String name, int hp) {
        this.name = name;
        this.hp = hp;
    }

    /**
     * Возвращает имя сущности.
     * @return имя сущности
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает имя сущности.
     * @param name имя сущности
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает здоровье сущности.
     * @return здоровье сущности
     */
    public int getHp() {
        return hp;
    }

    /**
     * Устанавливает здоровье сущности.
     * @param hp здоровье сущности
     */
    public void setHp(int hp) {
        this.hp = hp;
    }
}
