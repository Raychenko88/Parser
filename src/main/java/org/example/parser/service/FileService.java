package org.example.parser.service;

import org.example.parser.model.Item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileService {

    private static final String FILE_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "files";

    public static void saveItemToScvFile(List<Item> items, String fileName) {
        // check dir
        File fileDir = new File(FILE_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String filePath = FILE_DIR + System.getProperty("file.separator") + fileName;

        try (FileWriter fileWriter = new FileWriter(filePath)){
            for (Item item : items) {
                fileWriter.write(item.toString());
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
