import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Output {

    /**
     * outputClassesToCSV: note les informations reliées aux classes d'un projet Java
     * dans un fichier .csv
     * @param data une liste contenant des arrays de données pertinentes au classes
     * @throws FileNotFoundException si le fichier de sortie n'existe pas
     */
    public void outputClassesToCSV(List<String[]> data) throws FileNotFoundException {

        File out = new File("classes.csv");
        PrintWriter writer = new PrintWriter(out);

        // Représente la première ligne du fichier .csv
        String tags = "chemin,class,classe_LOC,classe_CLOC,classe_DC,WMC,classe_BC\n";

        write(data, writer, tags);

    }

    /**
     * outputPackagesToCSV: note les informations reliées aux paquets d'un projet Java
     * dans un fichier .csv
     * @param data une liste contenant des arrays de données pertinentes au classes
     * @throws FileNotFoundException si le fichier de sortie n'existe pas
     */
    public void outputPackagesToCSV(List<String[]> data) throws FileNotFoundException {

        File out = new File("paquets.csv");
        PrintWriter writer = new PrintWriter(out);

        // Représente la première ligne du fichier .csv
        String tags = "chemin,paquet,paquet_LOC,paquet_CLOC,paquet_DC,WCP,paquet_BC\n";

        write(data, writer, tags);
    }

    /**
     * write: procède à l'inscription des données dans un fichier .csv
     * @param data les données qu'on souhaite ajouter au .csv
     * @param writer objet permettant d'ajouter des données au  .csv
     * @param tags ligne indiquant le type de donnée de chaque colonne
     */
    private void write(List<String[]> data, PrintWriter writer, String tags) {
        writer.write(tags);

        for(String[] c : data) {
            String text = c[0] + "," + c[1] + "," + c[2] + "," + c[3] + "," + c[4] + "," + c[5] + "," + c[6] + "\n";
            writer.write(text);
        }
        writer.flush();
    }

}
