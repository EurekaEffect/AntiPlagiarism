package eureka.utils;

import eureka.Main;
import eureka.gui.Gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

public class Compare {
    public void compare(File suspectedFile, File checkFile) throws FileNotFoundException {
        if (Gui.multiCheck.isSelected()) {
            for (File file : Objects.requireNonNull(checkFile.listFiles())) {
                if (!file.getName().endsWith(".docx")) continue;

                List<String>
                        suspected = Main.transfer.readDocX(suspectedFile),
                        check = Main.transfer.readDocX(file);
                String oneLine = Main.transfer.getInOneLine(check);

                int coincidence = process(suspected, oneLine);
                int percentage = (coincidence * 100) / suspected.size();

                comparedText(coincidence, percentage, true, suspectedFile, file);
            }
        } else {
            List<String>
                    suspected = Main.transfer.readDocX(suspectedFile),
                    check = Main.transfer.readDocX(checkFile);
            String oneLine = Main.transfer.getInOneLine(check);

            int coincidence = process(suspected, oneLine);
            int percentage = (coincidence * 100) / suspected.size();

            comparedText(coincidence, percentage, false, suspectedFile, checkFile);
        }
    }

    private int process(List<String> suspected, String oneLine) {
        int coincidence = 0;

        for (String susLine : suspected) {
            if (susLine.isBlank()) continue;
            if (contains(susLine, oneLine)) coincidence++;
        }

        return coincidence;
    }

    private boolean contains(String suspected, String check) {
        if (check.contains(suspected)) return true;

        suspected = suspected.replace("а", "a");
        return check.contains(suspected);
    }

    private void comparedText(int coincidence, int percentage, boolean multicheck, File suspected, File check) {
        Main.output.lineSeparator(suspected, check);

        switch (Gui.language.getText()) {
            case "EN" -> {
                if (!multicheck) {
                    Gui.coincidenceLines.setText("Coincidence lines: " + coincidence);
                    Gui.plagiatMeter.setText("Plagiat meter: " + percentage + "%.");
                }

                Main.output.write("output.txt", " Coincidence lines: " + coincidence);
                Main.output.write("output.txt", " Plagiat meter: " + percentage + "%.");
            }
            case "UA" -> {
                if (!multicheck) {
                    Gui.coincidenceLines.setText("Співпалі лінії: " + coincidence);
                    Gui.plagiatMeter.setText("Відсоток плагіату: " + percentage + "%.");
                }

                Main.output.write("output.txt", " Співпалі лінії: " + coincidence);
                Main.output.write("output.txt", " Відсоток плагіату: " + percentage + "%.");
            }
            case "RU" -> {
                if (!multicheck) {
                    Gui.coincidenceLines.setText("Совпавшие строки: " + coincidence);
                    Gui.plagiatMeter.setText("Процент плагиата: " + percentage + "%.");
                }

                Main.output.write("output.txt", " Совпавшие строки: " + coincidence);
                Main.output.write("output.txt", " Процент плагиата: " + percentage + "%.");
            }
        }
    }
}
