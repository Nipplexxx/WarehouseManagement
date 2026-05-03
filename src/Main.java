import view.LoginForm;
import javax.swing.*;

// Главный класс программы.
// Отсюда начинается запуск всего приложения.
public class Main {

    static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Создаём и открываем окно входа в систему
                new LoginForm();
            }
        });
    }
}