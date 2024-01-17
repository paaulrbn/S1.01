public class PaireChaineEntier{
    private String chaine;
    private int entier;

    public String getChaine() { return chaine; }
    public int getEntier() { return entier; }

    public void setChaine(String chaine) { this.chaine = chaine; }
    public void setEntier(int entier) { this.entier = entier; }

    public PaireChaineEntier(String chaine, int entier) {
        this.chaine = chaine;
        this.entier = entier;
    }


    public int compareTo(PaireChaineEntier paireChaineEntier) {
        return this.chaine.compareTo(paireChaineEntier.getChaine());
    }
}