import java.io.*;

public class FileHelper {
    public static void deleteDirectory(String folderName) {
        File index = new File(folderName);
        if (!index.exists()) {
            index.mkdir();
        } else {
            String[]entries = index.list();
            for(String s: entries){
                File currentFile = new File(index.getPath(),s);
                currentFile.delete();
            }
            index.delete();
            if (!index.exists()) {
                index.mkdir();
            }
        }
    }

    public static void createDirectory(String folderName) {
        File folder = new File(folderName);
        folder.mkdir();
    }

    public static void clearDirectory(String folderName) {
        deleteDirectory(folderName);
        createDirectory(folderName);
    }

    public static void saveToFile(Object obj, String path) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(obj);
        out.close();
        fileOut.close();
    }

    public static String urlToFilename(String url) {
        char[] charactersToReplace = {'"', '\\', '/', ':', '?', '*', '<', '>', '|'};

        for (char i : charactersToReplace) {
            url = url.replace(i, ' ');
        }
        return url;
    }

    public static Object readFile(String path) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return in.readObject();
    }
}
