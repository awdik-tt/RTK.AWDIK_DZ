package com.example.dungeon.model;

import java.util.*;

/**
 * Класс, представляющий состояние игры.
 * Хранит информацию об игроке, текущей комнате, счёте и всех комнатах игрового мира.
 */
public class GameState {
    // Игрок, участвующий в игре
    private Player player;
    // Текущая комната, в которой находится игрок
    private Room current;
    // Счёт игрока
    private int score;
    // Карта всех комнат в игровом мире, где ключ - название комнаты
    private Map<String, Room> rooms = new HashMap<>();

    /**
     * Возвращает игрока.
     * @return объект Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Устанавливает игрока.
     * @param p объект Player
     */
    public void setPlayer(Player p) {
        this.player = p;
    }

    /**
     * Возвращает текущую комнату.
     * @return объект Room
     */
    public Room getCurrent() {
        return current;
    }

    /**
     * Устанавливает текущую комнату.
     * @param r объект Room
     */
    public void setCurrent(Room r) {
        this.current = r;
    }

    /**
     * Возвращает счёт игрока.
     * @return счёт
     */
    public int getScore() {
        return score;
    }

    /**
     * Добавляет очки к счёту игрока.
     * @param d количество очков для добавления
     */
    public void addScore(int d) {
        this.score += d;
    }

    /**
     * Возвращает карту всех комнат.
     * @return карта комнат
     */
    public Map<String, Room> getRooms() {
        return rooms;
    }

    /**
     * Устанавливает карту комнат.
     * @param rooms карта комнат
     */
    public void setRooms(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Добавляет комнату в карту комнат.
     * @param name название комнаты
     * @param room объект Room
     */
    public void addRoom(String name, Room room) {
        this.rooms.put(name, room);
    }

    /**
     * Возвращает комнату по её названию.
     * @param name название комнаты
     * @return объект Room или null, если комната не найдена
     */
    public Room getRoom(String name) {
        return this.rooms.get(name);
    }
}
