import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

class FileProcessingThread extends Thread {
    private String fileName;
    private String substring;
    private int idThreads;
    private File file;
    private final String fileExtension;
    private List<Integer> occurrenceLines;
    private int occurrenceCounter = 0;
    private final List<String> fileContentByLines;
    private final int threadPerFile;

    public FileProcessingThread(String fileName, String substring, int idThreads, File file, String fileExtension, int threadPerFile, List<String> fileContentByLines) {
        this.fileName = fileName;
        this.substring = substring;
        this.idThreads = idThreads;
        this.file = file;
        this.fileExtension = fileExtension;
        this.threadPerFile = threadPerFile;
        this.occurrenceLines = new ArrayList<>();
        this.fileContentByLines = fileContentByLines;
    }

    @Override
    public void run() {

        //Iterating by line and checking substring presence
        for (int i = idThreads%threadPerFile; i < fileContentByLines.size(); i += threadPerFile) {
            int index = fileContentByLines.get(i).toLowerCase().indexOf(substring.toLowerCase());
            while (index != -1) {
                if (!occurrenceLines.contains(i)) {
                    occurrenceLines.add(i);
                }
                occurrenceCounter++;
                index = fileContentByLines.get(i).toLowerCase().indexOf(substring.toLowerCase(), index + 1);
            }
        }
    }

    public void checkOccurrences(){

        int separatorIndex = fileName.lastIndexOf("/");
        if (!occurrenceLines.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Thread ID ").append(idThreads).append(": A substring [").append(substring).append("] foi encontrada ");
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
                System.out.print((line+1) + " ");
            }
        } else {
            if (threadPerFile==1) {
                System.out.println("Thread ID "+idThreads+": A substring '" + substring + "' não foi encontrada no arquivo '" + fileName.substring(separatorIndex + 1) + "'.");
            }
            else {
                System.out.println("Thread ID "+idThreads+": A substring '" + substring + "' não foi encontrada por essa thread.");
            }
        }
        System.out.print("\n");
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

/*
class WriteBinaryFile {

    public WriteBinaryFile() {}

    public  void createBinFile() throws IOException    {

        //"C:/Users/ramon/Documents/UFRJ/ProgConc/TrabalhoFinal/ParallelProgramming_Final_Work/src/Arquivo[arquivo desejado]"
        //"C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/[arquivo desejado]"

        File outFile = new File ("C:/Users/gabri/IdeaProjects/ParallelProgramming_Final_Work/src/Arquivos/teste1.bin");
        FileOutputStream outStream = new FileOutputStream (outFile);

        //arquivo1 = "exemplo\n" + "exemplo\n" + "exemplo\n" + "exempl\n" + "exemplo\n" + "exem";
        //arquivo 2 = "banana\n" + "banana\n" + "exemplo exemplo exemplo\n" + "banana\n" + "exemplo";
        //arquivo3 = "exemplo exemplo exemplo exemplo";

        try (outStream) {
            DataOutputStream output = new DataOutputStream(outStream);
            String teste1bin = "A Arte de Dar Exemplos Práticos\n" +
                    "\n" +
                    "Na busca por compreender conceitos complexos, a utilização de exemplos práticos é uma poderosa ferramenta. Um exemplo bem elaborado pode ilustrar de forma clara e tangível as ideias abstratas, facilitando a compreensão e tornando o aprendizado mais eficaz.\n" +
                    "\n" +
                    "Um bom exemplo prático permite que as pessoas visualizem como um conceito teórico pode ser aplicado na prática. Ele traz vida ao conhecimento, mostrando como ele se manifesta no mundo real. Ao enxergar um exemplo em ação, é possível entender melhor a sua utilidade e relevância.\n" +
                    "\n" +
                    "Além disso, exemplos práticos ajudam a estabelecer conexões entre teoria e prática. Eles proporcionam uma ponte entre o conhecimento abstrato e sua aplicação concreta. Ao ver um exemplo em funcionamento, é possível identificar como os conceitos teóricos se traduzem em ações e resultados reais.\n" +
                    "\n" +
                    "Ao dar exemplos práticos, é importante considerar a relevância para o público-alvo. É preciso escolher casos que sejam familiares e significativos para os destinatários do exemplo. Dessa forma, a mensagem se torna mais impactante e o aprendizado mais significativo.\n" +
                    "\n" +
                    "Por fim, é importante ressaltar que a arte de dar exemplos práticos requer habilidade e criatividade. É necessário encontrar situações concretas que possam ilustrar o conceito de forma clara e cativante. Um exemplo bem-sucedido desperta o interesse, estimula a curiosidade e facilita a assimilação do conhecimento.\n" +
                    "\n" +
                    "Portanto, ao buscar transmitir informações complexas, não subestime o poder dos exemplos práticos. Eles são uma ferramenta valiosa que pode tornar o aprendizado mais envolvente, compreensível e memorável. Utilize-os com sabedoria e desvende os mistérios do conhecimento.";
            output.writeUTF(teste1bin);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        outStream.close();

    }
}
*/

/*
class processedSharedFile {

    private final List<String> contentByLines;
    private final List<String> occurrenceList;

    public processedSharedFile (String[] contentByLines){
        this.contentByLines = List.of(contentByLines);
    }
}
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {

        //WriteBinaryFile writer = new WriteBinaryFile();
        //writer.createBinFile();
        //System.out.println("Bin file created!");

        Scanner scanner = new Scanner(System.in);

        //User insert necessary values: substring to search, number of files, thread per file, paths
        System.out.println("Digite a palavra a ser procurada:");
        //String substring = scanner.nextLine();
        String substring = "exemplo";

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

        System.out.println("Digite o número de threads para cada arquivo:");
        int threadPerFile;
        while(true){
            try {
                threadPerFile = scanner.nextInt();
                break;
            } catch (InputMismatchException e){
                System.out.println("Por favor, digite um número inteiro:");
                scanner.next();
            }
        }

        int totalThreadNumber = numberOfFiles * threadPerFile;
        FileProcessingThread[] threads = new FileProcessingThread[totalThreadNumber];

        System.out.println("Digite o path completo para o arquivo a ser processado, ou o path do arquivo na pasta Arquivos (src/Arquivos/nomedoarquivo.extensao):");
         /*
        String[] fileNames = new String[numberOfFiles];
        for(int i = 0; i < numberOfFiles; i++){
            System.out.print("Arquivos "+(i+1)+": ");
            fileNames[i] = scanner.next();
        }
        System.out.println();
         */
        //String[] fileNames = {"src/Arquivos/arquivo1.txt", "src/Arquivos/arquivo2.txt", "src/Arquivos/arquivo3.txt"};
        String[] fileNames = {"src/Arquivos/arquivo4.txt", "src/Arquivos/arquivo5.txt", "src/Arquivos/arquivo6.txt"};
        scanner.close();

        long startTimeReading = System.currentTimeMillis();
        //Pre-processing of target files
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
        System.out.println("\nO Pre processamento demorou " + durationTimeReading + " ms");


        //Get current time in milliseconds to check at the end
        long startTimeThreads = System.currentTimeMillis();

        //Create file processing threads with extension check, inserting null if failure happens
        for (int i = 0; i < totalThreadNumber; i++) {

            int fileNameIndex = i/threadPerFile;

            String extension = fileNames[fileNameIndex].substring(fileNames[fileNameIndex].lastIndexOf(".")+1);

            if(extension.equals("txt")||extension.equals("bin")) {
                File file = new File(fileNames[fileNameIndex]);
                threads[i] = new FileProcessingThread(fileNames[fileNameIndex], substring, i, file,  extension, threadPerFile, fileContentByLines.get(fileNameIndex));

            } else {
                System.out.println("Erro na Thread ID "+i+": Arquivo ["
                                    +fileNames[fileNameIndex].substring(fileNames[fileNameIndex].lastIndexOf("/")+1)+
                                    "] do tipo ["+extension+"] sem suporte. Utilize [txt] ou [bin]\n");
                threads[i] = null;
            }
        }

        /*
        for (int i = 0; i < totalThreadNumber; i++) {
            if(threads[i]==null) continue;
            threads[i].preProcessing();
        }
        */

        //Starts all threads and ignores null positions
        for (int i = 0; i < totalThreadNumber; i++) {
            if(threads[i]==null) continue;
            threads[i].start();
        }

        //Wait all threads end run() method and ignores null positions
        for (int i = 0; i < totalThreadNumber; i++) {
            if(threads[i]==null) continue;
            threads[i].join();
        }

        //Prints search results of each file once
        for (int i = 0; i < totalThreadNumber; i += 1) {
            if(threads[i]==null) continue;
            if(i%threadPerFile==0) {
                int separatorIndex = threads[i].getFileName().lastIndexOf("/");
                System.out.println("\nO arquivo ["+threads[i].getFileName().substring(separatorIndex + 1)
                        +"] possui "+threads[i].getFile().length() + " bytes");
            }
            threads[i].checkOccurrences();
        }

        long endTimeThread = System.currentTimeMillis();
        long durationTimeThread = endTimeThread - startTimeThreads;

        System.out.println("\nA busca demorou " + durationTimeThread + " ms");
    }
}
