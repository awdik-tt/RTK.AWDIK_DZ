package Task5_String;

/*Задача 1. Для введенной с клавиатуры буквы английского алфавита нужно вывести слева стоящую букву на стандартной клавиатуре.
При этом клавиатура замкнута, т.е. справа от буквы «p» стоит буква «a», а слева от "а" буква "р", также соседними считаются буквы «l» и буква «z»,
а буква «m» с буквой «q».
Входные данные: строка входного потока содержит один символ — маленькую букву английского алфавита.
Выходные данные: следует вывести букву стоящую слева от заданной буквы, с учетом замкнутости клавиатуры.*/

import java.util.Scanner;

public class LeftCharacter {
    public static void main(String[] args) {
        // Замкнутая последовательность букв алфавита
        String keyboard = "abcdefghijklmnopqrstuvwxyz";

        // Создаём объект для чтения ввода с клавиатуры
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите маленькую букву английского алфавита: ");

        // Считываем введённый символ
        char inputLetter = scanner.next().charAt(0);

        // Находим индекс введённой буквы
        int index = keyboard.indexOf(inputLetter);

        // Проверяем, что буква введена корректно (находится в алфавите)
        if (index == -1) {
            System.out.println("Ошибка: Вы ввели неподдерживаемый символ.");
        } else {
            // Находим индекс буквы слева
            int leftIndex = (index - 1 + keyboard.length()) % keyboard.length();
            char leftLetter = keyboard.charAt(leftIndex);
            // Выводим букву слева
            System.out.println("Буква слева: " + leftLetter);
        }
        scanner.close();
    }
}
