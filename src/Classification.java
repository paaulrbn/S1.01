import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Classification {


    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }


    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        //qui pour chacune des dépêches de depeches, calcule le score pour chaque catégorie de categories et
        //écrit dans le fichier de nom nomFichier, le nom de la catégorie ayant le plus grand score ainsi que les
        //pourcentages conformément au format attendu pour les fichiers réponses (voir ci-dessus). Prenez exemple sur
        //la classe ExempleEcritureFichier pour l’écriture dans un fichier.

        try {
            FileWriter file = new FileWriter(nomFichier);
            for (Depeche depeche : depeches) {
                ArrayList<PaireChaineEntier> scoreDepeche = new ArrayList<>();

                for (int i = 0; i < 5; i++) {
                    scoreDepeche.add(new PaireChaineEntier(categories.get(i).getNom(), categories.get(i).score(depeche)));
                }

                file.write(depeche.getId() + " : ");
                file.write(UtilitairePaireChaineEntier.chaineMax(scoreDepeche) + "\n");
            }

            //

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        return resultat;

    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
    }

    public static int poidsPourScore(int score) {
        return 0;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

    }

    public static void main(String[] args) {

        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");

        for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }

        ArrayList<Categorie> categories = new ArrayList<>(Arrays.asList(new Categorie("Environment-sciences"),
                                                                        new Categorie("Culture"),
                                                                        new Categorie("Economie"),
                                                                        new Categorie("Politique"),
                                                                        new Categorie("Sports")));


        categories.get(0).initLexique("./ENVIRONNEMENT-SCIENCES.txt");
        categories.get(1).initLexique("./CULTURE.txt");
        categories.get(2).initLexique("./ECONOMIE.txt");
        categories.get(3).initLexique("./POLITIQUE.txt");
        categories.get(4).initLexique("./SPORTS.txt");

        ArrayList<PaireChaineEntier> scoreDepeche = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            scoreDepeche.add(new PaireChaineEntier(categories.get(i).getNom(), categories.get(i).score(depeches.get(0))));
        }

        System.out.println("Catégorie pour la depeche 1 : " + UtilitairePaireChaineEntier.chaineMax(scoreDepeche));



        classementDepeches(depeches, categories, "./fichier-sortie.txt");


    }
}