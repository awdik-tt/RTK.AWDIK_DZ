package HWork7;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

/**
 * Задание 1: Возвращает набор уникальных элементов из списка.
 * Реализовано с использованием коллекций (ArrayList и HashSet) и вводом с консоли.
 */
class Task1_Console {

    /**
     * Реализует логику получения набора уникальных элементов из списка.
     * @param list Входной список с элементами.
     * @param <T> Тип элементов.
     * @return Набор уникальных элементов.
     */
    public static <T> Set<T> getUniqueElements(ArrayList<T> list) {
        // Создание HashSet из ArrayList автоматически обеспечивает наличие только уникальных элементов.
        return new HashSet<>(list);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> inputList = new ArrayList<>();

        System.out.println("--- Задание 1: Набор уникальных элементов ---");
        System.out.println("Введите элементы списка по одному. Для завершения ввода введите 'done'.");

        String input;
        // Цикл для сбора данных с консоли
        while (true) {
            System.out.print("Введите элемент (или 'done'): ");
            // Читаем всю строку, включая пробелы
            input = scanner.nextLine();

            // Проверка на команду завершения
            if (input.equalsIgnoreCase("done")) {
                break;
            }

            // Добавляем элемент, если строка не пуста
            if (!input.isEmpty()) {
                inputList.add(input);
            }
        }

        // Закрываем сканнер
        scanner.close();

        if (inputList.isEmpty()) {
            System.out.println("Список для обработки пуст.");
        } else {
            System.out.println("\nИсходный список (ArrayList): " + inputList);

            // Вызываем метод для получения уникального набора
            Set<String> uniqueSet = getUniqueElements(inputList);

            System.out.println("Набор уникальных элементов (Set): " + uniqueSet);
        }
    }
}