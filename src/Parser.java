import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Parser {

    /**
     * parseCode: effectue le parsing pour tous les fichiers .java dans un dossier ainsi que
     * ses sous-dossiers.
     * @param path le chemin du dossier de départ.
     * @param classList une liste où on entrepose les informations pour chaque classe.
     * @param packageList une liste où on entrepose les informations pour chaque paquet.
     * @throws FileNotFoundException si le path mène à un dossier qui n'existe pas.
     */
    public void parseCode(String path, List<String[]> classList, List<String[]> packageList)
            throws FileNotFoundException {

        // On initialise le dossier ainsi que la liste des fichiers contenus
        File folder = new File(path);
        File[] files = folder.listFiles();

        // Si le dossier n'est pas vide...
        if(files != null) {
            int package_LOC = 0;
            int package_CLOC = 0;
            int package_WCP = 0;

            // Itère sur tous les fichiers
            for(File file : files) {

                String name = file.getName();
                String extension = name.substring(name.lastIndexOf(".") + 1);

                // Si le fichier est un dossier, on exécute parseCode récursivement
                if(file.isDirectory()) parseCode(String.valueOf(file), classList, packageList);
                else if(extension.equals("java")) {
                    // On parse la classe et on ajoute les résultats
                    int[] results = parseClass(file);

                    int class_LOC = results[0];
                    int class_CLOC = results[1];
                    double class_DC = 1.0 * results[1] / results[0];

                    // On parse à nouveau pour déterminer la complexité cyclomatique
                    int class_WMC = parseClassCycle(file);
                    double class_BC = class_WMC == 0 ? 0 : class_DC / class_WMC;

                    // On ajoute les informations de la classe à classList
                    classList.add(new String[] {
                                    String.valueOf(file),
                                    file.getName(),
                                    String.valueOf(class_LOC),
                                    String.valueOf(class_CLOC),
                                    String.valueOf(class_DC),
                                    String.valueOf(class_WMC),
                                    String.valueOf(class_BC)
                    }
                    );

                    // On ajoute le résultat aux valeurs correspondant au paquet (dossier)
                    package_LOC += class_LOC;
                    package_CLOC += class_CLOC;
                    package_WCP += class_WMC;
                }
            }

            // On calcule le DC et le BC
            double package_DC = 1.0 * package_CLOC / package_LOC;
            double package_BC = package_WCP == 0 ? 0 : package_DC / package_WCP;

            // On ajoute les informations du paquet au packageList

            if (package_LOC != 0) {
                packageList.add(new String[] {
                    path,
                    String.valueOf(Paths.get(path).getFileName()),
                    String.valueOf(package_LOC),
                    String.valueOf(package_CLOC),
                    String.valueOf(package_DC),
                    String.valueOf(package_WCP),
                    String.valueOf(package_BC)
                }
                );
            }
        }

    }

    /**
     * parseClass: effectue le parsing d'un seul fichier .java
     * @param file un fichier .java sur lequel on fait le parsing
     * @return un array d'entiers contenant le nombre de lignes de code ainsi que le nombre de lignes de commentaire
     * @throws FileNotFoundException si le fichier donné n'existe pas
     */
    private int[] parseClass(File file) throws FileNotFoundException {
        // Initialise le LOC et le CLOC
        int class_LOC = 0;
        int class_CLOC = 0;

        Scanner scanner = new Scanner(file);
        String line;

        // On regarde le fichier ligne par ligne
        while(scanner.hasNext()) {
            line = scanner.nextLine();

            /* Si une ligne commence avec /*, on peut supposer que celle-ci,
               ainsi que toutes les autres qui suivent,
               sont des lignes de commentaires jusqu'à ce qu'on arrive à */
            if(line.startsWith("/*")) {
                while(!line.contains("*/")) {
                    if(!line.isEmpty()) {
                        class_CLOC++;
                        class_LOC++;
                    }
                    line = scanner.nextLine();
                }
            }
            else if(line.startsWith("//")) { // Si une ligne commence avec //, c'est une ligne de commentaire
                class_CLOC++;
                class_LOC++;
            }
            else if(!line.isBlank()) { // Si la ligne ne commence pas avec // ou /*...
                class_LOC++;

                // ...il se peut qu'il y ait tout de même un commentaire après le code.
                if(line.contains("//") || line.contains("/*")) class_CLOC++;
            }
        }

        // On retourne le LOC et le CLOC
        return new int[]{class_LOC, class_CLOC};
    }

    /**
     * parseClassCycle: comptabilise le nombre de structures de contrôle dans une classe pour déterminer
     * sa complexité cyclomatique de McCabe
     * @param file un fichier .java sur lequel on fait le parsing
     * @return un entier class_WMC qui représente la complexité cyclomatique de la classe
     * @throws FileNotFoundException si le fichier donné n'existe pas
     */
    private int parseClassCycle(File file) throws FileNotFoundException {
        int class_WMC = 0;

        /* Un ensemble d'expressions qui sont indicatives d'une structure de contrôle.
           À noter qu'on comprend "return", "public void" et "private void"; même si une méthode
           ne contient pas d'expressions de type "if" etc., ceci nous permet de compter le nombre de
           méthodes dans une classe. */
        String[] expressions = { "if(", "if (", "else if(", "else if (", "case ", "public void ", "private void ",
                                 "for(", "for (", " && ", " || ", " ? ", "catch(", "catch (", "return " };

        Scanner scanner = new Scanner(file);
        String line;

        // On regarde le fichier ligne par ligne
        while(scanner.hasNext()) {
            line = scanner.nextLine();

            // Si on voit une expression, on comptabilise class_WMC
            for(String expression : expressions) if(line.contains(expression)) class_WMC++;
        }

        return class_WMC;
    }

}
