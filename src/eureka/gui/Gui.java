package eureka.gui;

import eureka.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

public class Gui extends JFrame {
    public static JMenuBar menu = new JMenuBar();
    public static JMenu file = new JMenu("File");
    public static JMenu compare = new JMenu("Compare");
    public static JMenu open = new JMenu("Open");
    public static JMenuItem about = new JMenuItem("About");
    public static JMenuItem suspected = new JMenuItem("Suspected");
    public static JMenuItem check = new JMenuItem("Check");
    public static JMenuItem exit = new JMenuItem("Exit");
    public static JMenuItem run = new JMenuItem("Run");
    public static JCheckBoxMenuItem multiCheck = new JCheckBoxMenuItem("Multi Check");
    public static JCheckBoxMenuItem debug = new JCheckBoxMenuItem("Debug");
    public static JLabel coincidenceLines = new JLabel("Coincidence lines: N/A");
    public static JLabel plagiatMeter = new JLabel("Plagiat meter: N/A");
    public static JButton language = new JButton("EN");

    public static JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    public static File suspectedFile, checkFile;
    public static BufferedImage imageIcon, gendalfIcon;

    public Gui(int width, int height) {
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.getContentPane().setBackground(Color.GRAY);
        this.title();

        try {
            imageIcon = ImageIO.read(ClassLoader.getSystemResource("assets/fallout.png"));
            gendalfIcon = ImageIO.read(ClassLoader.getSystemResource("assets/gendalf.png"));
        } catch (IOException e) {
            Main.debug(Level.WARNING, "Can't load icon.");
            e.fillInStackTrace();
        }
        this.setIconImage(imageIcon);

        fileSection();
        aboutSection();
        compareSection();

        initMenu();

        this.setVisible(true);
    }

    private void title() {
        StringBuilder title = new StringBuilder();
        title.append("Anti Plagiarism");

        if (suspectedFile != null) {
            title.append(" | ").append(suspectedFile.getName());
            if (checkFile != null) title.append("/").append(checkFile.getName());
        }

        this.setTitle(title.toString());
    }

    private void fileSection() {
        suspected.addActionListener(action -> {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                suspectedFile = fileChooser.getSelectedFile();
                Main.debug(Level.INFO, suspectedFile.getName() + " was chosen as suspected.");

                this.title();
            }
        });
        check.addActionListener(action -> {
            fileChooser.setFileSelectionMode(multiCheck.isSelected() ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                checkFile = fileChooser.getSelectedFile();
                Main.debug(Level.INFO, checkFile.getName() + " was chosen as checkFile.");

                this.title();
            }
        });
        multiCheck.addActionListener(action -> {
            if (multiCheck.isSelected()) {
                switch (language.getText()) {
                    case "UA" -> check.setText("Директорія");
                    case "EN" -> check.setText("Directory");
                    case "RU" -> check.setText("Папка");
                }
            } else {
                switch (language.getText()) {
                    case "UA" -> check.setText("Перевіряючий");
                    case "EN" -> check.setText("Check");
                    case "RU" -> check.setText("Переверяющий");
                }
            }
        });
        exit.addActionListener(action -> System.exit(1));
    }

    private void aboutSection() {
        about.addActionListener(this::about);
    }

    private void compareSection() {
        run.addActionListener(action -> {
            if (suspectedFile == null)
                JOptionPane.showMessageDialog(null, "Suspected file is not chosen.", "Error", JOptionPane.ERROR_MESSAGE);
            if (checkFile == null)
                JOptionPane.showMessageDialog(null, "Check file is not chosen.", "Error", JOptionPane.ERROR_MESSAGE);
            if (suspectedFile == null || checkFile == null) return;

            try {
                Main.compare.compare(suspectedFile, checkFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        debug.setState(true);
    }

    private void initMenu() {
        open.add(suspected);
        open.add(check);

        file.add(open);
        file.add(multiCheck);
        file.add(exit);

        compare.add(run);
        compare.add(debug);

        menu.add(file);
        menu.add(about);
        menu.add(compare);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(gendalfIcon, 0, 0, null);
            }
        };

        coincidenceLines.setBounds(5, -5, 150, 40);
        plagiatMeter.setBounds(5, 20, 150, 40);

        language.setBounds(365, 190, 70, 25);
        language.setFocusPainted(false);
        language.addActionListener(action -> setLanguage());

        this.add(coincidenceLines);
        this.add(plagiatMeter);

        this.add(language);
        this.add(panel);
        this.setJMenuBar(menu); // Присваивает меню к гуи
    }

    public static void setLanguage() {
        switch (language.getText()) {
            case "EN" -> {
                file.setText("Файл");
                compare.setText("Порівняти");
                open.setText("Вибрати");

                about.setText("Про Програму");
                suspected.setText("Підозрюваний");

                if (multiCheck.isSelected()) check.setText("Директорія");
                else check.setText("Перевіряючий");

                exit.setText("Вихід");
                run.setText("Запуск");

                multiCheck.setText("Мульти Перевірка");
                debug.setText("Дебаг");

                if (coincidenceLines.getText().contains("N/A")) coincidenceLines.setText("Співпалі лінії: N/A");
                else {
                    String[] split = coincidenceLines.getText().split(": ");
                    int value = Integer.parseInt(split[1]);

                    coincidenceLines.setText("Співпалі лінії: " + value);
                }

                if (plagiatMeter.getText().contains("N/A")) plagiatMeter.setText("Відсоток плагіату: N/A");
                else {
                    String[] split = plagiatMeter.getText().split(": ");
                    int value = Integer.parseInt(split[1].replace("%.", ""));

                    plagiatMeter.setText("Відсоток плагіату: " + value + "%.");
                }

                language.setText("UA");
            }
            case "UA" -> {
                file.setText("Файл");
                compare.setText("Сравнить");
                open.setText("Выбрать");

                about.setText("О программе");
                suspected.setText("Подозреваемый");

                if (multiCheck.isSelected()) check.setText("Папка");
                else check.setText("Переверяющий");

                exit.setText("Выход");
                run.setText("Запуск");

                multiCheck.setText("Мульти Проверка");
                debug.setText("Дебаг");

                if (coincidenceLines.getText().contains("N/A"))
                    coincidenceLines.setText("Совпавшие строки: N/A");
                else {
                    String[] split = coincidenceLines.getText().split(": ");
                    int value = Integer.parseInt(split[1]);

                    coincidenceLines.setText("Совпавшие строки: " + value);
                }

                if (plagiatMeter.getText().contains("N/A")) plagiatMeter.setText("Процент плагиата: N/A");
                else {
                    String[] split = plagiatMeter.getText().split(": ");
                    int value = Integer.parseInt(split[1].replace("%.", ""));

                    plagiatMeter.setText("Процент плагиата: " + value + "%.");
                }

                language.setText("RU");
            }
            case "RU" -> {
                file.setText("File");
                compare.setText("Compare");
                open.setText("Open");

                about.setText("About");
                suspected.setText("Suspected");

                if (multiCheck.isSelected()) check.setText("Directory");
                else check.setText("Check");

                exit.setText("Exit");
                run.setText("Run");

                multiCheck.setText("Multi Check");
                debug.setText("Debug");

                if (coincidenceLines.getText().contains("N/A"))
                    coincidenceLines.setText("Coincidence lines: N/A");
                else {
                    String[] split = coincidenceLines.getText().split(": ");
                    int value = Integer.parseInt(split[1]);

                    coincidenceLines.setText("Coincidence lines: " + value);
                }

                if (plagiatMeter.getText().contains("N/A")) plagiatMeter.setText("Plagiat meter: N/A");
                else {
                    String[] split = plagiatMeter.getText().split(": ");
                    int value = Integer.parseInt(split[1].replace("%.", ""));

                    plagiatMeter.setText("Plagiat meter: " + value + "%.");
                }

                language.setText("EN");
            }
        }
    }

    public static void prepareLang(String lang) {
        switch (lang) {
            case "EN" -> language.setText("RU");
            case "UA" -> language.setText("EN");
            case "RU" -> language.setText("UA");
        }
    }

    private void about(ActionEvent actionEvent) {
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("assets/fallout.png"));
        icon = new ImageIcon(icon.getImage().getScaledInstance(150, 120, Image.SCALE_DEFAULT));

        switch (language.getText()) {
            case "EN" -> JOptionPane.showMessageDialog(null,
                    """
                            Anti Plagiarism - its a free and stand-alone software for searching
                            duplicate textual content in documents. It can be used to
                            determine if students have copied someone else's prose, and writers
                            use it to see if others are using their copyrighted work in full
                            or in part. Software was made by Java Developer Eureka.
                            v1.0"""
                    , "About", JOptionPane.INFORMATION_MESSAGE, icon);
            case "UA" -> JOptionPane.showMessageDialog(null,
                    """
                            Anti Plagiarism - це безкоштовне програмне забезпечення для пошуку
                            дублювання текстового вмісту в документах. Цей додаток можна
                            використовувати для визначення, чи переписували учні чужу прозу,
                            та чи повністю використовують інші свої роботи, захищені авторським правом
                            або частково. Програмне забезпечення розробив Java-розробник Eureka.
                            v1.0"""
                    , "Про програму", JOptionPane.INFORMATION_MESSAGE, icon);
            case "RU" -> JOptionPane.showMessageDialog(null,
                    """
                            Anti Plagiarism – это бесплатное программное обеспечение для поиска
                            дублирование текстового содержимого в документах. Это приложение можно
                            использовать для определения, переписывали ли ученики чужую прозу,
                            и полностью ли используют другие работы, защищенные авторским правом
                            или частично. Программное обеспечение разработал Java-разработчик Eureka.
                            v1.0"""
                    , "О программе", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }
}
