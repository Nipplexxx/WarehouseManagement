package view;

import model.User;
import model.Product;
import model.DataManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Это главное окно программы, которое открывается после входа
public class MainForm extends JFrame {

    private final User currentUser;   // здесь хранится текущий пользователь

    // Конструктор - сюда приходит пользователь после входа
    public MainForm(User user) {
        currentUser = user;

        // Название окна
        String role = "";
        if (user.getRole().equals("АДМИНИСТРАТОР")) {
            role = "Администратор";
        } else {
            role = "Кладовщик";
        }
        setTitle("Складское хозяйство - " + role + " (" + user.getUsername() + ")");

        setSize(950, 650);
        setLocationRelativeTo(null);           // окно в центре экрана
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Создаём главную панель
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Большая надпись сверху
        JLabel welcomeLabel = new JLabel("Добро пожаловать в систему складского хозяйства!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Панель с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 25, 25));

        // Создаём все кнопки
        JButton btnProducts = new JButton("Управление товарами");
        JButton btnIncome   = new JButton("Приход товаров");
        JButton btnOutcome  = new JButton("Расход товаров");
        JButton btnStock    = new JButton("Просмотр остатков");
        JButton btnHistory  = new JButton("История операций");
        JButton btnExit     = new JButton("Выход из системы");

        // Делаем кнопки большими и удобными
        Font font = new Font("Arial", Font.PLAIN, 18);
        btnProducts.setFont(font);
        btnIncome.setFont(font);
        btnOutcome.setFont(font);
        btnStock.setFont(font);
        btnHistory.setFont(font);
        btnExit.setFont(font);

        // Добавляем кнопки на панель
        buttonPanel.add(btnProducts);
        buttonPanel.add(btnIncome);
        buttonPanel.add(btnOutcome);
        buttonPanel.add(btnStock);
        buttonPanel.add(btnHistory);
        buttonPanel.add(btnExit);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Добавляем главную панель в окно
        add(mainPanel);

        // Привязываем кнопки к действиям
        btnProducts.addActionListener(e -> new ProductForm(this));
        btnIncome.addActionListener(e -> new IncomeForm(this));
        btnOutcome.addActionListener(e -> new OutcomeForm(this));
        btnStock.addActionListener(e -> showStock());
        btnHistory.addActionListener(e -> new HistoryForm(this));
        btnExit.addActionListener(e -> System.exit(0));

        // Кладовщик не может управлять товарами
        if (!currentUser.getRole().equals("АДМИНИСТРАТОР")) {
            btnProducts.setEnabled(false);
        }

        setVisible(true);
    }

    // Показать текущие остатки на складе
    private void showStock() {
        List<Product> products = DataManager.loadProducts();

        StringBuilder text = new StringBuilder("Текущие остатки товаров на складе:\n\n");

        for (Product p : products) {
            String status = "В норме";
            if (p.getCurrentStock() < p.getMinStock()) {
                status = "НИЗКИЙ ОСТАТОК!";
            }

            text.append(p.getId())
                    .append(" | ")
                    .append(p.getName())
                    .append(" | Остаток: ")
                    .append(p.getCurrentStock())
                    .append(" ")
                    .append(p.getUnit())
                    .append("   ")
                    .append(status)
                    .append("\n");
        }

        JTextArea area = new JTextArea(text.toString());
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(650, 450));

        JOptionPane.showMessageDialog(this, scroll, "Остатки на складе", JOptionPane.INFORMATION_MESSAGE);
    }
}