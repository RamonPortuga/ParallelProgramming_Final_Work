import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class FileProcessingThread extends Thread {
    private String fileName;
    private String substring;
    private List<Integer> occurrenceLines;
    private int idThreads;

    private final String fileExtension;

    public FileProcessingThread(String fileName, String substring, int idThreads, String fileExtension) {
        this.fileName = fileName;
        this.substring = substring;
        this.idThreads = idThreads;
        this.occurrenceLines = new ArrayList<>();
        this.fileExtension = fileExtension;
    }

    @Override
    public void run() {
        //System.out.println("Thread " + idThreads + " processando arquivo: " + fileName);

        if (fileExtension.equals("txt")) {
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
        }

        else if (fileExtension.equals("bin")) {
            try {
                FileInputStream inputStream = new FileInputStream(fileName);
                DataInputStream input = new DataInputStream (inputStream);

                String[] fileContentByLines = input.readUTF().split("\n");
                int lineNumber = 1;
                for (String fileContentByLine : fileContentByLines) {
                    if (fileContentByLine.contains(substring)) {
                        occurrenceLines.add(lineNumber);
                        //System.out.println(idThreads + ": " + fileContentByLines[i]);
                    }
                    lineNumber++;
                }

                inputStream.close();
                input.close();

            } catch (FileNotFoundException e) {
                System.out.println("Erro na procura do arquivo: " + fileName);
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo: " + fileName);
            }
        }

    }

    public void checkOccurrences(){
        int separatorIndex = fileName.lastIndexOf("/");
        System.out.println("Thread de Id " + idThreads + " executou arquivo " + fileName.substring(separatorIndex + 1));
        //System.out.println("Thread " + idThreads + " finalizou o processamento do arquivo: " + fileName);
        if (!occurrenceLines.isEmpty()) {
            for (int line : occurrenceLines) {
                System.out.println("Linha " + line);
            }
        } else {
            System.out.println("A substring '" + substring + "' não foi encontrada no arquivo '" + fileName + "'.");
        }
        System.out.print("\n\n");
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

class WriteBinaryFile {

    public WriteBinaryFile() {}

    public  void createBinFile() throws IOException    {

        //"C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivo[arquivo desejado]"
        //"C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/[arquivo desejado]"

        File outFile = new File ("C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste.bin");
        FileOutputStream outStream = new FileOutputStream (outFile);

        //arquivo1 = "exemplo\n" + "exemplo\n" + "exemplo\n" + "exempl\n" + "exemplo\n" + "exem";
        //arquivo 2 = "banana\n" + "banana\n" + "exemplo exemplo exemplo\n" + "banana\n" + "exemplo";
        //arquivo3 = "exemplo exemplo exemplo exemplo";

        try (outStream) {
            DataOutputStream output = new DataOutputStream(outStream);
            String name = "escreva dados aqui";
            output.writeUTF(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        outStream.close();

    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException{
        int sizeThreads = 3;

        FileProcessingThread[] threads = new FileProcessingThread[sizeThreads];

        //String path = "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivo"
        //String path = "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos"
        //String[] fileNames = {"C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivos/arquivo1.txt", "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivos/arquivo2.txt", "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivos/arquivo3.txt"};
        String[] fileNames = {"C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/arquivo1.txt", "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/arquivo2.txt", "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/arquivo3.txt"};
        //String[] fileNames = {"C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste1.bin", "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste2.bin", "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste3.bin"};

        Scanner input = new Scanner(System.in);
        System.out.println("Entre com a palavra a ser procurada: ");
        String substring = input.nextLine();
        //String substring = "exemplo";

        //WriteBinaryFile writer = new WriteBinaryFile();
        //writer.createBinFile();

        for (int i = 0; i < sizeThreads; i++) {
            String extension = fileNames[i].substring(fileNames[i].lastIndexOf(".")+1);
            //String extension = "jpeg";
            if(extension.equals("txt") || extension.equals("bin")) {
                threads[i] = new FileProcessingThread(fileNames[i], substring, i, extension);
                threads[i].start();
            }
            else {
                System.out.println("Arquivo do tipo ["+extension+"] sem suporte. Utilize [txt] ou [bin]");
                System.exit(-1);
            }
        }

        for (int i = 0; i < sizeThreads; i++) {
            System.out.println(threads.length);
            threads[i].join();
        }

        for (int i = 0; i < sizeThreads; i++) {
            threads[i].checkOccurrences();
        }

    }
}
