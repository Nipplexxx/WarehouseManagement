package view;

import model.Product;
import model.DataManager;
import model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Форма для прихода товаров на склад
public class IncomeForm extends JFrame {

    public IncomeForm(JFrame parent) {
        setTitle("Приход товаров на склад");
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Главная панель с сеткой
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Поля для ввода
        JLabel labelProduct = new JLabel("Товар:");
        JComboBox<String> comboProduct = new JComboBox<>();

        // Заполняем список товаров
        List<Product> products = DataManager.loadProducts();
        for (Product p : products) {
            comboProduct.addItem(p.getId() + " - " + p.getName());
        }

        JLabel labelQuantity = new JLabel("Количество:");
        JTextField tfQuantity = new JTextField();

        JLabel labelPartner = new JLabel("Поставщик:");
        JTextField tfPartner = new JTextField("ООО Поставщик");

        JButton btnSave = new JButton("Сохранить приход");
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

        // Кнопка "Сохранить приход"
        btnSave.addActionListener(e -> {
            try {
                String selected = (String) comboProduct.getSelectedItem();
                String productId = selected.split(" - ")[0];   // берём только код товара
                int quantity = Integer.parseInt(tfQuantity.getText().trim());
                String partner = tfPartner.getText().trim();

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, "Количество должно быть больше 0!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Обновляем остаток товара на складе
                List<Product> allProducts = DataManager.loadProducts();
                for (Product p : allProducts) {
                    if (p.getId().equals(productId)) {
                        p.setCurrentStock(p.getCurrentStock() + quantity);
                        break;
                    }
                }
                DataManager.saveProducts(allProducts);

                // Сохраняем операцию в историю
                String transId = "IN-" + System.currentTimeMillis();
                Transaction t = new Transaction(transId, "Приход", productId, quantity, partner);
                DataManager.saveTransaction(t);

                JOptionPane.showMessageDialog(this, "Приход успешно сохранён!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                dispose();   // закрываем форму

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка! Проверьте введённые данные.", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Кнопка отмены
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }
}