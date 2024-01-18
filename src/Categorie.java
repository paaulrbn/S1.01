import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique = new ArrayList<>(); //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }

    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        /*  Initialise l’attribut lexique (une ArrayList<PaireChaineEntier>) à partir du fichier lexique
            dont le nom nomFichier est passé en paramètre. */

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()) {
                String thisLine = scanner.nextLine();
                int indexSeparator = thisLine.indexOf(':');

                String mot = thisLine.substring(0, indexSeparator);
                int force = Integer.parseInt(thisLine.substring(indexSeparator + 1));
                lexique.add(new PaireChaineEntier(mot, force));

            }
            scanner.close();
            lexique.sort(PaireChaineEntier::compareTo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //calcul du score d'une dépêche pour la catégorie
    public ArrayList<Integer> score(Depeche d) {
        /*  Retourne l’entier correspondant au score de la dépêche d pour la catégorie sur laquelle l’appel est
            réalisé. */

        ArrayList<String> mots = d.getMots();
        int score = 0;
        int c = 0;

        for (String mot : mots) {
            int index = UtilitairePaireChaineEntier.indicePourChaine(lexique, mot);
            c++;
            if (index != -1) {
                score += lexique.get(index).getEntier();
            }
        }

        return new ArrayList<>(Arrays.asList(score, c));
    }

}