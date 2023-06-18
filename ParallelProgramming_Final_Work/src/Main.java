import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class FileProcessingThread extends Thread {
    private String fileName;
    private String substring;
    private List<Integer> occurrenceLines;

    private int idThreads;

    public FileProcessingThread(String fileName, String substring, int idThreads) {
        this.fileName = fileName;
        this.substring = substring;
        this.idThreads = idThreads;
        this.occurrenceLines = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("Thread " + idThreads + " processando arquivo: " + fileName);

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

        System.out.println("Thread " + idThreads + " finalizou o processamento do arquivo: " + fileName);
        if (!occurrenceLines.isEmpty()) {
            System.out.println("Ocorrências da substring '" + substring + "' no arquivo '" + fileName + "':");
            for (int line : occurrenceLines) {
                System.out.println("Linha " + line);
            }
        } else {
            System.out.println("A substring '" + substring + "' não foi encontrada no arquivo '" + fileName + "'.");
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSubstring() {
        return substring;
    }

    public void setSubstring(String substring) {
        this.substring = substring;
    }

    public List<Integer> getOccurrenceLines() {
        return occurrenceLines;
    }

    public void setOccurrenceLines(List<Integer> occurrenceLines) {
        this.occurrenceLines = occurrenceLines;
    }

    public int getIdThreads() {
        return idThreads;
    }

    public void setIdThreads(int idThreads) {
        this.idThreads = idThreads;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int sizeThreads = 3;

        FileProcessingThread[] threads = new FileProcessingThread[sizeThreads];

        //String path = "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivo"
        String[] fileNames = {"C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivos/arquivo1.txt", "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivos/arquivo2.txt", "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivos/arquivo3.txt"};
        String substring = "exemplo";

        for (int i = 0; i < sizeThreads; i++) {
            threads[i] = new FileProcessingThread(fileNames[i], substring, i);
            threads[i].start();
        }

        for (int i = 0; i < sizeThreads; i++) {
            threads[i].join();
        }
    }
}
