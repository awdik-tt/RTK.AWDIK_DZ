package Task4_TV;

public class App_TV {
    public static void main(String[] args) {
        // Создание объектов класса Television
        Television television1 = new Television("LG OLED55", 55);
        Television television2 = new Television("Samsung QLED QA65", 65);
        Television television3 = new Television("Sony Bravia KD-55", 55);

        // Проверка работы методов
        System.out.println(television1.getInfo());
        television1.turnOn();
        System.out.println(television1.getInfo());
        television1.turnOff();
        System.out.println(television1.getInfo());

        System.out.println(television2.getInfo());
        television2.turnOn();
        System.out.println(television2.getInfo());

        System.out.println(television3.getInfo());
        television3.turnOn();
        television3.turnOff();
    }
}
