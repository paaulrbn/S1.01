import java.util.ArrayList;
import java.util.Objects;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        int i = 0;
        while (i < listePaires.size() && !Objects.equals(listePaires.get(i).getChaine(), chaine)) {
            i++;
        }
        if (i != listePaires.size()) {
            return i;
        } else {
            return -1;
        }
    }


    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
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
        PaireChaineEntier max = listePaires.get(0);

        for (int i = 1; i < listePaires.size(); i++) {
            if (listePaires.get(i).getEntier() > max.getEntier()) {
                max = listePaires.get(i);
            }
        }

        return max.getChaine();
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        return 0;
    }


}