package com.obs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.function.Predicate;

public class TaskApplication {
    public static Logger logger = LoggerFactory.getLogger(TaskApplication.class);

    public static void main(String[] args) {
        try {
                if (createFile()){
                    readAndWriteFiles();
                }
        } catch (IOException e){
            logger.info(e.getMessage());
            e.printStackTrace();
        }

    }

    private static boolean createFile() throws IOException {
        boolean result;
        File file = new File("src/main/resources/Script.bat");
        if (file.createNewFile()){
            logger.info("Success create a file.");
            result = true;
        } else {
            logger.warn("File already exists!");
            result = false;
        }
        return result;
    }

    private static void readAndWriteFiles() throws IOException {

        StringJoiner listSuccessFiles = new StringJoiner(", ");
        StringJoiner listFailedFiles = new StringJoiner(", ");

        String path = "src/main/resources/files/";
        FileWriter fileWriter = new FileWriter("src/main/resources/Script.bat");
        Predicate<String> fileExtension = file -> file.endsWith(".sql");
        Predicate<Integer> directoryValidation = dir -> dir > 5 &&  dir != null;

        File directory = new File(path);
        if (directory.isDirectory()) {
            File[] listFile = directory.listFiles();
            if (directoryValidation.test(listFile.length)) {
                for (File file : listFile) {
                    if (fileExtension.test(file.getName())) {
                        Scanner scanner = new Scanner(file);
                        if (scanner.hasNextLine()) {
                            String reader = scanner.nextLine();
                            if (!reader.contains("System.out")) {
                                String write = "sqlcmd -U %dbUser% -P %dbPassword% -d %dbName% -i " + "\"" + file.getName() + "\"" + "\n";
                                fileWriter.write(write);
                                logger.info("Success write to file. " + write);
                                listSuccessFiles.add(file.getName());
                            } else {
                                listFailedFiles.add(file.getName());
                                logger.warn("file " + file.getName() + " is excluded because contain PRINT statement.");
                            }
                        } else {
                            listFailedFiles.add(file.getName());
                            logger.warn("file " + file.getName() + " is empty");
                        }
                    }
                }
                fileWriter.close();
                logger.info("Success close file writer");
                System.out.println("Success: " + listSuccessFiles);
                System.out.println("Failed: " + listFailedFiles);
            } else {
                logger.error("directory is empty");
            }
        } else {
            logger.error("Path not found!");
        }
    }
}
