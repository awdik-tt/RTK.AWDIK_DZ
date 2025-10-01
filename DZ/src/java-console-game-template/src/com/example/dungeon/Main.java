package com.example.dungeon;

import com.example.dungeon.core.Game;

/**
 * Главный класс приложения.
 * Точка входа в игру DungeonMini.
 */
public class Main {
    /**
     * Точка входа в приложение.
     * Создает и запускает игру.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        new Game().run();
    }
}
