package org.Task2ToyStore;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String menu = """
                1. Создать игрушку и добавить её в список разыгрываемых игрушек.
                2. Вывести список игрушек для розыгрыша.
                3. Организовать розыгрыш игрушек.
                4. Вывести список разыгранных игрушек.
                5. Изменить параметры игрушки.
                6. Выход.""";
        String quizToysFilePath = "src/main/java/org/ToyStore/QuizToys.txt";  //путь к разыгрываемым игрушкам
        String quizzedToysFilePath = "src/main/java/org/ToyStore/QuizzedToys.txt";  //путь к разыгранным игрушкам

        Scanner sc = new Scanner(System.in);
        int val = 0;
        String s = "";
        while (!"6".equals(s)) {
            System.out.println(menu);
            s = sc.next();
            try {
                val = Integer.parseInt(s);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
            switch (val) {
                case 1 -> {
                    Toy toy = createToy();  //создаем игрушку
                    saveToyToFile(quizToysFilePath, toy.getInfo());  //записываем её данные в файл
                }
                case 2 -> readToysListFromFileAndPrint(quizToysFilePath, true);  //вывод списка разыгрываемых игрушек
                case 3 -> {
                    ArrayList<String> listOfToysForQuiz = readToysListFromFileAndPrint(quizToysFilePath, false);
                    //создаем список игрушек для розыгрыша из всех игрушек, при этом не выводим его на экран
                    int winIndex = new Random().nextInt(listOfToysForQuiz.size());  //рандомно выбираем индекс одной из них
                    String wonToy = listOfToysForQuiz.get(winIndex).  //для вывода названия выигранной игрушки создаем
                            replace(":", "").         //строку с информацией об этой иргушке, переводим
                            replace(",", "");         //её в массив строк, в котором название будет под
                    System.out.println("Поздравляем! Вы выиграли игрушку " + wonToy.split(" ")[1] + "\n");  //индексом 1
                    saveToyToFile(quizzedToysFilePath, listOfToysForQuiz.get(winIndex));  //сохраняем информацию о выигранной игрушке в отдельный файл
                }
                case 4 -> readToysListFromFileAndPrint(quizzedToysFilePath, true);  //вывод списка разыгранных игрушек
                case 5 -> {
                    ArrayList<String> quizToysChanged = changeQuizToysList(quizToysFilePath);
                    //создаем список, в который нами внесены изменения
                    try {  //и сохраняем его, полностью перезаписывая файл
                        BufferedWriter bw = new BufferedWriter(new FileWriter(quizToysFilePath, false));
                        for (String str: quizToysChanged) {
                            bw.write(str + "\n");
                        }
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case 6 -> System.out.println("Завершение программы...");
                default -> System.out.println("Вы ввели неверное значение меню...\n");
            }
        }
        sc.close();
    }

    /**
     * Это метод по созданию экземпляра класса Toy. Пользователь задает название, id и вес игрушки.
     * @return созданную игрушку (экземпляр класса Toy)
     */
    public static Toy createToy() {
        String id;
        String name;
        String worth;
        int val;
        String s;
        System.out.println("Введите параметры игрушки");
        System.out.println("Название: ");
        Scanner sc = new Scanner(System.in);
        name = sc.nextLine();
        while (true) {
            try {
                System.out.println("ID (целым числом больше 0): ");
                id = sc.next();
                if (isNumeric(id)) throw new NumberFormatException();
                if (Integer.parseInt(id) <= 0)  throw new WrongNumberForCreateToy();
                break;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        while (true) {
            try {
                System.out.println("""
                        Выберите вес:
                        1 - 10
                        2 - 20
                        3 - 30""");
                s = sc.next();
                if (isNumeric(s)) throw new NumberFormatException();
                val = Integer.parseInt(s);
                switch (val) {
                    case 1 -> worth = "10";
                    case 2 -> worth = "20";
                    case 3 -> worth = "30";
                    default -> {
                        System.out.println("Вы ввели неверное значение (нужно 1, 2 или 3)!");
                        continue;
                    }
                }
                break;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        return new Toy(id, name, worth);
    }

    /**
     * Это метод, который проверяет, можно ли преобразовать строку в число
     * @param str это строка, которую нужно проверить
     * @return false, если можно преобразовать строку в число, либо true, если нельзя
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return false;
        } catch (java.lang.NumberFormatException e) {
            return true;
        }
    }

    /**
     * Это метод изменения названия и веса игрушки
     * @param path это путь к файлу со списком игрушек
     * @return список строк с измененными параметрами игрушек
     */
    public static ArrayList<String> changeQuizToysList (String path) {
        System.out.println("Выберите игрушку для изменения: ");
        ArrayList<String> list = readToysListFromFileAndPrint(path, true);//получаем и распечатываем список имеющихся игрушек
        String value;  // номер игрушки из распечатанного выше списка, параметры которой будем изменять
        Scanner sc = new Scanner(System.in);
        while (true) {  // вводим номер игрушки, параметры которой будем изменять (с выбрасыванием возможных ошибок)
            try {
                value = sc.nextLine();
                if (isNumeric(value)) throw new NumberFormatException();
                if (Integer.parseInt(value) <= 0 || Integer.parseInt(value) > list.size())
                    throw new WrongNumberForChangeQuizToysList(list.size());
                break;
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
        Toy toy = createToy();  // создаем экземпляр новой игрушки
        list.set(Integer.parseInt(value) - 1, toy.getInfo());  // в исходном списке заменяем строку с данными о
                                                                // выбранной игрушке на строку с данными новой игрушки
        return list;
    }

    /**
     * Это метод чтения файла со списком данных игрушек и его вывода пользователю (пронумерованного) при условии, что flag=true
     * @param path это путь к файлу со списком игрушек
     * @return список с данными игрушек
     */
    public static ArrayList<String> readToysListFromFileAndPrint (String path, Boolean flag) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            while (br.ready()) {
                list.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.isEmpty()) System.out.println("Список пуст!");  //если список пуст, сообщаем об этом
        else if (flag){  //если flag=true, распечатываем пронумерованный список игрушек, иначе не распечатываем
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
        System.out.println();
        return list;
    }

    /**
     * Это метод дополнения указанного файла информацией об игрушке.
     * @param path это путь к файлу
     * @param toyInfo это информация об игрушке, которой нужно дополнить файл
     */
    public static void saveToyToFile (String path, String toyInfo) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
            bw.write(toyInfo + "\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Это класс Exception, который сообщает об ошибке ввода (если вместо целых чисел вводится что-то другое)
 */
class NumberFormatException extends RuntimeException {
    public NumberFormatException () {
        super("При вводе должны использоваться только числа! Повторите ввод!");
    }
}

/**
 * Это класс Exception, который сообщает об ошибке ввода числа (введенное число не удовлетворяет условиям ввода)
 */
class WrongNumberForCreateToy extends RuntimeException {
    public WrongNumberForCreateToy () {
        super("Число должно быть больше 0! Повторите ввод данных!");
    }
}

/**
 * Это класс Exception, который сообщает об ошибке ввода числа (введенное число не удовлетворяет условиям ввода)
 */
class WrongNumberForChangeQuizToysList extends RuntimeException {
    public WrongNumberForChangeQuizToysList (int number) {
        super("Число должно быть от 1 до " + number + "! Повторите ввод данных!");
    }
}