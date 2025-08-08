package Task5_String;

import java.util.Scanner;

public class Arrow {
        public static void main(String[] args) {
            // Считываем входную строку
            Scanner scanner = new Scanner(System.in);
            // Запрашиваем у пользователя ввод символов
            System.out.println("Введите последовательность символов, состоящую из '>', '<' и '-':");
            String input = scanner.nextLine();
            scanner.close();

            // Выводим количество введённых символов
            int n = input.length();
            System.out.println("Вы ввели " + n + " символов.");

            // Инициализируем счётчик стрел
            int arrowCount = 0;

            // Ищем подстроки '>>-->' и '<--<<'
            for (int i = 0; i < n - 4; i++) {
                // Извлекаем подстроку длиной 5 символов
                String substring = input.substring(i, i + 5);
                // Информируем пользователя о текущей позиции и подстроке
                System.out.println("Проверяю позицию " + (i + 1) + ": " + substring);

                // Проверяем условие для '>>-->'
                if (substring.equals(">>-->")) {
                    arrowCount++;
                    System.out.println("Найдена стрелка! Текущий счёт: " + arrowCount);
                }
                // Проверяем условие для '<--<<'
                if (substring.equals("<--<<")) {
                    arrowCount++;
                    System.out.println("Найдена стрелка! Текущий счёт: " + arrowCount);
                }
            }

            // Выводим общее количество найденных стрел
            System.out.println("Общее количество найдённых стрел: " + arrowCount);
        }
}