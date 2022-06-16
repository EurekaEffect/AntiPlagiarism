package eureka.utils;

import eureka.gui.Gui;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Output {
    private BufferedWriter
            outputWriter,
            configWriter,
            debugWriter;

    private List<String>
            outputList = new ArrayList<>(),
            configList = new ArrayList<>(),
            debugList = new ArrayList<>();

    public void init() throws IOException {
        String path = "data/";

        outputList = new BufferedReader(new FileReader(path + "output.txt")).lines().toList();
        configList = new BufferedReader(new FileReader(path + "config.txt")).lines().toList();
        debugList = new BufferedReader(new FileReader(path + "debug.txt")).lines().toList();

        outputWriter = new BufferedWriter(new FileWriter(path + "output.txt"));
        configWriter = new BufferedWriter(new FileWriter(path + "config.txt"));
        debugWriter = new BufferedWriter(new FileWriter(path + "debug.txt"));

        loadAll();
    }

    public void loadAll() {
        for (String text : outputList) write("output.txt", text);
        for (String text : debugList) write("debug.txt", text);
    }

    public void lineSeparator(File suspected, File check) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        write("output.txt", "~  ~  ~  ~  ~  ~  ~");
        write("output.txt", " " + formatter.format(date));
        write("output.txt", " " + suspected.getName() + "/" + check.getName());
    }

    public void write(String file, String text) {
        text += System.lineSeparator();

        try {
            switch (file) {
                case "output.txt" -> outputWriter.write(text);
                case "config.txt" -> configWriter.write(text);
                case "debug.txtd" -> debugWriter.write(text);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() {
        if (configList.isEmpty()) return;

        Gui.debug.setState(configList.get(0).split(":")[1].equals("true"));
        Gui.language.setText(configList.get(1).split(":")[1]);
        Gui.multiCheck.setState(configList.get(2).split(":")[1].equals("true"));

        Gui.prepareLang(configList.get(1).split(":")[1]);
        Gui.setLanguage();
    }

    public void saveConfig() {
        write("config.txt", "debug:" + Gui.debug.isSelected());
        write("config.txt", "lang:" + Gui.language.getText());
        write("config.txt", "multicheck:" + Gui.multiCheck.isSelected());
    }

    public void close() {
        try {
            saveConfig();
            outputWriter.close();
            configWriter.close();
            debugWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
