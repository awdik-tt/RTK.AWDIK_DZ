package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за сохранение и загрузку состояния игры.
 * Также управляет таблицей лидеров.
 */
public class SaveLoad {
    // Путь к файлу сохранения игры
    private static final Path SAVE = Paths.get("save.txt");
    // Путь к файлу таблицы лидеров
    private static final Path SCORES = Paths.get("scores.csv");

    /**
     * Сохраняет состояние игры в файл.
     * @param s состояние игры для сохранения
     */
    public static void save(GameState s) {
        try (BufferedWriter w = Files.newBufferedWriter(SAVE)) {
            Player p = s.getPlayer();
            // Сохраняем информацию об игроке
            w.write("player;" + p.getName() + ";" + p.getHp() + ";" + p.getAttack());
            w.newLine();
            // Сохраняем инвентарь игрока
            String inv = p.getInventory().stream().map(i -> i.getClass().getSimpleName() + ":" + i.getName()).collect(Collectors.joining(","));
            w.write("inventory;" + inv);
            w.newLine();
            // Сохраняем текущую комнату
            w.write("room;" + s.getCurrent().getName());
            w.newLine();
            
            // Сохраняем информацию о комнатах
            w.write("rooms_count;" + s.getRooms().size());
            w.newLine();
            
            // Сохраняем все комнаты
            for (Map.Entry<String, Room> entry : s.getRooms().entrySet()) {
                Room room = entry.getValue();
                w.write("room_name;" + room.getName());
                w.newLine();
                w.write("room_description;" + room.getDescription());
                w.newLine();
                
                // Сохраняем соседей
                w.write("neighbors;" + String.join(",", room.getNeighbors().keySet()));
                w.newLine();
                
                // Сохраняем предметы в комнате
                List<String> items = new ArrayList<>();
                for (Item item : room.getItems()) {
                    items.add(item.getClass().getSimpleName() + ":" + item.getName());
                }
                w.write("items;" + String.join(",", items));
                w.newLine();
                
                // Сохраняем монстра в комнате
                Monster monster = room.getMonster();
                if (monster != null) {
                    w.write("monster;" + monster.getName() + ":" + monster.getLevel() + ":" + monster.getHp());
                } else {
                    w.write("monster;");
                }
                w.newLine();
                
                // Сохраняем дверь в комнате
                Door door = room.getDoor();
                if (door != null) {
                    w.write("door;" + door.getName() + ":" + door.isLocked() + ":" + door.isOpened());
                } else {
                    w.write("door;");
                }
                w.newLine();
            }
            
            System.out.println("Сохранено в " + SAVE.toAbsolutePath());
            writeScore(p.getName(), s.getScore());
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    /**
     * Загружает состояние игры из файла.
     * @param s состояние игры для обновления
     */
    public static void load(GameState s) {
        if (!Files.exists(SAVE)) {
            System.out.println("Сохранение не найдено.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SAVE)) {
            List<String> lines = r.lines().toList();
            
            // Загружаем игрока
            Player p = s.getPlayer();
            String playerLine = lines.stream()
                .filter(line -> line.startsWith("player;"))
                .findFirst()
                .orElse("player;Hero;10;3");
            
            String[] pp = playerLine.split(";");
            p.setName(pp[1]);
            p.setHp(Integer.parseInt(pp[2]));
            p.setAttack(Integer.parseInt(pp[3]));
            p.getInventory().clear();
            
            // Загружаем инвентарь
            String inventoryLine = lines.stream()
                .filter(line -> line.startsWith("inventory;"))
                .findFirst()
                .map(line -> line.substring("inventory;".length()))
                .orElse("");
            
            if (!inventoryLine.isBlank()) {
                for (String tok : inventoryLine.split(",")) {
                    String[] t = tok.split(":", 2);
                    if (t.length < 2) continue;
                    switch (t[0]) {
                        case "Potion" -> p.getInventory().add(new Potion(t[1], 5));
                        case "Key" -> p.getInventory().add(new Key(t[1]));
                        case "Weapon" -> p.getInventory().add(new Weapon(t[1], 3));
                        default -> {}
                    }
                }
            }
            
            // Загружаем комнаты
            s.getRooms().clear();
            
            // Находим все комнаты в файле сохранения
            Map<String, String> roomData = new HashMap<>();
            Map<String, String> doorData = new HashMap<>();
            String currentRoomName = null;
            
            for (String line : lines) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) {
                    String key = parts[0];
                    String value = parts[1];
                    
                    if (key.equals("room_name")) {
                        currentRoomName = value;
                    } else if (currentRoomName != null && 
                              (key.equals("room_description") || 
                               key.equals("neighbors") || 
                               key.equals("items") || 
                               key.equals("monster"))) {
                        roomData.put(key + "_" + currentRoomName, value);
                    } else if (key.equals("door") && currentRoomName != null) {
                        doorData.put("door_" + currentRoomName, value);
                    }
                }
            }
            
            // Создаем комнаты
            for (String key : roomData.keySet()) {
                if (key.startsWith("room_name_")) {
                    String actualRoomName = roomData.get(key);
                    String roomKey = key.substring("room_name_".length());
                    
                    String description = roomData.get("room_description_" + roomKey);
                    Room room = new Room(actualRoomName, description);
                    
                    // Загружаем предметы
                    String itemsStr = roomData.get("items_" + roomKey);
                    if (itemsStr != null && !itemsStr.isEmpty()) {
                        for (String itemStr : itemsStr.split(",")) {
                            if (itemStr.trim().isEmpty()) continue;
                            String[] itemParts = itemStr.split(":", 2);
                            if (itemParts.length == 2) {
                                switch (itemParts[0]) {
                                    case "Potion" -> room.getItems().add(new Potion(itemParts[1], 5));
                                    case "Key" -> room.getItems().add(new Key(itemParts[1]));
                                    case "Weapon" -> room.getItems().add(new Weapon(itemParts[1], 3));
                                    default -> {}
                                }
                            }
                        }
                    }
                    
                    // Загружаем монстра
                    String monsterStr = roomData.get("monster_" + roomKey);
                    if (monsterStr != null && !monsterStr.isEmpty()) {
                        String[] monsterParts = monsterStr.split(":");
                        if (monsterParts.length == 3) {
                            room.setMonster(new Monster(monsterParts[0], Integer.parseInt(monsterParts[1]), Integer.parseInt(monsterParts[2])));
                        }
                    }
                    
                    // Загружаем дверь
                    String doorStr = doorData.get("door_" + roomKey);
                    if (doorStr != null && !doorStr.isEmpty()) {
                        String[] doorParts = doorStr.split(":");
                        if (doorParts.length == 3) {
                            Door door = new Door(doorParts[0]);
                            door.setLocked(Boolean.parseBoolean(doorParts[1]));
                            door.setOpened(Boolean.parseBoolean(doorParts[2]));
                            room.setDoor(door);
                        }
                    }
                    
                    s.addRoom(actualRoomName, room);
                }
            }
            
            // Устанавливаем текущую комнату
            String currentRoomLine = lines.stream()
                .filter(line -> line.startsWith("room;"))
                .findFirst()
                .map(line -> line.substring("room;".length()))
                .orElse(null);
                
            if (currentRoomLine != null) {
                Room roomToSet = s.getRoom(currentRoomLine);
                if (roomToSet != null) {
                    s.setCurrent(roomToSet);
                }
            }
            
            System.out.println("Игра загружена полностью.");
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось загрузить игру", e);
        }
    }

    /**
     * Выводит таблицу лидеров (топ-10).
     */
    public static void printScores() {
        if (!Files.exists(SCORES)) {
            System.out.println("Пока нет результатов.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SCORES)) {
            System.out.println("Таблица лидеров (топ-10):");
            r.lines().skip(1).map(l -> l.split(",")).map(a -> new Score(a[1], Integer.parseInt(a[2])))
                    .sorted(Comparator.comparingInt(Score::score).reversed()).limit(10)
                    .forEach(s -> System.out.println(s.player() + " — " + s.score()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    /**
     * Записывает результат игрока в таблицу лидеров.
     * @param player имя игрока
     * @param score набранные очки
     */
    private static void writeScore(String player, int score) {
        try {
            boolean header = !Files.exists(SCORES);
            try (BufferedWriter w = Files.newBufferedWriter(SCORES, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (header) {
                    w.write("ts,player,score");
                    w.newLine();
                }
                w.write(LocalDateTime.now() + "," + player + "," + score);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    /**
     * Вспомогательный класс для хранения информации об игроке и его счёте в таблице лидеров.
     * @param player имя игрока
     * @param score набранные очки
     */
    private record Score(String player, int score) {
    }
}
