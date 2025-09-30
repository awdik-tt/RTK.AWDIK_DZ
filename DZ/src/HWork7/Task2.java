package HWork7;

import java.util.Arrays;
import java.util.Scanner;

public class Task2 {

    /**
     * Проверяет, являются ли две строки анаграммами.
     * Использует сортировку символов.
     * @param s Первая строка.
     * @param t Вторая строка.
     * @return true, если строки являются анаграммами, иначе false.
     */
    public static boolean isAnagram(String s, String t) {
        // 1. Нормализация строк: удаляем пробелы и приводим к нижнему регистру
        String cleanS = s.replaceAll("\\s", "").toLowerCase();
        String cleanT = t.replaceAll("\\s", "").toLowerCase();

        // 2. Анаграммы должны иметь одинаковую длину
        if (cleanS.length() != cleanT.length()) {
            return false;
        }

        // 3. Преобразуем строки в массивы символов и сортируем их
        char[] charArrayS = cleanS.toCharArray();
        char[] charArrayT = cleanT.toCharArray();

        Arrays.sort(charArrayS);
        Arrays.sort(charArrayT);

        // 4. Сравниваем отсортированные массивы
        return Arrays.equals(charArrayS, charArrayT);
    }

    // Пример использования (для тестирования)
    // /*
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите первую строку (s):");
        String s = scanner.nextLine();
        System.out.println("Введите вторую строку (t):");
        String t = scanner.nextLine();

        System.out.println("Являются ли строки анаграммами? " + isAnagram(s, t));
        // Если вход: Бейсбол и бобслей -> true (пример из источника [3])
    }
   //  */
}
