package stadnina;

import java.io.*;
import java.util.List;

public class DataExporter {

    public static void saveStablesBinary(List<Stable> stables, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(stables);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Stable> loadStablesBinary(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<Stable>) ois.readObject();
        }
    }

    public static void exportToCSV(StableDAO dao, String filename) throws IOException {
        List<Object[]> data = dao.getExportData();
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("Stadnina,Kon,Rasa,Cena");
            for (Object[] row : data) {
                writer.printf("%s,%s,%s,%.2f\n", row[0], row[1], row[2], row[3]);
            }
        }
    }
}