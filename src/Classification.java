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
        // Pour chacune des dépêches de depeches, calcule le score pour chaque catégorie de categories et
        // écrit dans le fichier de nom nomFichier, le nom de la catégorie ayant le plus grand score ainsi que les
        // pourcentages conformément au format attendu pour les fichiers réponses (voir ci-dessus). Prenez exemple sur
        // la classe ExempleEcritureFichier pour l’écriture dans un fichier.

        int envScience = 0;
        int culture = 0;
        int economie = 0;
        int politique = 0;
        int sports = 0;

        try {
            FileWriter file = new FileWriter(nomFichier);
            for (Depeche depeche : depeches) {
                ArrayList<PaireChaineEntier> scoreDepeche = new ArrayList<>();


                for (int i = 0; i < 5; i++) {
                    scoreDepeche.add(new PaireChaineEntier(categories.get(i).getNom(), categories.get(i).score(depeche)));
                }

                file.write(depeche.getId() + " : ");
                file.write(UtilitairePaireChaineEntier.chaineMax(scoreDepeche) + "\n");

                if (depeche.getCategorie().equalsIgnoreCase("ENVIRONNEMENT-SCIENCES") && UtilitairePaireChaineEntier.chaineMax(scoreDepeche).equalsIgnoreCase("ENVIRONNEMENT-SCIENCES")) {
                    envScience++;
                } else if (depeche.getCategorie().equalsIgnoreCase("CULTURE") && UtilitairePaireChaineEntier.chaineMax(scoreDepeche).equalsIgnoreCase("CULTURE")) {
                    culture++;
                } else if (depeche.getCategorie().equalsIgnoreCase("ECONOMIE") && UtilitairePaireChaineEntier.chaineMax(scoreDepeche).equalsIgnoreCase("ECONOMIE")) {
                    economie++;
                } else if (depeche.getCategorie().equalsIgnoreCase("POLITIQUE") && UtilitairePaireChaineEntier.chaineMax(scoreDepeche).equalsIgnoreCase("POLITIQUE")) {
                    politique++;
                } else if (depeche.getCategorie().equalsIgnoreCase("SPORTS") && UtilitairePaireChaineEntier.chaineMax(scoreDepeche).equalsIgnoreCase("SPORTS")) {
                    sports++;
                }

            }


            file.write("ENVIRONNEMENT-SCIENCES: " + (envScience/ 100f)*100 + "%\n");
            file.write("CULTURE: " + (culture/ 100f)*100 + "%\n");
            file.write("ECONOMIE: " + (economie/100f)*100 + "%\n");
            file.write("POLITIQUE: " + (politique/100f)*100 + "%\n");
            file.write("SPORTS: " + (sports/100f)*100 + "%\n");

            file.write("MOYENNE : " + ((envScience + culture + economie + politique + sports)/500f)*100 + "%\n");

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        for (Depeche depeche : depeches) {
            if (depeche.getCategorie().equalsIgnoreCase(categorie)) {
                for (String mot : depeche.getMots()) {
                    resultat.add(new PaireChaineEntier(mot, 0));
                }
            }
        }
        return resultat;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {

        for (Depeche depeche : depeches) {
            if (depeche.getCategorie().equalsIgnoreCase(categorie)) {
                for (String mot : depeche.getMots()) {
                    for (PaireChaineEntier paireChaineEntier : dictionnaire) {
                        if (mot.equals(paireChaineEntier.getChaine())) {
                            paireChaineEntier.setEntier(paireChaineEntier.getEntier() + 1);
                        }
                    }
                }
            } else {
                for (String mot : depeche.getMots()) {
                    for (PaireChaineEntier paireChaineEntier : dictionnaire) {
                        if (mot.equals(paireChaineEntier.getChaine())) {
                            paireChaineEntier.setEntier(paireChaineEntier.getEntier() - 1);
                        }
                    }
                }
            }
        }

    }

    public static int poidsPourScore(int score) {
        if (score <= 1) {
            return 0;
        } else if (score == 2) {
            return 1;
        } else if (score == 3) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

        ArrayList<PaireChaineEntier> dictionnaire = initDico(depeches, categorie);
        calculScores(depeches, categorie, dictionnaire);

        try {
            FileWriter file = new FileWriter(nomFichier);

            for (PaireChaineEntier paire : dictionnaire) {
                if (poidsPourScore(paire.getEntier()) != 0) {
                    file.write(paire.getChaine() + ":" + poidsPourScore(paire.getEntier()) + "\n");
                }
            }

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //Chargement des dépêches en mémoire
        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./test.txt");

        for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }

        ArrayList<Categorie> categories = new ArrayList<>(Arrays.asList(new Categorie("ENVIRONNEMENT-SCIENCES"),
                                                                        new Categorie("CULTURE"),
                                                                        new Categorie("ECONOMIE"),
                                                                        new Categorie("POLITIQUE"),
                                                                        new Categorie("SPORTS")));

        generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "ENVIRONNEMENT-SCIENCES.txt");
        generationLexique(depeches, "CULTURE", "CULTURE.txt");
        generationLexique(depeches, "ECONOMIE", "ECONOMIE.txt");
        generationLexique(depeches, "POLITIQUE", "POLITIQUE.txt");
        generationLexique(depeches, "SPORTS", "SPORTS.txt");


        categories.get(0).initLexique("./ENVIRONNEMENT-SCIENCES.txt");
        categories.get(1).initLexique("./CULTURE.txt");
        categories.get(2).initLexique("./ECONOMIE.txt");
        categories.get(3).initLexique("./POLITIQUE.txt");
        categories.get(4).initLexique("./SPORTS.txt");

//        ArrayList<PaireChaineEntier> scoreDepeche = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            scoreDepeche.add(new PaireChaineEntier(categories.get(i).getNom(), categories.get(i).score(depeches.get(0))));
//        }
//
//        System.out.println("Catégorie pour la depeche 1 : " + UtilitairePaireChaineEntier.chaineMax(scoreDepeche));





        classementDepeches(depeches, categories, "./fichier-sortie.txt");




    }
}