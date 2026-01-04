import java.util.Scanner;
import javax.sound.sampled.*;
import java.net.URL;

public class Menu {
    public static Scanner scanner = new Scanner(System.in);
    public static boolean affichageChiffre = true;
    public static boolean placementNormal = true;
    public static Clip pisteAudio;
    public static boolean activerMusique =  true;


    public static void main(String[] args) throws Exception{
        int choix;
        son("/Musiques/Brumes-de-la-Campagne.wav");

        System.out.println( "\n\u001B[34m" +

                " .|'''.|    .                     .                           \n" +
                " ||..  '  .||.  ... ..   ....   .||.    ....    ... .   ...   \n" +
                "  ''|||.   ||    ||' '' '' .||   ||   .|...||  || ||  .|  '|. \n" +
                ".     '||  ||    ||     .|' ||   ||   ||        |''   ||   || \n" +
                "|'....|'   '|.' .||.    '|..'|'  '|.'  '|...'  '||||.  '|..|' \n" +
                "                                              .|....'         "
                + "\u001B[0m\n");

        do{
            System.out.println("1. Joueur vs Joueur \n2. Joueur vs Ordinateur \n3. Règles du jeu\n4. Changer paramètres \n5. Quitter\n");
            System.out.println("Veuillez entrez votre choix : ");
            choix = saisieValide(5);

            switch (choix) {
                case 1:
                    System.out.println("Joueur vs Joueur :\n");
                    sonJeu();
                    JoueurVsJoueur.j1VsJ2();
                    break;
                case 2:
                    System.out.println("Joueur vs Ordinateur :\n");
                    sonJeu();
                    JoueurVSOrdinateur.Installation();
                    break;
                case 3:
                    System.out.println("Règles du jeu : \n");
                    regleJeu();
                    System.out.println("Retour au menu \n");
                    break;
                case 4:
                    System.out.println("Changer paramètres : \n");
                    parametre();
                    System.out.println("Retour au menu \n");
                    break;
                case 5:
                    System.out.println("Au revoir!");
            }

        }while(choix != 5);
        scanner.close();
    }


    /* Menu des paramètres */
    public static void parametre() throws Exception{
        int choixParametre;
        do{
            System.out.println("1. Affichage Pièce\n2. Mode de placement des pièces\n3. Sons\n4. Retourner dans le menu");
            choixParametre = saisieValide(4);

            switch (choixParametre){
                case 1:
                    System.out.println("Affichage Pièce : \n");
                    parametreAffichePiece();
                    break;
                case 2 :
                    System.out.println("Mode de placement des pièces :\n");
                    parametrePlacement();
                    break;
                case 3:
                    System.out.println("Sons :\n");
                    parametreSon();
                    break;
                case 4:
                    System.out.println("Retour au menu.\n");
            }
        } while(choixParametre != 4);
    }

    /* Affichage des règles du jeu */
    public static void regleJeu(){
        System.out.println(

                "Stratego est un jeu d'attaque et de défense. Suspense et imprévu constituent des éléments importants du jeu.\n\n " +
                        "En bref\n Chacun des deux joueurs possède une armée composée de 40 pièces. Ces pièces ont des grades différents.\n " +
                        "Chaque joueur a un drapeau. Il s agit de défendre son propre drapeau et de conquérir le drapeau de l adversaire,\n " +
                        "en disposant en secret son armée et en la menant à la victoire.\n\n");
    }

    //-----------------------------------------------------------------------------------------------------------------------------------
    // Paramètre

    /* Permet le choix entre l'affichage avec les lettres ou l'affichage avec les chiffre des pièces */
    public static void parametreAffichePiece(){
        int choix;

        System.out.println("1. Activer l'affichage des pièces avec les chiffres\n2. Activer l'affichage des pièces avec les lettres\n3. Retour au Menu\n Votre choix : ");
        choix = saisieValide(3);

        switch (choix){
            case 1:
                System.out.println("Affichage des pièces avec les chiffres activé.\n");
                affichageChiffre = true;
                break;
            case 2:
                System.out.println("Affichage des pièces avec les lettres activé.\n");
                affichageChiffre = false;
                break;
            case 3:
                System.out.println("Retour au menu.\n");
        }
    }

    /* Permet le choix entre le placement normal et le placement aléatoire des pièces*/
    public static void parametrePlacement(){
        int choix;

        System.out.println("1. Activer placement normal\n2. Activer placement aléatoire\n3. Retour au Menu\n Votre choix : ");
        choix = saisieValide(3);

        switch (choix){
            case 1:
                System.out.println("Placement normal activé.\n");
                placementNormal = true;
                break;
            case 2:
                System.out.println("Placement aléatoire activé.\n");
                placementNormal = false;
                break;
            case 3:
                System.out.println("Retour au menu.\n");
        }
    }

    /* Active ou coupe le son */
    public static void parametreSon() throws Exception {

        int choix;

        System.out.println("1. Activer le son\n2. Couper le son\n3.Retour au Menu\n Votre choix : ");
        choix = saisieValide(3);

        switch (choix) {
            case 1:
                System.out.println("Musique activée.\n");
                activerMusique = true;
                son("/Musiques/Brumes-de-la-Campagne.wav");
                break;
            case 2:
                if (pisteAudio != null)
                    pisteAudio.stop();
                System.out.println("Musique désactivée\n");
                activerMusique = false;
                break;
            case 3:
                System.out.println("Retour au menu");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Musique

    /* Choisit au hasard la musique pour le jeu joueur/joueur et joueur/ordinateur */
    public static void sonJeu() throws Exception{

        String[] musiques = {"/Musiques/Brumes-de-Stratégie.wav", "/Musiques/Combat-Tactic.wav", "/Musiques/Combat-Victorieux.wav", "/Musiques/Lune-Silencieuse.wav",
                "/Musiques/Marche-guerrière.wav", "/Musiques/Marche-Silencieuse.wav", "/Musiques/Marche-Tactic.wav"};

        int hasard = (int) (Math.random() * musiques.length);
        String musiqueAleatoire = musiques[hasard];
        son(musiqueAleatoire);
    }

    /* Joue les musiques */
    public static void son(String cheminSon) throws Exception{
        if (!activerMusique)
            return;
        URL musique = Menu.class.getResource(cheminSon);
        if (pisteAudio != null){
            pisteAudio.stop();
            pisteAudio.close();
        }
        pisteAudio = AudioSystem.getClip();
        pisteAudio.open(AudioSystem.getAudioInputStream(musique));
        pisteAudio.loop(Clip.LOOP_CONTINUOUSLY);
        pisteAudio.start();
    }

    //-------------------------------------------------------------------------------------------------------------------
    // Saisie (appelée un peu partout)

    /* Fonction valide la saisie en int pour éviter les bugs*/
    public static int saisieValide(int choixMax){
        String saisie = "";
        boolean valide = false;
        while(!valide){
            saisie = scanner.nextLine();
            if (saisie.matches("[1-9]")){
                if (Integer.parseInt(saisie) <= choixMax){
                    valide = true;
                }
                else{
                    System.out.println("Aucun chiffre ne correspond. Recommencer : ");
                }
            }
            if (!saisie.matches(("[1-9]")))
                System.out.println("Mauvaise Saisie. Recommencer :");
        }
        return Integer.parseInt(saisie);
    }

}