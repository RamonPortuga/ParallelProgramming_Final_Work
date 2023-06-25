import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class FileProcessingThread extends Thread {
    private String fileName;
    private String substring;
    private List<Integer> occurrenceLines;
    private int idThreads;
    private File file;
    private final String fileExtension;

    private int occurrenceCounter = 0;

    public FileProcessingThread(String fileName, String substring, int idThreads, File file, String fileExtension) {
        this.fileName = fileName;
        this.substring = substring;
        this.idThreads = idThreads;
        this.file = file;
        this.occurrenceLines = new ArrayList<>();
        this.fileExtension = fileExtension;
    }

    @Override
    public void run() {

        if (fileExtension.equals("txt")) {

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                int lineNumber = 1;

                while ((line = reader.readLine()) != null) {
                    int index = line.toLowerCase().indexOf(substring.toLowerCase());
                    while (index != -1) {
                        if(occurrenceLines.isEmpty()){
                            occurrenceLines.add(lineNumber);
                        }
                        else if (occurrenceLines.get(occurrenceLines.size() - 1) != lineNumber){
                            occurrenceLines.add(lineNumber);
                        }
                        occurrenceCounter++;
                        index = line.toLowerCase().indexOf(substring.toLowerCase(), index + 1);
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
                    int index = fileContentByLine.toLowerCase().indexOf(substring.toLowerCase());
                    while (index != -1) {
                        if(occurrenceLines.isEmpty()){
                            occurrenceLines.add(lineNumber);
                        }
                        else if (occurrenceLines.get(occurrenceLines.size() - 1) != lineNumber){
                            occurrenceLines.add(lineNumber);
                        }
                        occurrenceCounter++; // Incrementa o contador de ocorrências
                        index = fileContentByLine.toLowerCase().indexOf(substring.toLowerCase(), index + 1);
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

    public synchronized void checkOccurrences(){
        int separatorIndex = fileName.lastIndexOf("/");
        System.out.println("Thread de ID " + idThreads + " executou arquivo " + fileName.substring(separatorIndex + 1));

        System.out.println("O arquivo possui " + file.length() + " bytes");

        if (!occurrenceLines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("A substring [").append(substring).append("] foi encontrada ");
            if(occurrenceCounter == 1){
                sb.append(occurrenceCounter).append(" vez");
            }
            else{
                sb.append(occurrenceCounter).append(" vezes");
            }
            if(occurrenceLines.size() == 1){
                sb.append(" na seguinte linha");
            }
            else{
                sb.append(" nas seguintes linhas");
            }
            System.out.println(sb);
            for (int line : occurrenceLines) {
                System.out.print(line + " ");
            }
        } else {
            System.out.println("A substring '" + substring + "' não foi encontrada no arquivo '" + fileName.substring(separatorIndex + 1) + "'.");
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public int getOccurrenceCounter() {
        return occurrenceCounter;
    }

    public void setOccurrenceCounter(int occurrenceCounter) {
        this.occurrenceCounter = occurrenceCounter;
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
            String name = "Insira dados aqui";
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

        /*
        String[] fileNames = {
                "C:/Users/Vitória Nazareth/Desktop/COMP_CON/ParallelProgramming_Final_Work/src/Arquivos/arquivo1.txt",
                "C:/Users/Vitória Nazareth/Desktop/COMP_CON/ParallelProgramming_Final_Work/src/Arquivos/arquivo2.txt",
                "C:/Users/Vitória Nazareth/Desktop/COMP_CON/ParallelProgramming_Final_Work/src/Arquivos/arquivo3.txt",
                "C:/Users/Vitória Nazareth/Desktop/COMP_CON/ParallelProgramming_Final_Work/src/Arquivos/arquivo4.txt"
        };
        */
        //String path = "C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivo"
        //String path = "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos"

        //PARA ARQUIVOS TXT
        /*
        String[] fileNames = {
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/arquivo1.txt",
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/arquivo2.txt",
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/arquivo3.txt",
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/arquivo4.txt"
        };
        */

        //PARA ARQUIVOS BIN
        /*
        String[] fileNames = {
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/teste1.bin",
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/teste2.bin",
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/teste3.bin",
                "C:/Users/ramon/Documents/UFRJ/ProgConc/ParallelProgramming_Final_Work/src/Arquivos/teste4.bin"
        };
         */

        /*
        String[] fileNames = {
                "src/Arquivos/arquivo1.txt",
                "src/Arquivos/arquivo2.txt",
                "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/arquivo3.txt",
                "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/arquivo4.txt"
        };
        */
        /*
        String[] fileNames = {
                "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste1.bin",
                "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste2.bin",
                "C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste3.bin"
        };
        */

        Scanner input = new Scanner(System.in);
        System.out.println("Digite a palavra a ser procurada:");
        String substring = input.nextLine();

        System.out.println("Digite o número de arquivos a serem processados:");
        boolean rightType = false;
        int sizeThreads = 0;
        while(!rightType){
            try {
                sizeThreads = input.nextInt();
                rightType = true;
            } catch (InputMismatchException e){
                System.out.println("Por favor, digite um número inteiro:");
                input.next();
            }
        }
        FileProcessingThread[] threads = new FileProcessingThread[sizeThreads];

        String[] fileNames = new String[sizeThreads];
        System.out.println("Digite o path completo para o arquivo a ser processado, ou o path do arquivo na pasta Arquivos (src/Arquivos/nomedoarquivo.extensao):");
        for(int i=0; i<sizeThreads; i++){
            System.out.print("Arquivos "+(i+1)+": ");
            fileNames[i] = input.next();
        }
        System.out.println();
        input.close();

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < sizeThreads; i++) {
            String extension = fileNames[i].substring(fileNames[i].lastIndexOf(".")+1);

            if(extension.equals("txt") || extension.equals("bin")) {
                File file = new File(fileNames[i]);
                threads[i] = new FileProcessingThread(fileNames[i], substring, i, file,  extension);
                threads[i].start();
            }
            else {
                System.out.println("Arquivo do tipo ["+extension+"] sem suporte. Utilize [txt] ou [bin]");
                System.exit(-1);
            }
        }

        for (int i = 0; i < sizeThreads; i++) {
            threads[i].join();
        }

        for (int i = 0; i < sizeThreads; i++) {
            threads[i].checkOccurrences();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("A busca demorou " + duration + " ms");
    }
}
