import java.io.*;
import java.util.*;

class FileProcessingThread extends Thread {
    private String fileName;
    private String substring;
    private int idThreads;
    private File file;
    private final String fileExtension;
    private List<Integer> occurrenceLines;
    private int occurrenceCounter = 0;
    private final List<String> lines;
    private final int threadPerFile;

    public FileProcessingThread(String fileName, String substring, int idThreads, File file, String fileExtension, int threadPerFile) {
        this.fileName = fileName;
        this.substring = substring;
        this.idThreads = idThreads;
        this.file = file;
        this.fileExtension = fileExtension;
        this.threadPerFile = threadPerFile;
        this.occurrenceLines = new ArrayList<>();
        this.lines = new ArrayList<>();
    }

    public void preProcessing(){
        if (fileExtension.equals("txt")) {

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;

                while ((line = reader.readLine()) != null) {
                    lines.add(line);
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
                Collections.addAll(lines, fileContentByLines);

                inputStream.close();
                input.close();
            } catch (FileNotFoundException e) {
                System.out.println("Erro na procura do arquivo: " + fileName);
            } catch (IOException e) {
                System.out.println("Erro ao ler o arquivo: " + fileName);
            }
        }
    }

    @Override
    public void run() {
        int lineNumber = 1;
        for (int i = idThreads%threadPerFile; i < lines.size(); i += threadPerFile) {
            int index = lines.get(i).toLowerCase().indexOf(substring.toLowerCase());
            while (index != -1) {
                if (!occurrenceLines.contains(lineNumber)) {
                    occurrenceLines.add(lineNumber);
                }
                occurrenceCounter++;
                index = lines.get(i).toLowerCase().indexOf(substring.toLowerCase(), index + 1);
            }
            lineNumber = i + 1; // Incrementar o número da linha
        }
    }

    public synchronized void checkOccurrences(){
        int separatorIndex = fileName.lastIndexOf("/");
        //System.out.println("Thread de ID " + idThreads + " executou arquivo " + fileName.substring(separatorIndex + 1));

        System.out.println("O arquivo ["+fileName.substring(separatorIndex + 1)+"] possui " + file.length() + " bytes");

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
        Scanner input = new Scanner(System.in);

        //User insert necessary values: substring to search, number of files, thread per file, paths
        System.out.println("Digite a palavra a ser procurada:");
        String substring = input.nextLine();

        System.out.println("Digite o número de arquivos a serem processados:");
        int numberOfFiles;
        while(true){
            try {
                numberOfFiles = input.nextInt();
                break;
            } catch (InputMismatchException e){
                System.out.println("Por favor, digite um número inteiro:");
                input.next();
            }
        }

        System.out.println("Digite o número de threads para cada arquivo:");
        int threadPerFile;
        while(true){
            try {
                threadPerFile = input.nextInt();
                break;
            } catch (InputMismatchException e){
                System.out.println("Por favor, digite um número inteiro:");
                input.next();
            }
        }
        int totalThreadNumber = numberOfFiles * threadPerFile;
        FileProcessingThread[] threads = new FileProcessingThread[totalThreadNumber];

        System.out.println("Digite o path completo para o arquivo a ser processado, ou o path do arquivo na pasta Arquivos (src/Arquivos/nomedoarquivo.extensao):");
        String[] fileNames = new String[numberOfFiles];
        for(int i=0; i<numberOfFiles; i++){
            System.out.print("Arquivos "+(i+1)+": ");
            fileNames[i] = input.next();
        }
        System.out.println();

        input.close();

        //Get current time in milliseconds to check at the end
        long startTime = System.currentTimeMillis();

        //Create file processing threads with extension check
        for (int i = 0; i < totalThreadNumber; i++) {

            int fileNameIndex = i/threadPerFile;
            //System.out.println(fileNameIndex);
            String extension = fileNames[fileNameIndex].substring(fileNames[fileNameIndex].lastIndexOf(".")+1);

            if(extension.equals("txt") || extension.equals("bin")) {
                File file = new File(fileNames[fileNameIndex]);
                threads[i] = new FileProcessingThread(fileNames[fileNameIndex], substring, i, file,  extension, threadPerFile);
            }
            else {
                System.out.println("Erro na Thread ID "+i+": Arquivo ["
                                    +fileNames[fileNameIndex].substring(fileNames[fileNameIndex].lastIndexOf("/")+1)+
                                    "] do tipo ["+extension+"] sem suporte. Utilize [txt] ou [bin]\n");
                threads[i] = null;
            }
        }

        for (int i = 0; i < totalThreadNumber; i++) {
            if(threads[i]==null) continue;
            threads[i].preProcessing();
        }

        for (int i = 0; i < totalThreadNumber; i++) {
            if(threads[i]==null) continue;
            threads[i].start();
        }

        for (int i = 0; i < totalThreadNumber; i++) {
            if(threads[i]==null) continue;
            threads[i].join();
        }

        int currentFile = -1;
        for (int i = 0; i < totalThreadNumber; i ++) {
            if(threads[i]==null) continue;
            //threads[i].checkOccurrences();
            ///*
            if(threads[i].getIdThreads()/threadPerFile != currentFile){
                currentFile = threads[i].getIdThreads();
                threads[i].checkOccurrences();
            //*/
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("A busca demorou " + duration + " ms");
    }
}
