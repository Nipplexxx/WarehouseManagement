package view;

import model.Transaction;
import model.DataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

// Форма для просмотра истории всех операций на складе
public class HistoryForm extends JFrame {

    private final DefaultTableModel tableModel;   // модель таблицы
    private final JTable table;                   // сама таблица

    // Конструктор - создаём окно истории
    public HistoryForm(JFrame parent) {
        setTitle("История операций");
        setSize(900, 550);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Заголовок
        JLabel titleLabel = new JLabel("История приходов и расходов товаров", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Создаём таблицу
        String[] columnNames = {"ID операции", "Тип", "Код товара", "Количество", "Партнёр", "Дата"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Панель с кнопками внизу
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Обновить");
        JButton closeButton = new JButton("Закрыть");

        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Загружаем данные при открытии окна
        loadHistoryToTable();

        // Привязываем кнопки
        refreshButton.addActionListener(e -> loadHistoryToTable());
        closeButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Метод загрузки всех операций в таблицу
    private void loadHistoryToTable() {
        tableModel.setRowCount(0);   // очищаем таблицу

        List<Transaction> transactions = DataManager.loadTransactions();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        for (Transaction t : transactions) {
            String dateStr = dateFormat.format(t.getDate());

            Object[] row = {
                    t.getId(),
                    t.getType(),
                    t.getProductId(),
                    t.getQuantity(),
                    t.getPartner(),
                    dateStr
            };
            tableModel.addRow(row);
        }

        // Если операций ещё нет
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Пока нет ни одной операции на складе.",
                    "Информация",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}