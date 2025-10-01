package com.example.dungeon.model;

/**
 * Класс, представляющий зелье в игре.
 * При применении восстанавливает здоровье игрока.
 */
public class Potion extends Item {
    // Количество здоровья, которое восстанавливает зелье
    private final int heal;

    /**
     * Конструктор зелья.
     * @param name название зелья
     * @param heal количество здоровья, которое восстанавливает зелье
     */
    public Potion(String name, int heal) {
        super(name);
        this.heal = heal;
    }

    /**
     * Применяет зелье - восстанавливает здоровье игрока и удаляет зелье из инвентаря.
     * @param ctx состояние игры, в котором применяется зелье
     */
    @Override
    public void apply(GameState ctx) {
        Player p = ctx.getPlayer();
        p.setHp(p.getHp() + heal);
        System.out.println("Выпито зелье: +" + heal + " HP. Текущее HP: " + p.getHp());
        p.getInventory().remove(this);
    }
}
