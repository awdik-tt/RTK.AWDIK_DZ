package com.example.dungeon.model;

/**
 * Класс, представляющий монстра в игре.
 * Наследуется от Entity, добавляя уровень монстра.
 */
public class Monster extends Entity {
    // Уровень монстра, влияет на его силу и здоровье
    private int level;

    /**
     * Конструктор монстра.
     * @param name имя монстра
     * @param level уровень монстра
     * @param hp здоровье монстра
     */
    public Monster(String name, int level, int hp) {
        super(name, hp);
        this.level = level;
    }

    /**
     * Возвращает уровень монстра.
     * @return уровень монстра
     */
    public int getLevel() {
        return level;
    }

    /**
     * Устанавливает уровень монстра.
     * @param level уровень монстра
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
