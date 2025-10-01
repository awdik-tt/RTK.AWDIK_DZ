package com.example.dungeon.core;

/**
 * Класс исключения, которое выбрасывается при вводе некорректной команды.
 * Наследуется от RuntimeException для упрощения обработки в игровом цикле.
 */
public class InvalidCommandException extends RuntimeException {
    /**
     * Конструктор исключения.
     * @param m сообщение об ошибке
     */
    public InvalidCommandException(String m) {
        super(m);
    }
}
