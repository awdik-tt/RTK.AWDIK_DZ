package com.example.dungeon.model;

/**
 * Класс, представляющий ключ в игре.
 * При применении открывает соответствующую дверь, если та находится в той же комнате.
 */
public class Key extends Item {
    // Имя двери, которую можно открыть этим ключом
    private String doorName;

    /**
     * Конструктор ключа.
     * Извлекает имя двери из названия ключа.
     * @param name название ключа
     */
    public Key(String name) {
        super(name);
        this.doorName = name.replace("Ключ", "").trim(); // Предполагаем, что имя двери содержится в названии ключа
    }

    /**
     * Конструктор ключа с явным указанием двери.
     * @param name название ключа
     * @param doorName имя двери, которую открывает ключ
     */
    public Key(String name, String doorName) {
        super(name);
        this.doorName = doorName;
    }

    /**
     * Применяет ключ - открывает дверь в текущей комнате, если ключ подходит.
     * @param ctx состояние игры, в котором применяется ключ
     */
    @Override
    public void apply(GameState ctx) {
        Room currentRoom = ctx.getCurrent();
        
        // Проверяем, есть ли дверь в комнате
        if (currentRoom.getDoor() != null && currentRoom.getDoor().isLocked()) {
            Door door = currentRoom.getDoor();
            
            // Проверяем, подходит ли ключ к этой двери
            if (door.getName().equals(doorName)) {
                door.setLocked(false);
                door.setOpened(true);
                System.out.println("Ключ открыл дверь: " + door.getName());
                
                // Удаляем ключ из инвентаря
                ctx.getPlayer().getInventory().remove(this);
            } else {
                System.out.println("Ключ не подходит к этой двери.");
            }
        } else {
            System.out.println("В этой комнате нет двери, которую можно открыть.");
        }
    }

    /**
     * Возвращает имя двери, которую открывает ключ.
     * @return имя двери
     */
    public String getDoorName() {
        return doorName;
    }
}
