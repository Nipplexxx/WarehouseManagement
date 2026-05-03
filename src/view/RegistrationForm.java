package view;

import model.DataManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Форма для регистрации нового пользователя
public class RegistrationForm extends JFrame {

    private final JTextField usernameField;           // поле логина
    private final JPasswordField passwordField;       // поле пароля
    private final JPasswordField confirmPasswordField; // повтор пароля
    private final JComboBox<String> roleComboBox;     // выбор роли
    private final JButton registerButton;             // кнопка "Зарегистрировать"
    private final JButton cancelButton;               // кнопка "Отмена"

    // Конструктор - создаём окно регистрации
    public RegistrationForm(JFrame parent) {
        setTitle("Регистрация нового пользователя");
        setSize(460, 380);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Главная панель
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Заголовок
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Регистрация в системе склада");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, gbc);

        // Логин
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Логин:"), gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Пароль
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Пароль:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Повтор пароля
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Повторите пароль:"), gbc);
        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        // Выбор роли
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("Роль:"), gbc);
        roleComboBox = new JComboBox<>(new String[]{"РАБОТНИК", "АДМИНИСТРАТОР"});
        gbc.gridx = 1;
        panel.add(roleComboBox, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel();
        registerButton = new JButton("Зарегистрировать");
        cancelButton = new JButton("Отмена");

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);

        // Привязываем кнопки
        registerButton.addActionListener(e -> registerUser());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    // Метод регистрации пользователя
    private void registerUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Логин и пароль обязательны!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Пароли не совпадают!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<User> users = DataManager.loadUsers();

        // Проверяем, нет ли уже такого логина
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this, "Пользователь с таким логином уже существует!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Создаём и сохраняем нового пользователя
        User newUser = new User(username, password, role);
        users.add(newUser);
        DataManager.saveUsers(users);

        JOptionPane.showMessageDialog(this, "Пользователь успешно зарегистрирован!\nЛогин: " + username, "Успех", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}