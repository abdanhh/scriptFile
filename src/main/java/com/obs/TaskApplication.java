package com.obs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TaskApplication {
    private static Logger logger = LoggerFactory.getLogger(TaskApplication.class);
    private static String DIRECTORY = "src/main/resources/files/";
    private static String PATH = "src/main/resources/Script.bat";

    public static void main(String[] args) throws IOException{
            createFile();
    }

    public static File createFile() throws IOException {
        File file = new File(PATH);
        if (file.createNewFile()){
            logger.info("Success create a file.");
            readAndWriteFile();
            return file;
        } else {
            logger.error("File Already Exists");
        }
        return null;
    }

    public static List<File> getListFiles(){
        File directory = new File(DIRECTORY);
        if (directory.isDirectory()){
            File[] listFiles = directory.listFiles();
            return Arrays.stream(listFiles).filter(file -> file.getName().endsWith(".sql")).collect(Collectors.toList());
        } else {
            logger.error("Directory Not Found");
        }
        return null;
    }

    public static void readAndWriteFile() throws IOException {
        List<String> listSuccessFiles = new ArrayList<>();
        List<String> listFailedFiles = new ArrayList<>();

        FileWriter fileWriter = new FileWriter("src/main/resources/Script.bat");

        for (File file : getListFiles()) {
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
        fileWriter.close();
        logger.info("Success close file writer");
        System.out.println("Success: " + listSuccessFiles.stream().collect(Collectors.joining(", ")));
        System.out.println("Failed: " + listFailedFiles.stream().collect(Collectors.joining(", ")));

    }
}
