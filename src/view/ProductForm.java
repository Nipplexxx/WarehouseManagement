package view;

import model.Product;
import model.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Форма для управления товарами на складе
public class ProductForm extends JFrame {

    private final DefaultTableModel tableModel;   // модель таблицы
    private final JTable productTable;            // таблица с товарами
    private final JTextField searchField;         // поле поиска

    // Конструктор - создаём окно управления товарами
    public ProductForm(JFrame parent) {
        setTitle("Управление товарами");
        setSize(1050, 650);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Верхняя панель с поиском
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Поиск по названию или ID:");
        searchField = new JTextField(25);
        JButton searchButton = new JButton("Найти");
        JButton refreshButton = new JButton("Обновить таблицу");

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(refreshButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Таблица товаров
        String[] columnNames = {"ID", "Название товара", "Категория", "Ед.изм",
                "Цена закупки", "Мин. остаток", "Текущий остаток"};

        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        productTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(productTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Нижняя панель с кнопками
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton addButton = new JButton("Добавить новый товар");
        JButton editButton = new JButton("Редактировать товар");
        JButton deleteButton = new JButton("Удалить товар");
        JButton closeButton = new JButton("Закрыть окно");

        // Делаем кнопки крупнее
        Font btnFont = new Font("Arial", Font.PLAIN, 16);
        addButton.setFont(btnFont);
        editButton.setFont(btnFont);
        deleteButton.setFont(btnFont);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Загружаем товары при открытии
        loadProductsToTable();

        // Привязываем кнопки
        addButton.addActionListener(e -> addProduct());
        editButton.addActionListener(e -> editProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        searchButton.addActionListener(e -> searchProducts());
        refreshButton.addActionListener(e -> loadProductsToTable());
        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Загрузка всех товаров в таблицу
    private void loadProductsToTable() {
        tableModel.setRowCount(0);   // очищаем таблицу

        List<Product> products = DataManager.loadProducts();

        for (Product p : products) {
            Object[] row = {
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getUnit(),
                    p.getPurchasePrice(),
                    p.getMinStock(),
                    p.getCurrentStock()
            };
            tableModel.addRow(row);
        }
    }

    // Добавление нового товара
    private void addProduct() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(25);
        JTextField catField = new JTextField(18);
        JTextField unitField = new JTextField(8);
        JTextField priceField = new JTextField(12);
        JTextField minStockField = new JTextField(8);
        JTextField currentStockField = new JTextField(8);

        Object[] fields = {
                "ID товара:", idField,
                "Название товара:", nameField,
                "Категория:", catField,
                "Единица измерения:", unitField,
                "Цена закупки (руб):", priceField,
                "Минимальный остаток:", minStockField,
                "Текущий остаток:", currentStockField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Добавление нового товара", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                String category = catField.getText().trim();
                String unit = unitField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int minStock = Integer.parseInt(minStockField.getText().trim());
                int currentStock = Integer.parseInt(currentStockField.getText().trim());

                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "ID и Название товара обязательны!",
                            "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Product newProduct = new Product(id, name, category, unit, price, minStock, currentStock);

                List<Product> products = DataManager.loadProducts();
                products.add(newProduct);
                DataManager.saveProducts(products);

                loadProductsToTable();
                JOptionPane.showMessageDialog(this, "Товар успешно добавлен!",
                        "Успех", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка! Проверьте введённые числа.",
                        "Ошибка ввода", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Редактирование товара
    private void editProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Сначала выберите товар в таблице!",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        List<Product> products = DataManager.loadProducts();

        Product productToEdit = null;
        for (Product p : products) {
            if (p.getId().equals(id)) {
                productToEdit = p;
                break;
            }
        }

        if (productToEdit == null) return;

        JTextField nameField = new JTextField(productToEdit.getName(), 25);
        JTextField catField = new JTextField(productToEdit.getCategory(), 18);
        JTextField unitField = new JTextField(productToEdit.getUnit(), 8);
        JTextField priceField = new JTextField(String.valueOf(productToEdit.getPurchasePrice()), 12);
        JTextField minStockField = new JTextField(String.valueOf(productToEdit.getMinStock()), 8);
        JTextField currentStockField = new JTextField(String.valueOf(productToEdit.getCurrentStock()), 8);

        Object[] fields = {
                "Название:", nameField,
                "Категория:", catField,
                "Ед.изм:", unitField,
                "Цена закупки:", priceField,
                "Мин. остаток:", minStockField,
                "Текущий остаток:", currentStockField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Редактирование товара", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Product updatedProduct = new Product(
                        id,
                        nameField.getText().trim(),
                        catField.getText().trim(),
                        unitField.getText().trim(),
                        Double.parseDouble(priceField.getText().trim()),
                        Integer.parseInt(minStockField.getText().trim()),
                        Integer.parseInt(currentStockField.getText().trim())
                );

                products.removeIf(p -> p.getId().equals(id));
                products.add(updatedProduct);
                DataManager.saveProducts(products);

                loadProductsToTable();
                JOptionPane.showMessageDialog(this, "Товар успешно обновлён!",
                        "Успех", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при редактировании!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Удаление товара
    private void deleteProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Выберите товар для удаления!",
                    "Внимание", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить товар:\n" + name + " (" + id + ")?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            List<Product> products = DataManager.loadProducts();
            products.removeIf(p -> p.getId().equals(id));
            DataManager.saveProducts(products);

            loadProductsToTable();
            JOptionPane.showMessageDialog(this, "Товар успешно удалён!",
                    "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Поиск товаров
    private void searchProducts() {
        String text = searchField.getText().trim().toLowerCase();

        if (text.isEmpty()) {
            loadProductsToTable();
            return;
        }

        tableModel.setRowCount(0);
        List<Product> products = DataManager.loadProducts();

        for (Product p : products) {
            if (p.getName().toLowerCase().contains(text) ||
                    p.getCategory().toLowerCase().contains(text) ||
                    p.getId().toLowerCase().contains(text)) {

                Object[] row = {
                        p.getId(), p.getName(), p.getCategory(), p.getUnit(),
                        p.getPurchasePrice(), p.getMinStock(), p.getCurrentStock()
                };
                tableModel.addRow(row);
            }
        }
    }
}