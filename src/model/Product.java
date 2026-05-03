package model;

// Класс для описания одного товара на складе
public class Product {

    // Поля класса
    private final String id;              // код товара
    private final String name;            // название товара
    private final String category;        // категория
    private final String unit;            // единица измерения
    private final double purchasePrice;   // цена закупки
    private final int minStock;           // минимальный остаток на складе
    private int currentStock;       // текущее количество на складе

    // Конструктор
    public Product(String id, String name, String category, String unit,
                   double purchasePrice, int minStock, int currentStock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.purchasePrice = purchasePrice;
        this.minStock = minStock;
        this.currentStock = currentStock;
    }

    // Геттеры (методы для получения данных)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public int getMinStock() {
        return minStock;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    // Сеттер только для текущего остатка (чтобы менять количество на складе)
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    // Метод для сохранения товара в файл (в формате CSV)
    @Override
    public String toString() {
        return id + "," + name + "," + category + "," + unit + "," +
                purchasePrice + "," + minStock + "," + currentStock;
    }
}