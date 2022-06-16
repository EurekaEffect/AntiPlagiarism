package eureka.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Transfer {
    public String getInOneLine(List<String> list) {
        StringBuilder document = new StringBuilder();

        for (String line : list) {
            document.append(" ").append(line);
        }

        return document.toString();
    }

    public List<String> readDocX(File file) {
        XWPFDocument doc;
        try {
            doc = new XWPFDocument(Files.newInputStream(file.toPath()));
        } catch (IOException e) {throw new RuntimeException(e);}

        List<XWPFParagraph> list = doc.getParagraphs();
        List<String> document = new ArrayList<>();

        for (XWPFParagraph paragraph : list) {
            if (paragraph.getText().isBlank()) continue;
            document.add(paragraph.getText());
        }

        return document;
    }
}
