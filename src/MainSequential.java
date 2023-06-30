import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

//File Processor Class
class FileProcessingSequential {
    private String fileName;
    private String substring;
    private List<Integer> occurrenceLines;
    private int fileNumber;
    private File file;
    private final String fileExtension;
    private final List<String> fileContentByLines;
    private int occurrenceCounter = 0;
    private final Map<Integer,Integer> occurrencePerLineMap;
    private int occurrencePerLineCounter = 0;

    //Constructor of File processor
    public FileProcessingSequential(String fileName, String substring, int fileNumber, File file, String fileExtension, List<String> fileContentByLines) {
        this.fileName = fileName;
        this.substring = substring;
        this.fileNumber = fileNumber;
        this.file = file;
        this.occurrenceLines = new ArrayList<>();
        this.fileExtension = fileExtension;
        this.fileContentByLines = fileContentByLines;
        this.occurrencePerLineMap = new HashMap<>();
    }

    //Iterating by line and checking substring presence, records total occurrence number and occurrence per line
    public void executeSearch() {

        for (int i = 0; i < fileContentByLines.size(); i += 1) {
            int index = fileContentByLines.get(i).toLowerCase().indexOf(substring.toLowerCase());
            while (index != -1) {
                if (!occurrenceLines.contains(i)) {
                    occurrenceLines.add(i);
                }
                occurrenceCounter++;
                occurrencePerLineCounter++;
                index = fileContentByLines.get(i).toLowerCase().indexOf(substring.toLowerCase(), index + 1);
            }
            occurrencePerLineMap.put(i, occurrencePerLineCounter);
            occurrencePerLineCounter=0;
        }
    }

    //Print total occurrences, occurrences per line and byte size
    public void checkOccurrences(){
        int separatorIndex = fileName.lastIndexOf("/");
        System.out.println("Arquivo número " + (fileNumber+1) + " denominado [" + fileName.substring(separatorIndex + 1)+"] foi executado!");

        System.out.println("O arquivo possui " + file.length() + " bytes");

        if (!occurrenceLines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("A substring ").append(substring).append(" foi encontrada ");
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
                System.out.println("Linha "+(line+1) + ": "+occurrencePerLineMap.get(line)+" ocorrências.");
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

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
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

//Class used to make bin files for testing
/*
class WriteBinaryFileSequential {

    public WriteBinaryFileSequential() {}

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
 */

//Main Class
public class MainSequential {
    public static void main(String[] args) {

        //User insert necessary values: substring to search, number of files, thread per file, paths
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite a palavra a ser procurada:");
        String substring = scanner.nextLine();

        System.out.println("Digite o número de arquivos a serem processados:");
        int numberOfFiles;
        while(true){
            try {
                numberOfFiles = scanner.nextInt();
                break;
            } catch (InputMismatchException e){
                System.out.println("Por favor, digite um número inteiro:");
                scanner.next();
            }
        }
        FileProcessingSequential[] files = new FileProcessingSequential[numberOfFiles];

        System.out.println("Digite o path completo para o arquivo a ser processado, ou o path do arquivo na pasta Arquivos (src/Arquivos/nomedoarquivo.extensao):");

        String[] fileNames = new String[numberOfFiles];
        for(int i=0; i<numberOfFiles; i++){
            System.out.print("Arquivos "+(i+1)+": ");
            fileNames[i] = scanner.next();
        }
        System.out.println();

        /*
        String[] fileNames = {  "src/Arquivos/arquivo1.txt",
                                "src/Arquivos/arquivo2.txt",
                                "src/Arquivos/arquivo3.txt",
                                "src/Arquivos/arquivo4.txt",
                                "src/Arquivos/arquivo5.txt",
                                "src/Arquivos/arquivo6.txt"};
        scanner.close();
        */

        //Pre-processing of target files with extension check
        long startTimeReading = System.currentTimeMillis();
        List<List<String>> fileContentByLines = new ArrayList<>();
        for (int i = 0; i < numberOfFiles; i++){

            String extension = fileNames[i].substring(fileNames[i].lastIndexOf(".")+1);

            if(extension.equals("bin")) {

                try {
                    FileInputStream inputStream = new FileInputStream(fileNames[i]);
                    DataInputStream input = new DataInputStream (inputStream);

                    fileContentByLines.add(List.of(input.readUTF().split("\n")));

                    inputStream.close();
                    input.close();
                } catch (IOException e) {
                    System.out.println("Erro ao ler o arquivo: " + fileNames[i]);
                }

            } else if (extension.equals("txt")) {

                Path filePath = Paths.get(fileNames[i]);
                try {
                    fileContentByLines.add(Files.readAllLines(filePath));
                } catch (IOException e) {
                    System.out.println("Erro ao ler o arquivo: " + fileNames[i]);
                }

            } else fileContentByLines.add(null);
        }
        long endTimeReading = System.currentTimeMillis();
        long durationTimeReading = endTimeReading - startTimeReading;
        System.out.println("\nO pré processamento demorou " + durationTimeReading + " ms");

        //Create file processing objects with extension check, ending the program in case of failure
        long startTimeProcessing = System.currentTimeMillis();
        for (int i = 0; i < numberOfFiles; i++) {
            String extension = fileNames[i].substring(fileNames[i].lastIndexOf(".") + 1);

            if (extension.equals("txt") || extension.equals("bin")) {
                File file = new File(fileNames[i]);
                files[i] = new FileProcessingSequential(fileNames[i], substring, i, file, extension, fileContentByLines.get(i));
            } else {
                System.out.println("Arquivo do tipo [" + extension + "] sem suporte. Utilize [txt] ou [bin]");
                System.exit(-1);
            }
        }

        //Search files for occurrences of substring
        for (int i = 0; i < numberOfFiles; i++) {
            files[i].executeSearch();
        }

        long endTimeProcessing = System.currentTimeMillis();
        long durationTimeProcessing = endTimeProcessing - startTimeProcessing;
        System.out.println("\nA busca demorou " + durationTimeProcessing + " ms");

        System.out.println("O tempo de execução total foi "+(durationTimeProcessing+durationTimeReading)+" ms");

        //Prints search results of each file once
        for (int i = 0; i < numberOfFiles; i++) {
            int separatorIndex = files[i].getFileName().lastIndexOf("/");
            System.out.println("\nO arquivo ["+files[i].getFileName().substring(separatorIndex + 1)
                        +"] possui "+files[i].getFile().length() + " bytes");
            files[i].checkOccurrences();
        }

    }
}
