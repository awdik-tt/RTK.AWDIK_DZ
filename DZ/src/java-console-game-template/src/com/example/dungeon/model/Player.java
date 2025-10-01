package com.example.dungeon.model;

import java.util.*;

/**
 * Класс, представляющий игрока в игре.
 * Наследуется от Entity, добавляя параметры атаки инвентарь.
 */
public class Player extends Entity {
    // Сила атаки игрока
    private int attack;
    // Инвентарь игрока - список предметов, которые он несёт
    private final List<Item> inventory = new ArrayList<>();

    /**
     * Конструктор игрока.
     * @param name имя игрока
     * @param hp здоровье игрока
     * @param attack сила атаки игрока
     */
    public Player(String name, int hp, int attack) {
        super(name, hp);
        this.attack = attack;
    }

    /**
     * Возвращает силу атаки игрока.
     * @return сила атаки
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Устанавливает силу атаки игрока.
     * @param attack сила атаки
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Возвращает инвентарь игрока.
     * @return список предметов в инвентаре
     */
    public List<Item> getInventory() {
        return inventory;
    }
}
