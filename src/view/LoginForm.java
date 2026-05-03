package view;

import model.DataManager;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

// Форма для входа в систему (окно логина)
public class LoginForm extends JFrame {

    private final JTextField usernameField;      // поле для ввода логина
    private final JPasswordField passwordField;  // поле для ввода пароля
    private final JButton loginButton;           // кнопка "Войти"
    private final JButton registerButton;        // кнопка "Регистрация"
    private final JButton exitButton;            // кнопка "Выход"

    // Конструктор - здесь создаётся окно входа
    public LoginForm() {
        // Настраиваем окно
        setTitle("Вход в систему - Складское хозяйство");
        setSize(420, 280);
        setLocationRelativeTo(null);           // окно по центру экрана
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);                   // нельзя менять размер окна

        // Создаём панель для размещения элементов
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // отступы между элементами

        // Заголовок
        JLabel titleLabel = new JLabel("Складское хозяйство");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Логин
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Логин:"), gbc);

        usernameField = new JTextField(18);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Пароль
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Пароль:"), gbc);

        passwordField = new JPasswordField(18);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Кнопки
        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Войти");
        registerButton = new JButton("Регистрация");
        exitButton = new JButton("Выход");

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Добавляем панель в окно
        add(panel);

        // Привязываем кнопки к методам
        loginButton.addActionListener(e -> attemptLogin());
        registerButton.addActionListener(e -> new RegistrationForm(this));
        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);   // показываем окно
    }

    // Метод проверки логина и пароля
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Проверка, что поля заполнены
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Введите логин и пароль!",
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Загружаем пользователей из файла
        List<User> users = DataManager.loadUsers();

        // Ищем пользователя
        for (User user : users) {
            if (user.getUsername().equals(username) &&
                    user.getPassword().equals(password)) {

                JOptionPane.showMessageDialog(this,
                        "Добро пожаловать, " + username + "!",
                        "Успешный вход",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();               // закрываем окно входа
                new MainForm(user);      // открываем главное окно
                return;
            }
        }

        // Если пользователь не найден
        JOptionPane.showMessageDialog(this,
                "Неверный логин или пароль!",
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }
}