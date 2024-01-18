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


    public static int nbDepeche(ArrayList<Depeche> depeches, String categorie) {
        /* Renvoie le nombre de dépêches ayant la catégorie "categorie"  */
        int nb = 0;
        for (Depeche depeche : depeches) {
            if (depeche.getCategorie().equalsIgnoreCase(categorie)) {
                nb++;
            }
        }
        return nb;
    }

    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        /*  Pour chacune des dépêches de depeches, calcule le score pour chaque catégorie de categories et
            écrit dans le fichier de nom nomFichier, le nom de la catégorie ayant le plus grand score ainsi que les
            pourcentages conformément au format attendu pour les fichiers réponses. */

        int envScience = 0;
        int culture = 0;
        int economie = 0;
        int politique = 0;
        int sports = 0;

        try {
            FileWriter file = new FileWriter(nomFichier);

            int sommeTotal = 0;

            for (Depeche depeche : depeches) {
                ArrayList<PaireChaineEntier> scoreDepeche = new ArrayList<>();

//                for (int i = 0; i < 5; i++) {
//                    scoreDepeche.add(new PaireChaineEntier(categories.get(i).getNom(), categories.get(i).score(depeche)));
//                }

                int somme = 0;
                for (int i = 0; i < 5; i++) {
                    ArrayList<Integer> scores = categories.get(i).score(depeche);
                    scoreDepeche.add(new PaireChaineEntier(categories.get(i).getNom(), scores.get(0)));
                    somme += scores.get(1);
                }
                sommeTotal += (somme/5);



                String categorieCoresp = UtilitairePaireChaineEntier.chaineMax(scoreDepeche);

                file.write(depeche.getId() + " : " + categorieCoresp + "\n");

                if (categorieCoresp.equalsIgnoreCase(depeche.getCategorie())) {
                    if (categorieCoresp.equalsIgnoreCase("ENVIRONNEMENT-SCIENCES")) {
                        envScience++;
                    } else if (categorieCoresp.equalsIgnoreCase("CULTURE")) {
                        culture++;
                    } else if (categorieCoresp.equalsIgnoreCase("ECONOMIE")) {
                        economie++;
                    } else if (categorieCoresp.equalsIgnoreCase("POLITIQUE")) {
                        politique++;
                    } else if (categorieCoresp.equalsIgnoreCase("SPORTS")) {
                        sports++;
                    }
                }
            }

            // Affichage du nombre de comparaison moyen de la méthode score

            // System.out.println();
            // System.out.println("Nombre de comparaison moyenne de la méthode score : " + sommeTotal/500);


            int pEnv = (int) (( (float) envScience/ nbDepeche(depeches, "ENVIRONNEMENT-SCIENCES"))*100);
            int pCulture = (int) (( (float) culture/ nbDepeche(depeches, "CULTURE"))*100);
            int pEconomie = (int) (( (float) economie/ nbDepeche(depeches, "ECONOMIE"))*100);
            int pPolitique = (int) (( (float) politique/ nbDepeche(depeches, "POLITIQUE"))*100);
            int pSports = (int) (( (float) sports/ nbDepeche(depeches, "SPORTS"))*100);

            file.write("ENVIRONNEMENT-SCIENCES : " + pEnv + "%\n" +
                            "CULTURE : " + pCulture + "%\n" +
                            "ECONOMIE : " + pEconomie + "%\n" +
                            "POLITIQUE : " + pPolitique + "%\n" +
                            "SPORTS : " + pSports + "%\n" +
                            "MOYENNE : " + ((pEnv + pCulture + pEconomie + pPolitique + pSports)/ (float) 5) + "%\n");

            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        /*  Retourne une ArrayList<PaireChaineEntier> contenant tous les mots présents dans au
            moins une dépêche de la catégorie categorie. Même si le mot est présent plusieurs fois,
            il apparaît qu’une fois dans la ArrayList retournée. Dans les entiers, les scores sont stockés
            et associés à chaque mot et dans un premier temps, nous initialiserons ce score à 0. */

        ArrayList<String> present = new ArrayList<>();

        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        for (Depeche depeche : depeches) {
            if (depeche.getCategorie().equalsIgnoreCase(categorie)) {
                for (String mot : depeche.getMots()) {
                    if (!present.contains(mot)) {
                        present.add(mot);
                        resultat.add(new PaireChaineEntier(mot, 0));
                    }
                }
            }
        }
        return resultat;
    }

    public static int calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        /*  Met à jour les scores des mots présents dans dictionnaire. Lorsqu'un mot présent dans
            dictionnaire apparaît dans une dépêche de depeches, son score est : décrémenté si la dépêche
            n'est pas dans la catégorie categorie et incrémenté si la dépêche est dans la catégorie categorie. */

        int c = 0;
        dictionnaire.sort(PaireChaineEntier::compareTo);

        for (Depeche depeche : depeches){
            for(String mot : depeche.getMots()){
                int index = UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, mot);
                if(index != -1){
                    c++;
                    if(depeche.getCategorie().equalsIgnoreCase(categorie)){
                        dictionnaire.get(index).setEntier(dictionnaire.get(index).getEntier() + 1);
                    }else{
                        dictionnaire.get(index).setEntier(dictionnaire.get(index).getEntier() - 1);
                    }
                }
            }
        }
        return c;

    }

    public static int poidsPourScore(int score) {
        /*  Retourne une valeur de poids (1,2 ou 3) en fonction du score */

        if (score <= 0) {
            return 0;
        } else if (score < 2) {
            return 1;
        } else if (score < 5) {
            return 2;
        } else {
            return 3;
        }
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        /*  Crée pour la catégorie categorie le fichier lexique de nom nomFichier à partir du vecteur de
            dépêches depeches. Cette méthode construit une ArrayList<PaireChaineEntier> avec
            initDico, puis met à jour les scores dans ce vecteur avec calculScores et enfin utilise le vecteur
            résultant pour créer un fichier lexique en utilisant la fonction poidsPourScore. */

        ArrayList<PaireChaineEntier> dictionnaire = initDico(depeches, categorie);

        System.out.println(categorie + " : " + calculScores(depeches, categorie, dictionnaire));
//        calculScores(depeches, categorie, dictionnaire);

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

        // Choix du mode de génération des lexiques

        Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez saisir 0 pour lexique manuel et 1 pour lexique automatique : ");
        int choix = scanner.nextInt();


        long startTime = System.currentTimeMillis();

        //Chargement des dépêches en mémoire
//        System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
        ArrayList<Depeche> dechechesTest = lectureDepeches("./test.txt");



//        for (int i = 0; i < depeches.size(); i++) {
//            depeches.get(i).afficher();
//        }

        // Création de l'ArrayList avec les catégories
        ArrayList<Categorie> categories = new ArrayList<>(Arrays.asList(new Categorie("ENVIRONNEMENT-SCIENCES"),
                                                                        new Categorie("CULTURE"),
                                                                        new Categorie("ECONOMIE"),
                                                                        new Categorie("POLITIQUE"),
                                                                        new Categorie("SPORTS")));


        if (choix == 0) {
            // Génération des lexiques manuellement

            // Initialisation des lexiques pour chaque catégorie à partir des fichiers
            categories.get(0).initLexique("./lexiqueM/ENVIRONNEMENT-SCIENCES.txt");
            categories.get(1).initLexique("./lexiqueM/CULTURE.txt");
            categories.get(2).initLexique("./lexiqueM/ECONOMIE.txt");
            categories.get(3).initLexique("./lexiqueM/POLITIQUE.txt");
            categories.get(4).initLexique("./lexiqueM/SPORTS.txt");


        } else if (choix == 1) {
            // Génération des lexiques automatiquement

            // Création des threads
            ArrayList<Thread> threads = new ArrayList<>();

            // Génération des lexiques pour chaque catégorie grace a des threads
            System.out.println("Nombre de comparaisons pour calculScores : ");
            threads.add(new Thread(() -> generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "lexiqueA/ENVIRONNEMENT-SCIENCES.txt")));
            threads.add(new Thread(() -> generationLexique(depeches, "CULTURE", "lexiqueA/CULTURE.txt")));
            threads.add(new Thread(() -> generationLexique(depeches, "ECONOMIE", "lexiqueA/ECONOMIE.txt")));
            threads.add(new Thread(() -> generationLexique(depeches, "POLITIQUE", "lexiqueA/POLITIQUE.txt")));
            threads.add(new Thread(() -> generationLexique(depeches, "SPORTS", "lexiqueA/SPORTS.txt")));

            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Initialisation des lexiques pour chaque catégorie à partir des fichiers générée
            categories.get(0).initLexique("./LexiqueA/ENVIRONNEMENT-SCIENCES.txt");
            categories.get(1).initLexique("./LexiqueA/CULTURE.txt");
            categories.get(2).initLexique("./LexiqueA/ECONOMIE.txt");
            categories.get(3).initLexique("./LexiqueA/POLITIQUE.txt");
            categories.get(4).initLexique("./LexiqueA/SPORTS.txt");
        }



        // Classement des dépêches dans leur categories
        classementDepeches(dechechesTest, categories, "./fichier-sortie.txt");

        long endTime = System.currentTimeMillis();
        System.out.println("Programme réalisé en : " + (endTime-startTime) + "ms");


    }
}