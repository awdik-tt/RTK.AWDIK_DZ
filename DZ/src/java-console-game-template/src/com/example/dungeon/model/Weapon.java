package com.example.dungeon.model;

/**
 * Класс, представляющий оружие в игре.
 * При применении увеличивает силу атаки игрока.
 */
public class Weapon extends Item {
    // Бонус к атаке, который даёт оружие
    private final int bonus;

    /**
     * Конструктор оружия.
     * @param name название оружия
     * @param bonus бонус к атаке, который даёт оружие
     */
    public Weapon(String name, int bonus) {
        super(name);
        this.bonus = bonus;
    }

    /**
     * Применяет оружие - увеличивает силу атаки игрока и удаляет оружие из инвентаря.
     * @param ctx состояние игры, в котором применяется оружие
     */
    @Override
    public void apply(GameState ctx) {
        var p = ctx.getPlayer();
        p.setAttack(p.getAttack() + bonus);
        System.out.println("Оружие экипировано. Атака теперь: " + p.getAttack());
        p.getInventory().remove(this);
    }
}
