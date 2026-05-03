package model;

// Класс для хранения информации о пользователях системы
public class User {

    private final String username;   // логин пользователя
    private String password;   // пароль
    private final String role;       // роль

    // Конструктор для создания нового пользователя
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Геттеры (методы для получения данных)
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // Сеттер только для пароля
    public void setPassword(String password) {
        this.password = password;
    }

    // Метод для сохранения пользователя в файл
    @Override
    public String toString() {
        return username + "," + password + "," + role;
    }
}