package model;

import java.util.Date;

// Класс для записи всех операций на складе
public class Transaction {

    private final String id;           // уникальный номер операции
    private final String type;         // тип операции: "Приход" или "Расход"
    private final String productId;    // код товара, с которым работали
    private final int quantity;        // сколько штук пришло или ушло
    private final String partner;      // поставщик (приход) или получатель (расход)
    private final Date date;           // дата и время выполнения операции

    // Конструктор
    public Transaction(String id, String type, String productId, int quantity,
                       String partner) {
        this.id = id;
        this.type = type;
        this.productId = productId;
        this.quantity = quantity;
        this.partner = partner;
        this.date = new Date();        // автоматически ставим текущее время
    }

    // Геттеры (методы для получения информации)
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPartner() {
        return partner;
    }

    public Date getDate() {
        return date;
    }

    // Метод для сохранения операции в файл
    @Override
    public String toString() {
        return id + "," + type + "," + productId + "," + quantity + "," +
                partner + "," + date.getTime();
    }
}