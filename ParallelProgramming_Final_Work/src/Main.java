import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileProcessingThread extends Thread {
    private String fileName;
    private String substring;
    private List<Integer> occurrenceLines;

    public FileProcessingThread(String fileName, String substring) {
        this.fileName = fileName;
        this.substring = substring;
        this.occurrenceLines = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("Thread " + Thread.currentThread().getId() + " processando arquivo: " + fileName);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (line.contains(substring)) {
                    occurrenceLines.add(lineNumber);
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + fileName);
        }

        System.out.println("Thread " + Thread.currentThread().getId() + " finalizou o processamento do arquivo: " + fileName);
        if (!occurrenceLines.isEmpty()) {
            System.out.println("Ocorrências da substring '" + substring + "' no arquivo '" + fileName + "':");
            for (int line : occurrenceLines) {
                System.out.println("Linha " + line);
            }
        } else {
            System.out.println("A substring '" + substring + "' não foi encontrada no arquivo '" + fileName + "'.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String[] fileNames = { "C:/Users/ramon/IdeaProjects/ParallelProgramming_Final_Work/src/arquivo1.txt", "C:/Users/ramon/IdeaProjects/ParallelProgramming_Final_Work/src/arquivo2.txt", "C:/Users/ramon/IdeaProjects/ParallelProgramming_Final_Work/src/arquivo3.txt" };
        String substring = "exemplo";

        for (String fileName : fileNames) {
            FileProcessingThread thread = new FileProcessingThread(fileName, substring);
            thread.start();
        }
    }
}
