package com.example.dungeon.model;

/**
 * Класс, представляющий дверь в игре.
 * Определяет состояние двери (заперта/закрыта/открыта) и возможность прохода через неё.
 */
public class Door {
    // Название двери
    private String name;
    // Состояние замка: true - заперта, false - не заперта
    private boolean locked;
    // Состояние двери: true - открыта, false - закрыта
    private boolean opened;

    /**
     * Конструктор двери.
     * По умолчанию дверь создается запертой и закрытой.
     * @param name название двери
     */
    public Door(String name) {
        this.name = name;
        this.locked = true; // Дверь изначально заперта
        this.opened = false; // Дверь изначально закрыта
    }

    /**
     * Возвращает название двери.
     * @return название двери
     */
    public String getName() {
        return name;
    }

    /**
     * Проверяет, заперта ли дверь.
     * @return true, если дверь заперта, иначе false
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Устанавливает состояние замка двери.
     * @param locked true - заперта, false - не заперта
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Проверяет, открыта ли дверь.
     * @return true, если дверь открыта, иначе false
     */
    public boolean isOpened() {
        return opened;
    }

    /**
     * Устанавливает состояние двери.
     * @param opened true - открыта, false - закрыта
     */
    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    /**
     * Проверяет, можно ли пройти через дверь.
     * @return true, если дверь открыта и не заперта, иначе false
     */
    public boolean canPass() {
        return opened && !locked;
    }
}
