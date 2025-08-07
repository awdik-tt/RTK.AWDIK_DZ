package Task4_TV;

class Television {
    // Поля класса
    private String model;  // Модель
    private int screenSize; // Размер экрана в дюймах
    private boolean isOn;   // Состояние (включен или выключен)

    // Конструктор
    public Television(String model, int screenSize) {
        this.model = model;
        this.screenSize = screenSize;
        this.isOn = false; // По умолчанию телевизор выключен
    }

    // Метод для включения телевизора
    public void turnOn() {
        isOn = true;
        System.out.println("Включаем телевизор: Телевизор " + model + " включен.");
    }

    // Метод для выключения телевизора
    public void turnOff() {
        isOn = false;
        System.out.println("Выключаем телевизор: Телевизор " + model + " выключен.");
    }

    // Метод для получения информации о телевизоре
    public String getInfo() {
        String state = isOn ? "включен" : "выключен";
        return "Модель: " + model + ", Размер экрана: " + screenSize + " дюймов, Состояние: " + state;
    }
}

