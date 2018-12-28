package FilesManager;

import com.apple.eio.FileManager;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileUtils {

    private String currPath;

    private Map<Character, Integer> result;
    private StringBuilder message;

    public FileUtils(String currPath) {
        this.currPath = currPath;
        if(!this.currPath.endsWith("/")) {
            this.currPath += "/";
        }
        this.result = new HashMap<>();
        this.message = new StringBuilder();
    }

    /**
     *
     * @param fileName didn't need to start with `/`
     * @return File
     */
    public File findFile(String fileName) {
        File file = null;
        try {
            file = new File(currPath+fileName);
        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return file;
    }

    public void readFile(File file) {
        int[] count = new int[255];

        String path = currPath+file.getName();

        try {
            InputStream flux = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(flux);
            BufferedReader buffer = new BufferedReader(reader);
            String ligne;
            while((ligne=buffer.readLine()) != null)
            {
                int length = ligne.length();
                for (int i = 0; i < length; i++) {
                    count[ligne.charAt(i)]++;
                }
                this.message.append(ligne);
                char[] ch = new char[ligne.length()];
                for (int i = 0; i < length; i++) {
                    ch[i] = ligne.charAt(i);
                    int find = 0;
                    for (int j = 0; j <= i; j++) {
                        if (ligne.charAt(i) == ch[j])
                            find++;
                    }

                    if (find == 1) {
                        this.result.put(ligne.charAt(i), count[ligne.charAt(i)]);
                    }
                }
            }
            buffer.close();
            this.result = sortByComparator(this.result, true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Map<Character, Integer> sortByComparator(Map<Character, Integer> unsortMap, final boolean order) {

        List<Map.Entry<Character, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        Collections.sort(list, (o1, o2) -> {
            if (order)
                return o1.getValue().compareTo(o2.getValue());
            else
                return o2.getValue().compareTo(o1.getValue());
        });

        Map<Character, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Character, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public HashMap<Character, Integer> getResult() { return (HashMap<Character, Integer>) result; }

    public StringBuilder getMessage() { return message; }

    public byte[] readBinaryFile(String fileName) {
        byte[] bytes = null;
        try{
            Path path = Paths.get(this.currPath+fileName);
            bytes = Files.readAllBytes(path);
        } catch (IOException e){
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, e);
        }
        return bytes;
    }

    public void writeBinaryFile(String s, String fileName) {
        try{
            Path path = Paths.get(this.currPath+fileName);
            byte[] bytes = BinaryUtils.convertStringToBytes(s);
            FileOutputStream out = new FileOutputStream(path.toString());
            out.write(bytes);
             Files.write(path, bytes); //creates, overwrites
        } catch (IOException e){
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public File[] getAllFiles(){
        File folder = new File(currPath);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }
}
