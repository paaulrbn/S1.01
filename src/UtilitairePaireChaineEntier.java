import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        /*  Retourne l’indice de chaine dans listePaires si chaine est présente et -1 sinon. */

        listePaires.sort(PaireChaineEntier::compareTo);

        int debut = 0;
        int fin = listePaires.size() - 1;
        int milieu = (debut + fin) / 2;

        while (debut <= fin && listePaires.get(milieu).getChaine().compareTo(chaine) != 0) {
            if (listePaires.get(milieu).getChaine().compareTo(chaine) < 0) {
                debut = milieu + 1;
            } else {
                fin = milieu - 1;
            }
            milieu = (debut + fin) / 2;
        }

        if (debut <= fin) {
            return milieu;
        } else {
            return -1;
        }

    }


    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        /*  Retourne l’entier associé à la chaîne de caractères chaine dans listePaires si elle est présente et
            0 sinon. */

        int i = 0;
        while (i < listePaires.size() && listePaires.get(i).getChaine().compareTo(chaine) != 0) {
            i++;
        }

        if (i < listePaires.size() - 1) {
            return listePaires.get(i).getEntier();
        } else {
            return 0;
        }
    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        /*  Retourne la chaine associée au plus grand entier de listePaires */

        PaireChaineEntier max = listePaires.get(0);

        for (int i = 1; i < listePaires.size(); i++) {
            if (listePaires.get(i).getEntier() > max.getEntier()) {
                max = listePaires.get(i);
            }
        }
        return max.getChaine();
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        /*  Retourne la moyenne des entiers stockés dans listePaires. */

        float somme = 0;

        for (PaireChaineEntier paireChaineEntier : listePaires) {
            somme += paireChaineEntier.getEntier();
        }
        return somme / listePaires.size();
    }


}