package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    // Названия файлов, где хранятся данные
    public static String usersFile = "users.txt";
    public static String productsFile = "products.txt";
    public static String transactionsFile = "transactions.txt";

    // Загрузка всех пользователей из файла
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(usersFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
            br.close();
        } catch (Exception e) {
            // Если файла нет - создаём тестовых пользователей
            createDefaultUser();
        }
        return users;
    }

    // Создаём тестового администратора и кладовщика при первом запуске
    private static void createDefaultUser() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile));
            bw.write("admin,admin,ADMIN");
            bw.newLine();
            bw.write("worker,123,WORKER");
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.out.println("Не удалось создать файл пользователей");
        }
    }

    // Сохранение пользователей в файл
    public static void saveUsers(List<User> users) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(usersFile));
            for (User u : users) {
                bw.write(u.toString());
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Ошибка сохранения пользователей");
        }
    }

    // Загрузка всех товаров
    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(productsFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Product p = new Product(parts[0], parts[1], parts[2], parts[3],
                            Double.parseDouble(parts[4]),
                            Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[6]));
                    products.add(p);
                }
            }
            br.close();
        } catch (Exception e) {
            // Файл может не существовать - это нормально
        }
        return products;
    }

    // Сохранение товаров
    public static void saveProducts(List<Product> products) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(productsFile));
            for (Product p : products) {
                bw.write(p.toString());
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            System.out.println("Ошибка сохранения товаров");
        }
    }

    // Загрузка истории операций
    public static List<Transaction> loadTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(transactionsFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Transaction t = new Transaction(
                            parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]),
                            parts[4]
                    );
                    transactions.add(t);
                }
            }
            br.close();
        } catch (Exception e) {
            // Файл может быть пустым - это нормально
        }
        return transactions;
    }

    // Сохранение одной операции (приход или расход)
    public static void saveTransaction(Transaction t) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(transactionsFile, true));
            bw.write(t.toString());
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            System.out.println("Ошибка сохранения операции");
        }
    }
}