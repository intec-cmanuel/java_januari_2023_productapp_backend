package be.intecbrussel.productapp.logger;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileLogger {
    public String log(String log) {
        String logFileName = getLogFileTitle();
        String finalLogLine = getFinalLogLine(log);
        String path = "src/main/resources/logs/";
        boolean success = sendToFile(path, logFileName, finalLogLine);
        if (success) return finalLogLine;
        else return "";
    }

    public String logException(Throwable e) {
        String logFileName = getExceptionLogFileTitle();
        String exceptionLogLine = getExceptionLogLine(e);
        String path = "src/main/resources/logs/";
        boolean success = sendToFile(path, logFileName, exceptionLogLine);
        if (success) return exceptionLogLine;
        else return "";
    }

    private String getFinalLogLine(String log) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        return "[" + formattedDateTime + "] : " + log;
    }

    private String getExceptionLogLine(Throwable exception) {
        List<String> lines = new ArrayList<>();
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        lines.add("[" + formattedDateTime + "] : EXCEPTION: " + exception.getMessage());
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            String line = stackTraceElement.toString();
            String finalLine = "[" + formattedDateTime + "] : " + line;
            lines.add(finalLine);
        }

        return lines.stream().reduce("", (acc, e) ->  acc + e + "\n");
    }

    private String getLogFileTitle() {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return "log-" + formattedDate + ".txt";
    }

    private String getExceptionLogFileTitle() {
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return "exception-" + formattedDate + ".txt";
    }

    private boolean sendToFile(String directoryPath, String fileName, String log) {
        String pathString = directoryPath+fileName; // Specify the file name or path

        try {
            Path path = Paths.get(pathString);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            FileWriter fileWriter = new FileWriter(path.toFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(log);
            bufferedWriter.newLine(); // Optionally add a newline

            bufferedWriter.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
