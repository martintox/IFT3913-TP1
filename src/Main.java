import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    /**
     * main: exécute le code
     * @param args la liste d'arguments (la seule entrée à donner est un String représentant le path)
     * @throws FileNotFoundException si le path donné en entrée mène à un dossier non-existant
     */
    public static void main(String[] args) throws FileNotFoundException {
        Parser parser = new Parser();
        Output output = new Output();

        // Ces ArrayLists contiennent les ensembles de données à exporter en .csv par la suite
        List<String[]> classList = new ArrayList<>();       // ArrayList pour les classes
        List<String[]> packageList = new ArrayList<>();       // ArrayList pour les paquets

        // Parsing du code
        parser.parseCode(args[0], classList, packageList);

        // Mène à la création des fichiers .csv
        output.outputClassesToCSV(classList);
        output.outputPackagesToCSV(packageList);

    }
}
