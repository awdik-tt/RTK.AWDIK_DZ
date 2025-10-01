package com.example.dungeon.core;

import com.example.dungeon.model.GameState;
import java.util.List;

/**
 * Функциональный интерфейс, представляющий команду в игре.
 * Определяет метод выполнения команды с переданным контекстом и аргументами.
 */
@FunctionalInterface
public interface Command { 
    /**
     * Выполняет команду с указанным контекстом и аргументами.
     * @param ctx состояние игры, в котором выполняется команда
     * @param args список аргументов команды
     */
    void execute(GameState ctx, List<String> args); 
}
