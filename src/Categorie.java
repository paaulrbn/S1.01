import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()) {
                String thisLine = scanner.nextLine();
                int indexSeparator = thisLine.indexOf(':');

                String mot = thisLine.substring(0, indexSeparator - 1);
                int force = Integer.parseInt(thisLine.substring(indexSeparator));

                lexique.add(new PaireChaineEntier(mot, force));
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        return 0;
    }


}
