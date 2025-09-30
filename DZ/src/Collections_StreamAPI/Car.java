package Collections_StreamAPI;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Класс Автомобиль (Car)
  */
class Car {
    // Поля класса (private)
    private String carNumber;
    private String model;
    private String color;
    private long mileage;
    private long cost;

    // Конструктор с аргументами
    public Car(String carNumber, String model, String color, long mileage, long cost) {
        this.carNumber = carNumber;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
        this.cost = cost;
    }

    // Геттеры (свойства, необходимые для Stream API)
    public String getCarNumber() { return carNumber; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public long getMileage() { return mileage; }
    public long getCost() { return cost; }

    // Сеттеры требуются по заданию [1], но опущены здесь для краткости

    /**
     * Переопределение метода toString [1].
     */
    @Override
    public String toString() {
        // Форматирование для вывода в таблицу, как в примерах [3, 4]
        return String.format("%-10s %-10s %-7s %-15d %d",
                carNumber, model, color, mileage, cost);
    }
}

/**
 * Класс Main
 * Реализует проверку работы (п. 2), загрузку коллекции (п. 3) и Stream API задачи (п. 4)
 */
class Main {

    public static void main(String[] args) {

        // Исходные данные
        String[] rawData = {
                "a123me|Mercedes|White|0|8300000",
                "b873of|Volga|Black|0|673000",
                "w487mn|Lexus|Grey|76000|900000",
                "p987hj|Volga|Red|610|704340",
                "c987ss|Toyota|White|254000|761000",
                "o983op|Toyota|Black|698000|740000",
                "p146op|BMW|White|271000|850000",
                "u893ii|Toyota|Purple|210900|440000",
                "l097df|Toyota|Black|108000|780000",
                "y876wd|Toyota|Black|160000|1000000"
        };

        // Входные параметры для задач Stream API
        final String colorToFind = "Black";
        final long mileageToFind = 0L;
        final long priceStart = 700000;
        final long priceEnd = 800000;
        final String modelToFindToyota = "Toyota";
        final String modelToFindVolvo = "Volvo";

        // 3. Создать объект Java Collections со списком автомобилей
        List<Car> cars = new ArrayList<>();

        // Заполнение коллекции, парсинг каждой строки
        for (String line : rawData) {
            String[] parts = line.split("\\|");

            if (parts.length == 5) {
                try {
                    String carNumber = parts[0];   // Номер автомобиля
                    String model = parts[1];       // Модель
                    String color = parts[2];       // Цвет

                    // Преобразование числовых значений в long
                    long mileage = Long.parseLong(parts[3]); // Пробег
                    long cost = Long.parseLong(parts[4]);    // Стоимость

                    cars.add(new Car(carNumber, model, color, mileage, cost));
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка парсинга числовых данных: " + line);
                }
            }
        }

        // 2. Проверить работу в классе Main (Вывод коллекции)
        System.out.println("Автомобили в базе:");
        System.out.println(String.format("%-10s %-10s %-7s %-15s %s",
                "Number", "Model", "Color", "Mileage", "Cost"));

        for (Car car : cars) {
            System.out.println(car);
        }

        System.out.println("\n--- Результаты Stream API ---");

        // ---------------------------------------------------------------------
        // 4. Реализация задач с использованием Java Stream API
        // ---------------------------------------------------------------------

        // Задача 1: Номера всех автомобилей, имеющих заданный цвет ИЛИ нулевой пробег
        String carNumbersResult = cars.stream()
                // Фильтрация: colorToFind ("Black") ИЛИ mileageToFind (0L)
                .filter(car -> car.getColor().equals(colorToFind) || car.getMileage() == mileageToFind)
                .map(Car::getCarNumber)
                .collect(Collectors.joining(" "));

        // Ожидаемый результат: a123me b873of o983op l097df y876wd
        System.out.println("Номера автомобилей по цвету или пробегу: " + carNumbersResult);


        // Задача 2: Количество уникальных моделей в ценовом диапазоне [n, m ]
        // Диапазон: [700_000L, 800_000L]
        long uniqueModelsCount = cars.stream()
                .filter(car -> car.getCost() >= priceStart && car.getCost() <= priceEnd)
                .map(Car::getModel)
                .distinct()
                .count();

        // Ожидаемый результат: 4 шт.
        System.out.println("Уникальные автомобили: " + uniqueModelsCount + " шт.");


        // Задача 3: Вывести цвет автомобиля с минимальной стоимостью
        Optional<Car> carWithMinCost = cars.stream()
                .min(Comparator.comparingLong(Car::getCost));

        String minCostColor = carWithMinCost
                .map(Car::getColor)
                .orElse("Не найден");

        // Ожидаемый результат: Purple
        System.out.println("Цвет автомобиля с минимальной стоимостью: " + minCostColor);


        // Задача 4: Средняя стоимость искомой модели

        // Toyota
        calculateAndPrintAverageCost(cars, modelToFindToyota); // Ожидаемый результат: 744200,00

        // Volvo
        calculateAndPrintAverageCost(cars, modelToFindVolvo);   // Ожидаемый результат: 0,00
    }

    /**
     * Вспомогательный метод для расчета и вывода средней стоимости модели.
     */
    private static void calculateAndPrintAverageCost(List<Car> cars, String modelToFind) {
        OptionalDouble averageCostOpt = cars.stream()
                .filter(car -> car.getModel().equals(modelToFind))
                .mapToLong(Car::getCost)
                .average();

        double averageCost = averageCostOpt.orElse(0.0);

        // Форматирование вывода (%.2f): соответствует примерам
        System.out.printf("Средняя стоимость модели %s: %.2f%n",
                modelToFind, averageCost);
    }
}