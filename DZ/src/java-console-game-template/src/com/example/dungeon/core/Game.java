package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Основной класс игры DungeonMini.
 * Управляет игровым циклом, командами, состоянием игры и игровым миром.
 */
public class Game {
    // Состояние игры, включающее игрока, текущую комнату и счёт
    private final GameState state = new GameState();
    // Карта команд, доступных игроку, где ключ - название команды, значение - функция выполнения
    private final Map<String, Command> commands = new LinkedHashMap<>();

    static {
        WorldInfo.touch("Game");
    }

    /**
     * Конструктор класса Game.
     * Регистрирует команды и инициализирует игровой мир.
     */
    public Game() {
        registerCommands();
        bootstrapWorld();
    }
    
    // Примеры ошибок компиляции и выполнения:
    // Ошибка компиляции: следующая строка вызовет ошибку компиляции, 
    // так как строку нельзя привести к числу без преобразования
    // int number = "не число"; // Раскомментируйте для демонстрации ошибки компиляции
    
    // Ошибка выполнения: следующий код компилируется, но вызывает исключение во время выполнения
    // int result = divide(10, 0); // Это вызовет ArithmeticException при выполнении
    
    // Метод для демонстрации ошибки выполнения
    private int divide(int a, int b) {
        return a / b; // При b=0 будет выброшено ArithmeticException
    }

    /**
     * Регистрирует все доступные команды в игре.
     * Каждая команда связывается с функцией, которая будет выполнена при вводе этой команды.
     */
    private void registerCommands() {
        // Команда help - выводит список всех доступных команд
        commands.put("help", (ctx, a) -> System.out.println("Команды: " + String.join(", ", commands.keySet())));
        // Команда about - выводит информацию о разработчике, версии и дате реализации
        commands.put("about", (ctx, a) -> System.out.println("DungeonMini v1.0\nРазработчик: Толстиков Вадим\nДата реализации: 30.09.2025"));
        // Команда gc-stats - выводит информацию об использовании памяти и работе сборщика мусора
        commands.put("gc-stats", (ctx, a) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
        });
        // Команда alloc - выделяет большой объем памяти для демонстрации работы GC
        commands.put("alloc", (ctx, a) -> {
            System.out.println("Выделение большой памяти для демонстрации GC...");
            // Создаем массив объектов для заполнения памяти
            List<byte[]> memoryHog = new ArrayList<>();
            try {
                for (int i = 0; i < 1000; i++) {
                    memoryHog.add(new byte[1024 * 1024]); // 1MB на каждый массив
                }
            } catch (OutOfMemoryError e) {
                System.out.println("Память заполнена, вызывается GC...");
            }
            
            System.out.println("Память выделена, вызов GC...");
            System.gc(); // Принудительный вызов сборщика мусора
            
            // Ждем немного, чтобы GC успел отработать
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память после GC: used=" + used + " free=" + free + " total=" + total);
        });
        // Команда look - описывает текущую комнату, включая предметы, монстров и выходы
        commands.put("look", (ctx, a) -> System.out.println(ctx.getCurrent().describe()));
        // Команда move - перемещает игрока в указанном направлении
        commands.put("move", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите направление: move <north|south|east|west>");
            }
            String direction = a.get(0).toLowerCase();
            Room currentRoom = ctx.getCurrent();
            Room nextRoom = currentRoom.getNeighbors().get(direction);
            if (nextRoom == null) {
                throw new InvalidCommandException("В этом направлении нет пути");
            }
            
            // Проверяем, есть ли дверь между комнатами
            // В текущей реализации двери не связаны с направлениями, 
            // поэтому добавим специальную логику для комнаты с сокровищами
            if (nextRoom.getName().equals("Комната сокровищ")) {
                Door door = nextRoom.getDoor();
                if (door != null && !door.canPass()) {
                    throw new InvalidCommandException("Дверь в комнату сокровищ закрыта. Нужен ключ.");
                }
            }
            
            ctx.setCurrent(nextRoom);
            System.out.println("Вы перешли в: " + nextRoom.getName());
        });
        // Команда take - позволяет игроку взять предмет из текущей комнаты
        commands.put("take", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите название предмета: take <название>");
            }
            String itemName = String.join(" ", a);
            Room currentRoom = ctx.getCurrent();
            Item itemToTake = null;
            
            for (Item item : currentRoom.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    itemToTake = item;
                    break;
                }
            }
            
            if (itemToTake == null) {
                throw new InvalidCommandException("В этой комнате нет '" + itemName + "'");
            }
            
            currentRoom.getItems().remove(itemToTake);
            ctx.getPlayer().getInventory().add(itemToTake);
            System.out.println("Взято: " + itemToTake.getName());
        });
        // Команда inventory - выводит содержимое инвентаря игрока с группировкой по типам предметов
        commands.put("inventory", (ctx, a) -> {
            List<Item> inventory = ctx.getPlayer().getInventory();
            if (inventory.isEmpty()) {
                System.out.println("Инвентарь пуст");
                return;
            }
            
            // Используем Stream API для группировки и сортировки предметов
            Map<String, List<Item>> groupedItems = inventory.stream()
                .collect(Collectors.groupingBy(item -> item.getClass().getSimpleName()));
            
            groupedItems.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String itemType = entry.getKey();
                    List<Item> items = entry.getValue();
                    String itemNames = items.stream()
                        .map(Item::getName)
                        .distinct()
                        .collect(Collectors.joining(", "));
                    System.out.println("- " + itemType + " (" + items.size() + "): " + itemNames);
                });
        });
        // Команда use - применяет предмет из инвентаря игрока (полиморфизм)
        commands.put("use", (ctx, a) -> {
            if (a.isEmpty()) {
                throw new InvalidCommandException("Укажите название предмета: use <название>");
            }
            String itemName = String.join(" ", a);
            Player player = ctx.getPlayer();
            Item itemToUse = null;
            
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    itemToUse = item;
                    break;
                }
            }
            
            if (itemToUse == null) {
                throw new InvalidCommandException("У вас нет '" + itemName + "'");
            }
            
            // Вызываем метод apply у предмета (полиморфизм)
            itemToUse.apply(ctx);
        });
        // Команда fight - начинает бой между игроком и монстром в текущей комнате
        commands.put("fight", (ctx, a) -> {
            Room currentRoom = ctx.getCurrent();
            Monster monster = currentRoom.getMonster();
            
            if (monster == null) {
                throw new InvalidCommandException("В этой комнате нет монстра для боя");
            }
            
            Player player = ctx.getPlayer();
            int playerAttack = player.getAttack();
            int monsterAttack = monster.getLevel() * 2; // Атака монстра зависит от уровня
            
            System.out.println("Бой начался! Вы сражаетесь с " + monster.getName());
            
            while (monster.getHp() > 0 && player.getHp() > 0) {
                // Атака игрока
                int playerDamage = playerAttack;
                monster.setHp(monster.getHp() - playerDamage);
                System.out.println("Вы бьёте " + monster.getName() + " на " + playerDamage + ". HP монстра: " + Math.max(0, monster.getHp()));
                
                if (monster.getHp() <= 0) {
                    System.out.println("Вы победили " + monster.getName() + "!");
                    currentRoom.setMonster(null); // Удаляем монстра из комнаты
                    
                    // Выпадение лута (шанс 50%)
                    if (Math.random() > 0.5) {
                        Item loot = new Potion("Зелье опыта", 3);
                        currentRoom.getItems().add(loot);
                        System.out.println("Монстр выбросил: " + loot.getName());
                    }
                    break;
                }
                
                // Атака монстра
                int monsterDamage = monsterAttack;
                player.setHp(player.getHp() - monsterDamage);
                System.out.println("Монстр отвечает на " + monsterDamage + ". Ваше HP: " + Math.max(0, player.getHp()));
                
                if (player.getHp() <= 0) {
                    System.out.println("Вы были побеждены! Игра окончена.");
                    System.exit(0); // Завершаем игру при смерти игрока
                    break;
                }
                
                // Небольшая пауза для лучшего восприятия боя
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        // Команда save - сохраняет текущее состояние игры
        commands.put("save", (ctx, a) -> SaveLoad.save(ctx));
        // Команда load - загружает сохраненное состояние игры
        commands.put("load", (ctx, a) -> SaveLoad.load(ctx));
        // Команда scores - выводит таблицу лидеров
        commands.put("scores", (ctx, a) -> SaveLoad.printScores());
        // Команда exit - завершает игру
        commands.put("exit", (ctx, a) -> {
            System.out.println("Пока!");
            System.exit(0);
        });
    }

    /**
     * Инициализирует начальное состояние игрового мира.
     * Создает игрока, комнаты, устанавливает связи между комнатами, размещает предметы и монстров.
     */
    private void bootstrapWorld() {
        // Создаем игрока с начальными характеристиками
        Player hero = new Player("Герой", 20, 5);
        state.setPlayer(hero);

        // Создаем основные комнаты игрового мира
        Room square = new Room("Площадь", "Каменная площадь с фонтаном.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        
        // Создаем новую комнату с дверью
        Room treasureRoom = new Room("Комната сокровищ", "Таинственная комната с сокровищами.");
        Door secretDoor = new Door("Секретная дверь");
        treasureRoom.setDoor(secretDoor);
        treasureRoom.getItems().add(new Weapon("Волшебный меч", 10)); // Добавляем ценное оружие в комнату сокровищ
        
        // Устанавливаем связи между комнатами (направления переходов)
        square.getNeighbors().put("north", forest);
        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        cave.getNeighbors().put("west", forest);
        // Добавляем переход к комнате сокровищ через дверь (после открытия)
        // Пока дверь закрыта, доступа комнате сокровищ нет
        
        // Добавляем предметы и монстров в комнаты
        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 1, 8));
        
        // Добавляем ключ в пещеру
        cave.getItems().add(new Key("Ключ от секретной двери", "Секретная дверь"));

        // Устанавливаем начальную комнату для игрока
        state.setCurrent(square);
        
        // Добавляем комнаты в GameState для сохранения/загрузки
        state.addRoom("Площадь", square);
        state.addRoom("Лес", forest);
        state.addRoom("Пещера", cave);
        state.addRoom("Комната сокровищ", treasureRoom);
    }

    /**
     * Запускает основной игровой цикл.
     * Читает команды от пользователя и выполняет соответствующие действия.
     */
    public void run() {
        System.out.println("DungeonMini (TEMPLATE). 'help' — команды.");
        // Используем try-with-resources для автоматического закрытия потока ввода
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> "); // Приглашение для ввода команды
                String line = in.readLine(); // Читаем строку ввода
                if (line == null) break; // Если достигнут конец ввода, выходим из цикла
                line = line.trim(); // Убираем пробелы в начале и конце
                if (line.isEmpty()) continue; // Если строка пустая, пропускаем итерацию
                // Разбиваем строку на части по пробелам
                List<String> parts = Arrays.asList(line.split("\\s+"));
                // Первая часть - команда, остальные - аргументы
                String cmd = parts.get(0).toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                // Получаем команду из карты команд
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    // Выполняем команду с переданными аргументами
                    c.execute(state, args);
                    // Увеличиваем счет за успешное выполнение команды
                    state.addScore(1);
                } catch (InvalidCommandException e) {
                    // Обработка пользовательских ошибок команд
                    System.out.println("Ошибка: " + e.getMessage());
                } catch (Exception e) {
                    // Обработка непредвиденных ошибок
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            // Обработка ошибок ввода/вывода
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}
