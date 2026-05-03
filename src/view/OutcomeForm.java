package view;

import model.Product;
import model.DataManager;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Форма для расхода (списания) товаров со склада
public class OutcomeForm extends JFrame {

    public OutcomeForm(JFrame parent) {
        setTitle("Расход товаров со склада");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Главная панель с сеткой
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelProduct = new JLabel("Товар:");
        JComboBox<String> comboProduct = new JComboBox<>();

        // Заполняем список товаров
        List<Product> products = DataManager.loadProducts();
        for (Product p : products) {
            comboProduct.addItem(p.getId() + " - " + p.getName());
        }

        JLabel labelQuantity = new JLabel("Количество:");
        JTextField tfQuantity = new JTextField();

        JLabel labelPartner = new JLabel("Получатель:");
        JTextField tfPartner = new JTextField("Магазин");

        JButton btnSave = new JButton("Сохранить расход");
        JButton btnCancel = new JButton("Отмена");

        // Добавляем элементы на панель
        panel.add(labelProduct);
        panel.add(comboProduct);
        panel.add(labelQuantity);
        panel.add(tfQuantity);
        panel.add(labelPartner);
        panel.add(tfPartner);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));

        // Панель с кнопками
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        panel.add(btnPanel);

        add(panel);

        // Кнопка "Сохранить расход"
        btnSave.addActionListener(e -> {
            try {
                String selected = (String) comboProduct.getSelectedItem();
                String productId = selected.split(" - ")[0];
                int quantity = Integer.parseInt(tfQuantity.getText().trim());
                String partner = tfPartner.getText().trim();

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Количество должно быть больше 0!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Проверяем, хватает ли товара на складе
                List<Product> allProducts = DataManager.loadProducts();
                Product selectedProduct = null;
                for (Product p : allProducts) {
                    if (p.getId().equals(productId)) {
                        selectedProduct = p;
                        break;
                    }
                }

                if (selectedProduct == null || selectedProduct.getCurrentStock() < quantity) {
                    JOptionPane.showMessageDialog(this, "На складе недостаточно товара!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Списываем товар
                selectedProduct.setCurrentStock(selectedProduct.getCurrentStock() - quantity);
                DataManager.saveProducts(allProducts);

                // Сохраняем операцию в историю
                String transId = "OUT-" + System.currentTimeMillis();
                Transaction t = new Transaction(transId, "Расход", productId, quantity, partner);
                DataManager.saveTransaction(t);

                JOptionPane.showMessageDialog(this, "Расход успешно сохранён!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка! Проверьте введённые данные.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Кнопка отмены
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }
}