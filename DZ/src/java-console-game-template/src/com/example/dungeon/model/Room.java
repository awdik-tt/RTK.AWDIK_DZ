package com.example.dungeon.model;

import java.util.*;

/**
 * Класс, представляющий комнату в игровом мире.
 * Содержит информацию о названии, описании, соседних комнатах, предметах, монстрах и дверях.
 */
public class Room {
    // Название комнаты
    private final String name;
    // Описание комнаты, которое видит игрок
    private final String description;
    // Карта соседних комнат, где ключ - направление (например, "north", "south")
    private final Map<String, Room> neighbors = new HashMap<>();
    // Список предметов, находящихся в комнате
    private final List<Item> items = new ArrayList<>();
    // Монстр в комнате (может быть null, если монстра нет)
    private Monster monster;
    // Дверь в комнате (может быть null, если двери нет)
    private Door door; // Добавляем дверь в комнату

    /**
     * Конструктор комнаты.
     * @param name название комнаты
     * @param description описание комнаты
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Возвращает название комнаты.
     * @return название комнаты
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание комнаты.
     * @return описание комнаты
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает карту соседних комнат.
     * @return карта соседних комнат
     */
    public Map<String, Room> getNeighbors() {
        return neighbors;
    }

    /**
     * Возвращает список предметов в комнате.
     * @return список предметов
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * Возвращает монстра в комнате.
     * @return объект Monster или null, если монстра нет
     */
    public Monster getMonster() {
        return monster;
    }

    /**
     * Устанавливает монстра в комнате.
     * @param m объект Monster
     */
    public void setMonster(Monster m) {
        this.monster = m;
    }

    /**
     * Возвращает дверь в комнате.
     * @return объект Door или null, если двери нет
     */
    public Door getDoor() {
        return door;
    }

    /**
     * Устанавливает дверь в комнате.
     * @param door объект Door
     */
    public void setDoor(Door door) {
        this.door = door;
    }

    /**
     * Возвращает полное описание комнаты, включая предметы, монстров, двери и выходы.
     * @return строка с описанием комнаты
     */
    public String describe() {
        StringBuilder sb = new StringBuilder(name + ": " + description);
        if (!items.isEmpty()) {
            sb.append("\nПредметы: ").append(String.join(", ", items.stream().map(Item::getName).toList()));
        }
        if (monster != null) {
            sb.append("\nВ комнате монстр: ").append(monster.getName()).append(" (ур. ").append(monster.getLevel()).append(")");
        }
        if (door != null) {
            if (door.isLocked()) {
                sb.append("\nДверь заперта: ").append(door.getName());
            } else if (!door.isOpened()) {
                sb.append("\nДверь закрыта: ").append(door.getName());
            } else {
                sb.append("\nДверь открыта: ").append(door.getName());
            }
        }
        if (!neighbors.isEmpty()) {
            sb.append("\nВыходы: ").append(String.join(", ", neighbors.keySet()));
        }
        return sb.toString();
    }
}
