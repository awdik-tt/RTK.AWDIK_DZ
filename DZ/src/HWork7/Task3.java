package HWork7;

import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

class PowerfulSet {

    /**
     * [Задание 3] Возвращает пересечение двух наборов (общие элементы).
     * @param set1 Первый набор.
     * @param set2 Второй набор.
     * @return Новый набор, содержащий только общие элементы.
     */
    public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
        // 1. Создаем копию первого набора. ЭТО ВАЖНО, чтобы не менять исходный set1.
        Set<T> result = new HashSet<>(set1);

        // 2. Используем метод retainAll: он оставляет в 'result' только те элементы,
        //    которые содержатся в set2.
        result.retainAll(set2);
        return result;
    } // Требование из источника [3]

    /**
     * [Задание 3] Возвращает объединение двух наборов (все уникальные элементы).
     * @return Новый набор, содержащий все уникальные элементы из set1 и set2.
     */
    public <T> Set<T> union(Set<T> set1, Set<T> set2) {
        // 1. Создаем копию первого набора.
        Set<T> result = new HashSet<>(set1);

        // 2. Используем метод addAll: добавляем все элементы из set2.
        //    Так как result — это Set, дубликаты автоматически игнорируются.
        result.addAll(set2);
        return result;
    } // Требование из источника [3]

    /**
     * [Задание 3] Возвращает элементы первого набора без тех, которые есть во втором (set1 - set2).
     * @return Новый набор, содержащий элементы set1, которых нет в set2.
     */
    public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
        // 1. Создаем копию первого набора.
        Set<T> result = new HashSet<>(set1);

        // 2. Используем метод removeAll: удаляем из 'result' все элементы,
        //    которые также присутствуют в set2.
        result.removeAll(set2);
        return result;
    } // Требование из источника [3]


    // --- Вспомогательный метод для ввода данных с консоли ---

    /**
     * Считывает элементы набора с консоли, разделенные запятыми.
     * Этот подход является более надежным, чем считывание по одному токену.
     */
    private static Set<Integer> readSetFromConsole(Scanner scanner, String setName) {
        Set<Integer> set = new HashSet<>();
        System.out.println("Введите элементы для " + setName + ", разделяя их запятыми (например, согласно примеру: 1, 2, 3):");

        // Считываем всю строку целиком.
        String inputLine = scanner.nextLine();

        // Разделяем строку по запятой, удаляя возможные пробелы
        String[] elements = inputLine.split(",");

        for (String elementStr : elements) {
            String trimmedStr = elementStr.trim();
            if (!trimmedStr.isEmpty()) {
                try {
                    // Парсим строку в Integer
                    int element = Integer.parseInt(trimmedStr);
                    set.add(element);
                } catch (NumberFormatException e) {
                    System.err.println("Предупреждение: Не удалось распознать '" + trimmedStr + "' как целое число. Элемент пропущен.");
                }
            }
        }
        return set;
    }

    public static void main(String[] args) {
        PowerfulSet ps = new PowerfulSet();
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Задание 3: PowerfulSet ---");

        // 1. Формируем Set 1
        Set<Integer> set1 = readSetFromConsole(scanner, "Set 1");

        // 2. Формируем Set 2
        Set<Integer> set2 = readSetFromConsole(scanner, "Set 2");

        System.out.println("\n--- Исходные данные ---");
        System.out.println("Set 1: " + set1);
        System.out.println("Set 2: " + set2);

        System.out.println("\n--- Результаты PowerfulSet ---");

        // 3. Вызов методов, определенных в Задании 3 [3]

        // Пересечение (Intersection): ожидается {1, 2} для примера из задания
        System.out.println("Пересечение (Intersection): " + ps.intersection(set1, set2));

        // Объединение (Union): ожидается {0, 1, 2, 3, 4} для примера из задания
        System.out.println("Объединение (Union): " + ps.union(set1, set2));

        // Относительное дополнение (set1 - set2): ожидается {3} для примера из задания
        System.out.println("Разность (Relative Complement, Set1 - Set2): " + ps.relativeComplement(set1, set2));
    }
}