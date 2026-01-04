import java.util.Scanner;
import java.util.Arrays;

/*
    L: Faire l'IA
    L: Faire la vérification pour empêcher les situations blqouées

    M: 'combat' entre 2 pieces
    M:  regle "combat" piece
    M: victoire
*/
public class JoueurVsJoueur {
    public static Scanner scanner = new Scanner(System.in);
    public static final int LIGNES = 10;
    public static final int COLONNES = 10;
    public static int[][] plateau = new int[LIGNES][COLONNES];
    public static boolean joueur1 = true;
    public static boolean combatEffecute =  false;
    public static int dernierePieceAttaquante;

    // Variable globable situation bloqué
    public static int cycle = 0;
    public static int repetition = 0;
    public static int compteur = 0;
    public static int sousCompteur1 = 0;
    public static int sousCompteur2 = 0;
    // Premier Joueur
    public static int[] caseDepart1_1;
    public static int[] caseArrivee1_1;
    public static int[] caseDepart1_2;
    public static int[] caseArrivee1_2;

    //Deuzième Joueur
    public static int[] caseDepart2_1;
    public static int[] caseArrivee2_1;
    public static int[] caseDepart2_2;
    public static int[] caseArrivee2_2;



    public static void j1VsJ2(){
        creationPlateau();
        choixPlacement(Menu.placementNormal);
        jeu();
    }

    public static void choixPlacement(boolean choixNormal){
        if (choixNormal){
            placerPiecesNormal();
        }
        else{
            placerPiecesAleatoirement();
        }
    }

    /* Initialisation des valeurs du tableau */
    public static void creationPlateau() {
        // Lacs
        plateau[4][2] = 13;
        plateau[4][3] = 13;
        plateau[5][2] = 13;
        plateau[5][3] = 13;

        plateau[4][6] = 13;
        plateau[4][7] = 13;
        plateau[5][6] = 13;
        plateau[5][7] = 13;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Affichage

    /* Affiche le plateau */
    public static void affichePlateau() {

        // Affichage du plateau
        for (int lig = 0; lig < LIGNES; lig++) {
            for (int col = 0; col < COLONNES; col++)
                System.out.print("+-----");
            System.out.println("+");

            for (int col = 0; col < COLONNES; col++)
                System.out.print("|  " + affichageSelonJoueur(plateau[lig][col]) + "  ");
            System.out.println("|  " + (LIGNES - lig));
        }
        // Dernière ligne du plateau
        for (int col = 0; col < COLONNES; col++)
            System.out.print("+-----");
        System.out.println("+");

        // Lettres coordonnées
        for (char lettre = 'A'; lettre < 'A' + LIGNES; lettre++)
            System.out.print("   " + lettre + "  ");
        System.out.println();
    }

    /* Change le tour du joueur (de la constante) */
    public static void changerTourJoueur() {
        joueur1 = !joueur1;
    }

    /* Le joueur appuie sur 1 quand il veut terminer son tour */
    public static void finTourJoueur(){
        String finTour;

        System.out.println("Appuyer sur 1 pour finir votre tour :");
        do {
            finTour = scanner.nextLine();

            if (!finTour.matches("1"))
                System.out.println("Erreur de saisie. Recommencer :");

        } while (!finTour.matches("[1]"));
    }

    /* Affiche ou cache les pièces colorées selon le tour du joueur
    (fait appel à une autre méthode pour l'affichage exact pour les pièces pas cachées)*/
    public static String affichageSelonJoueur(int idPiece){
        if(idPiece == 0)
            return " ";
        if (idPiece == 13)
            return "\u001B[96m≈\u001B[0m";
        if (joueur1)
            if (idPiece < 0)
                return "\u001B[34m*\u001B[0m";
            else
                return "\u001B[31m" + choixAffichage(idPiece) + "\u001B[0m";
        else
        if (idPiece > 0)
            return "\u001B[31m*\u001B[0m";
        else
            return "\u001B[34m" + choixAffichage(idPiece) + "\u001B[0m";
    }

    public static String choixAffichage(int piece){
        if (Menu.affichageChiffre){
            return affichagePiecesChiffre(piece);
        }
        else{
            return affichagePiecesLettre(piece);
        }
    }

    /* Renvoi l'affichage selon l'identification des pièces
    (est appelé par affichageSelonJoueur) */
    public static String affichagePiecesChiffre(int idPiece) {

        switch (Math.abs(idPiece)){
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "M";
            case 11:
                return "X";
            case 12:
                return "⚑";
            default:
                return "?";
        }

    }

    public static String affichagePiecesLettre(int idPiece) {

        switch (Math.abs(idPiece)){
            case 1:
                return "⚔";
            case 2:
                return "▲";
            case 3:
                return "◇";
            case 4:
                return "D";
            case 5:
                return "E";
            case 6:
                return "F";
            case 7:
                return "G";
            case 8:
                return "H";
            case 9:
                return "I";
            case 10:
                return "☩";
            case 11:
                return "X";
            case 12:
                return "⚑";
            default:
                return "?";
        }

    }

    //-----------------------------------------------------------------------------------------------------------------
    // Placement

    /* Placer les pièces dans le plateau (version normal) */
    public static void placerPiecesNormal() {
        affichePlateau();
        System.out.println("Au joueur 1 de poser ses pièces.");
        System.out.println("Veuillez entrez les coordonnées du drapeau : ");
        placement(12, 1);
        System.out.println("Veuillez entrez les coordonnées des six bombes : ");
        placement(11, 6);
        System.out.println("Veuillez entrez les coordonnées du maréchal (10): ");
        placement(10, 1);
        System.out.println("Veuillez entrez les coordonnées du général (9) : ");
        placement(9, 1);
        System.out.println("Veuillez entrez les coordonnées des deux colonnels (8) : ");
        placement(8, 2);
        System.out.println("Veuillez entrez les coordonnées des trois  commandants (7) : ");
        placement(7, 3);
        System.out.println("Veuillez entrez les coordonnées des quatres capitaines (6) : ");
        placement(6, 4);
        System.out.println("Veuillez entrez les coordonnées des quatres lieutenants (5) : ");
        placement(5, 4);
        System.out.println("Veuillez entrez les coordonnées des quatres sergents (4) : ");
        placement(4, 4);
        System.out.println("Veuillez entrez les coordonnées des trois démineurs (3) : ");
        placement(3, 5);
        System.out.println("Veuillez entrez les coordonnées des éclaireurs huits éclaireurs (2): ");
        placement(2, 8);
        System.out.println("Veuillez entrez les coordonnées de l'assassins (1) : ");
        placement(1, 1);

        finTourJoueur();
        changerTourJoueur();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n"); // Pour pas que le joueur voit dans l'historique du terminal le plateau de l'autre joueur
        affichePlateau();

        System.out.println("Au joueur 2 de poser ses pièces.");
        System.out.println("Veuillez entrez les coordonnées du drapeau : ");
        placement(-12, 1);
        System.out.println("Veuillez entrez les coordonnées des six bombes : ");
        placement(-11, 6);
        System.out.println("Veuillez entrez les coordonnées du maréchal (10): ");
        placement(-10, 1);
        System.out.println("Veuillez entrez les coordonnées du général (9) : ");
        placement(-9, 1);
        System.out.println("Veuillez entrez les coordonnées des deux colonnels (8) : ");
        placement(-8, 2);
        System.out.println("Veuillez entrez les coordonnées des trois  commandants (7) : ");
        placement(-7, 3);
        System.out.println("Veuillez entrez les coordonnées des quatres capitaines (6) : ");
        placement(-6, 4);
        System.out.println("Veuillez entrez les coordonnées des quatres lieutenants (5) : ");
        placement(-5, 4);
        System.out.println("Veuillez entrez les coordonnées des quatres sergents (4) : ");
        placement(-4, 4);
        System.out.println("Veuillez entrez les coordonnées des trois démineurs (3) : ");
        placement(-3, 5);
        System.out.println("Veuillez entrez les coordonnées des éclaireurs huits éclaireurs (2): ");
        placement(-2, 8);
        System.out.println("Veuillez entrez les coordonnées de l'assassins (1) : ");
        placement(-1, 1);

        finTourJoueur();
        changerTourJoueur();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
    }

    /* Place le nombre de fois la même pièce (version normal)*/
    public static void placement(int id, int nb){

        String coordo;
        int coordoLig, coordoCol;

        for (int i = 0; i < nb; i++){
            do {
                coordo = saisirCoordonnee();
                coordoLig = tranformInt(coordo);
                coordoCol = transformLettre(coordo);

            }while(!verifPlacementInstallation(coordoLig, coordoCol));

            plateau[coordoLig][coordoCol] = id;
            affichePlateau();
        }
    }

    /* Check si le placement est possible (version normal)*/
    public static boolean verifPlacementInstallation(int lig, int col) {
        if (plateau[lig][col] == 0){
            if (joueur1)
                if (lig >= 6 && lig < LIGNES) {
                    return true;
                }
                else {
                    System.out.println("Vous ne pouvez pas placer vos pièce au delà de la ligne 4. Recommencer:");
                }
            else {
                if (lig < 4) {
                    return true;
                }
                else {
                    System.out.println("Vous ne pouvez pas placer vos pièce en dessous de la ligne 7. Recommencer :");
                }
            }
        }
        else {
            System.out.println("Cette case n'est pas vide. Recommencer : ");
        }
        return false;
    }

    /* Placer les pièces dans le plateau (version aléatoire) */
    public static void placerPiecesAleatoirement(){
        while(!piecesNonBloques()){
            viderPlateau(); // Pour éviter les bugs
            System.out.println("Placement aléatoire des pièces du joueur 1.");
            placementAleatoire(12, 1); // Drapeau
            placementAleatoire(11, 6); // Bombes
            placementAleatoire(10, 1); // Maréchal
            placementAleatoire(9, 1); // Général
            placementAleatoire(8, 2); // Colonnels
            placementAleatoire(7, 3); //Commandants
            placementAleatoire(6, 4); // Capitaines
            placementAleatoire(5, 4); // Lieutenants
            placementAleatoire(4, 4); // Sergents
            placementAleatoire(3, 5); // Démineurs
            placementAleatoire(2, 8); // Eclaireurs
            placementAleatoire(1, 1); // Assasins
        }
        System.out.println("Voici vos pièces :");
        affichePlateau();
        changerTourJoueur();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");

        while(!piecesNonBloques()){
            viderPlateau();
            System.out.println("Placement aléatoire des pièces du joueur 2.\n");
            placementAleatoire(-12, 1); // Drapeau
            placementAleatoire(-11, 6); // Bombes
            placementAleatoire(-10, 1); // Marchéal
            placementAleatoire(-9, 1); // Général
            placementAleatoire(-8, 2); // Colonnels
            placementAleatoire(-7, 3); // Commandants
            placementAleatoire(-6, 4); // Capitaines
            placementAleatoire(-5, 4); // Lieutenants
            placementAleatoire(-4, 4); // Sergents
            placementAleatoire(-3, 5); // Démineurs
            placementAleatoire(-2, 8); // Eclaireurs
            placementAleatoire(-1, 1); // Assassins
        }
        System.out.println("Voici vos pièces :");
        affichePlateau();
        changerTourJoueur();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
    }

    /* Si les pièces bloqués, enlève les pièces pour réinitialiser le placement aléatoire (version aléatoire) */
    public static void viderPlateau(){
        if (joueur1){
            for (int lig = 0; lig < LIGNES; lig++)
                for(int col = 0; col < COLONNES; col++)
                    if (plateau[lig][col] > 0 && plateau[lig][col] < 13)
                        plateau[lig][col] = 0;
        }
        else {
            for (int lig = 0; lig < LIGNES; lig++)
                for(int col = 0; col < COLONNES; col++)
                    if (plateau[lig][col] < 0)
                        plateau[lig][col] = 0;
        }
    }

    /* Place le nombre de fois la même pièce (version aléatoire)*/
    public static void placementAleatoire(int id, int nb) {
        for (int i = 0; i < nb; i++){
            boolean placer = false;
            while(!placer){
                if (joueur1){
                    int lig = (int) (Math.random() * (9 - 6 + 1)) + 6;
                    int col = (int) (Math.random() * 10);
                    if (plateau[lig][col] == 0) {
                        plateau[lig][col] = id;
                        placer = true;
                    }
                }
                else{
                    int lig = (int) (Math.random() * 4);
                    int col = (int) (Math.random() * 10);
                    if (plateau[lig][col] == 0) {
                        plateau[lig][col] = id;
                        placer = true;
                    }
                }
            }
        }
    }

    /* Vérifie qu'au moins une pièce peut être bouger par le joueur suite au placement aléatoire
       Pour pas qu'il y ait déjà une condition de victoire avec le jeu (version aléatoire)*/

    public static boolean piecesNonBloques(){
        int [] col = {0, 1, 4, 5, 8, 9};

        if(joueur1){
            for (int i : col)
                if (plateau[7][i] > 0 && plateau[7][i] < 11)
                    return true;
        }
        else{
            for (int i : col)
                if (plateau[3][i] < 0 && plateau[3][i] > -11)
                    return true;

        }
        return false;
    }

    //-----------------------------------------------------------------------------------------------------------------
    // Jeu

    /* Gère les tours avec le déplacement et le combat des pièces jusqu'à la victoire */
    public static void jeu(){
        while (!victoire()){
            if (combatEffecute == true){
                revelationPieceAttanquante();
            }
            affichePlateau();
            bougerPiece();
            affichePlateau();
            finTourJoueur();
            changerTourJoueur();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //Mouvement

    // Programme principal du mouvement

    /* Permet le mouvement de la pièce */
    public static void bougerPiece(){
        // Sélectionne la pièce à déplacer
        int [] tabCoordonneesPiece  =  choisirPiece();
        // Valide la case de déplacement

        int [] tabCoordonneesCase =valideCase(tabCoordonneesPiece);
        // Bouge la pièce
        mouvement(tabCoordonneesPiece, tabCoordonneesCase);
    }



    // Etape 1: Pièce

    /* Valide ou non la pièce sélectionner par le joueur */
    public static int[] choisirPiece(){
        String coordo;
        int coordoLig, coordoCol;
        int[] tab = new int[2];

        System.out.print("Quelle pièce souhaitez bouger? \n");
        do{
            coordo = saisirCoordonnee();
            coordoLig = tranformInt(coordo);
            coordoCol = transformLettre(coordo);
        } while(!verifExistancePiece(coordoLig, coordoCol) || !pieceBougeable(coordoLig, coordoCol, true));

        tab[0] = coordoLig;
        tab[1] = coordoCol;
        return  tab;
    }

    /* Vérifie si la pièce existe et si ce n'est pas un lac ou une pièce immobile */
    public static boolean verifExistancePiece(int lig, int col){
        if (joueur1)
            if (plateau[lig][col] > 0 && plateau[lig][col] <=  13){
                if (plateau[lig][col] < 11){
                    return true;
                }
                else{
                    System.out.println("C'est une pièce immmobile. Choisissez une autre pièce :");
                    return false;
                }

            }
            else {
                System.out.println("Vous ne possédez pas de pièce à cette case là. Recommencer : ");
                return false;
            }
        else {
            if (plateau[lig][col] < 0 && plateau[lig][col] > -13) {
                if (plateau[lig][col] < 0 && plateau[lig][col] > -11){
                    return true;
                }
                else{
                    System.out.println("C'est une pièce immmobile. Choisissez une autre pièce :");
                    return false;
                }
            }
            else{
                System.out.println("Vous ne possédez pas de pièce à cette case là. Recommencer : ");
                return false;

            }
        }
    }

    /* Vérifie s'il est possible de bouger la pièce */
    public static boolean pieceBougeable(int lig, int col, boolean afficherMessage) {
        int testLig, testCol, testCase;
        int[][] direction = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};

        for (int i = 0; i < direction.length; i++) {
            testLig = lig + direction[i][0];
            testCol = col + direction[i][1];

            if (testLig >= 0 && testLig < LIGNES){
                if (testCol >= 0 && testCol < COLONNES){
                    testCase = plateau[testLig][testCol];
                    if (joueur1) {
                        if (testCase <= 0)
                            return true;
                    }
                    else {
                        if (testCase >= 0 && testCase < 13)
                            return true;
                    }
                }
            }
        }
        if (afficherMessage) {  //pour pas avoir le message dans la methode victoire
            System.out.println("La pièce ne peut bouger car elle est bloquée par vos autres pièces. Recommencer : ");
        }  return false;
    }



    // Etape 2 : La case futur

    /* Valide la saisie et valide la case de déplacement */
    public static int[] valideCase(int[] coordoPiece){
        String coordo;
        int coordoLig, coordoCol;
        int[] nvCase = new int[2];

        System.out.println("Où veux-tu la déplacer ? ");
        do{
            coordo = saisirCoordonnee();
            coordoLig = tranformInt(coordo);
            coordoCol = transformLettre(coordo);
        } while(!verifMouvementPossible(coordoPiece, coordoLig, coordoCol));

        nvCase[0] = coordoLig;
        nvCase[1] = coordoCol;
        return nvCase;
    }

    /* Vérifie si la case de déplacement est valide sur le plateau */
    public static boolean verifMouvementPossible(int[] coordoPiece, int testNvLig, int testNvCol){

        if (situationBloque(coordoPiece, testNvLig, testNvCol)){
            System.out.println("Mouvement en boucle, donc impossible. Recommencer :");
            return false;
        }

        int caseTest = plateau[testNvLig][testNvCol];

        if (joueur1){
            if (caseTest <= 0)
                if (verifieMouvementSelonLaPiece(coordoPiece,testNvLig, testNvCol))
                    return true;
            if (caseTest > 0 && caseTest != 13){
                System.out.println("La pièce ne peut se déplacer sur ces propres pièces. Recommencer :");
            }

        }
        else{
            if (caseTest >= 0 && caseTest < 13)
                if (verifieMouvementSelonLaPiece(coordoPiece, testNvLig, testNvCol))
                    return true;
            if(caseTest < 0)
                System.out.println("La pièce ne peut se déplacer sur ces propres pièces. Recommencer :");
        }

        if (caseTest ==  13){
            System.out.println("La pièce ne peut se déplacer sur le lac. Recommencer :");
        }

        return false;
    }

    /* Valide si la situation est bloquée ou non (par les répétition) */
    public static boolean situationBloque(int[] coordoPiece, int testNvLig, int testNvCol){

        if (cycle == 3) {
            cycle = 0;
            caseDepart1_1 = null;
            caseDepart1_2 = null;
            caseArrivee1_1 = null;
            caseArrivee1_2 = null;

            caseDepart2_1 = null;
            caseDepart2_2 =  null;
            caseArrivee2_1 =  null;
            caseArrivee2_2 = null;
            return true;
        }

        if (cycle == 1)
            repetition = 0;

        if (repetitionMouvement(coordoPiece, testNvLig, testNvCol)){
            cycle++;
        }

        return false;
    }

    /* Vérifie si les mouvements se répète en cycle */
    public static boolean repetitionMouvement(int[] coordoPiece, int testNvLig, int testNvCol){

        if (compteur % 2 == 0){
            if (sousCompteur1 % 2 == 0){
                caseDepart1_1 = new int[] {coordoPiece[0], coordoPiece[1]};
                caseArrivee1_1 = new int[] {testNvLig,testNvCol};
            }
            else{
                caseDepart1_2 = new int[] {coordoPiece[0], coordoPiece[1]};
                caseArrivee1_2 = new int[] {testNvLig,testNvCol};
            }
            sousCompteur1++;
        }

        else {
            if (sousCompteur2 % 2 == 0) {
                caseDepart2_1 = new int[]{coordoPiece[0], coordoPiece[1]};
                caseArrivee2_1 = new int[]{testNvLig, testNvCol};
            }
            else {
                caseDepart2_2 = new int[]{coordoPiece[0], coordoPiece[1]};
                caseArrivee2_2 = new int[]{testNvLig, testNvCol};
            }
            sousCompteur2++;
        }
        compteur++;


        if (caseDepart1_1 != null && caseDepart1_2 != null && caseArrivee1_1 != null && caseArrivee1_2 != null &&
            caseDepart2_1 != null && caseDepart2_2 != null && caseArrivee2_1 !=  null && caseArrivee2_2 != null)
            if (Arrays.equals(caseDepart1_1,caseArrivee1_2) && Arrays.equals(caseArrivee1_1,caseDepart1_2)
                && Arrays.equals(caseDepart2_1, caseArrivee2_2) && Arrays.equals(caseArrivee2_1, caseDepart2_2)) {
                sousCompteur1 = 0;
                sousCompteur2 = 0;
                return true;
            }

        return false;
    }



    /* regarde si le mouvement est valide selon ses possiblités de mouvement) */
    public static boolean verifieMouvementSelonLaPiece(int[] coordoPie, int testNvL, int testNvC){

        int idPiece = plateau[coordoPie[0]][coordoPie[1]];

        // Mouvement des pièces normales (mouvement orthogonal, pas de 1)
        if (Math.abs(idPiece) != 2){
            // test le pas de 1 selon la position initial de la pièce et la position de la case voulu

            if(testNvL == coordoPie[0])
                if (testNvC - 1 == coordoPie[1] || testNvC + 1 == coordoPie[1])
                    return true;
            if (testNvC == coordoPie[1])
                if (testNvL - 1 == coordoPie[0] || testNvL + 1 == coordoPie[0])
                    return true;
            System.out.println("La pièce ne peut se déplacer aussi loin. Recommencer :");
            return false;
        }

        // Mouvement de l'éclaireur comme une tour (mouvement orthogonal, pas : limite du plateau ou bloqué avant par des pièces)
        else{
            return eclaireur(coordoPie, testNvL, testNvC);
        }
    }

    /* Vérifie se le mouvement de l'éclaireur est valide */
    public static boolean eclaireur(int[] coordoPie, int testNvL, int testNvC){
        // Si sur la même ligne
        if(testNvL == coordoPie[0]) {
            // Test la droite verticale
            if (testNvC != coordoPie[1])
                if (mouvementVerticalEclaireur(testNvL,coordoPie[1], testNvC))
                    return true;
        }

        // Si sur la même colonne
        if (testNvC == coordoPie[1]) {
            // Test la droite horizontale
            if (testNvL != coordoPie[0])
                if (mouvementHorizontaleEclaireur(testNvC,coordoPie[0], testNvL))
                    return true;
        }
        else{
            System.out.println("Aucune pièce ne peut se déplacer en diagonal. Recommencer :");
        }
        return false;
    }

    /* Verifie à la vertical que les cases sont vides entre le départ et l'arrivée exclues */
    public static boolean mouvementVerticalEclaireur(int lig, int colDepart, int colArrivee){
        if (colDepart < colArrivee){
            for(int co = colDepart + 1; co < colArrivee - 1; co++)
                if (plateau[lig][co] != 0) {
                    System.out.println("Chemin impossible car bloqué par une pièce. Recommencez :");
                    return false;
                }
        }
        else {
            for (int co = colArrivee + 1; co < colDepart - 1; co++)
                if (plateau[lig][co] != 0){
                    System.out.println("Chemin impossible car bloqué par une pièce. Recommencez :");
                    return false;
                }
        }
        return true;
    }

    /* Verifie à l'horizontal que les cases sont vides entre le départ et l'arrivée exclues */
    public static boolean mouvementHorizontaleEclaireur(int col, int ligDepart, int ligArrivee){
        if (ligDepart < ligArrivee){
            for(int li = ligDepart + 1; li < ligArrivee - 1; li++)
                if (plateau[li][col] != 0) {
                    System.out.println("Chemin impossible car bloqué par une pièce. Recommencez :");
                    return false;
                }
        }
        else {
            for (int li = ligArrivee + 1; li < ligDepart - 1; li++)
                if (plateau[li][col] != 0){
                    System.out.println("Chemin impossible car bloqué par une pièce. Recommencez :");
                    return false;
                }
        }
        return true;
    }

    /* Déplace la pièce sélectionné à la case sélectionné si case vide
       Appel la méthode combat si pièce adverse */

    public static void mouvement(int[] pieceQuiBouge, int[] newCase){
        int idPiece = plateau[pieceQuiBouge[0]][pieceQuiBouge[1]];

        // Bouge direct la pièce si case vide
        if (plateau[newCase[0]][newCase[1]] == 0) { //regarde si case vide
            plateau[newCase[0]][newCase[1]] = idPiece; //met le pions dans la case
            plateau[pieceQuiBouge[0]][pieceQuiBouge[1]] = 0; //mets la case d'ou vien le pion vide
        }

        // Appel méthode combat si pièce adverse
        else{
            combat(pieceQuiBouge, newCase);
            combatEffecute = true;
        }
    }



    // ----------------------------------------------------------------------------------------------------------------
    // Coordonnées (utilisé un peu partout à chaque saisie)


    /* Saisie de Coordonnée */
    public static String saisirCoordonnee(){
        String saisieCoordo = "";
        boolean valide = false;

        while(!valide){
            saisieCoordo = scanner.nextLine();

            if (saisieCoordo.matches("[A-J]([1-9]|10)"))
                valide = true;

            if (!saisieCoordo.matches("[A-J]([1-9]|10)"))
                System.out.println("Mauvaise saisie. Recommencez :");
        }
        return saisieCoordo;
    }

    /* Récupère la lettre en colonne */
    public static int transformLettre(String col){
        String recupStr;
        char strToChar;
        recupStr = col.substring(0, 1);
        strToChar = recupStr.charAt(0);
        return strToChar - 'A';
    }

    /* Récupère la ligne (avec inversion par rapport au tableau) */
    public static int tranformInt(String lig){
        int recupInt;
        recupInt = Integer.parseInt(lig.substring(1));
        return LIGNES - recupInt;
    }



    //------------------------------------------------------------------------------------------------------------------
    // Combat


    public static void combat(int[] pieceQuiAttaque, int[] pieceCible){
        int idAttaquant = plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]];
        int idDefenseur = plateau[pieceCible[0]][pieceCible[1]];
        dernierePieceAttaquante = idAttaquant;

        // Révélation écrite de la pièce adverse
        System.out.println("La pièce ciblé est un : " + choixAffichage(idDefenseur) + "\n Place au combat!");

        // si attaque bombe
        if (Math.abs(idDefenseur) == 11) {
            combatContreBombe(pieceQuiAttaque, pieceCible, idAttaquant);
            return;// sert a sortir direct du if apres le combat evite un "double" combat en mode normal
        }
        // si attaque drapeau
        if (Math.abs(idDefenseur) == 12) {
            captureDrapeau(pieceQuiAttaque, pieceCible, idAttaquant);
            return;
        }
        // assassin attaque marechal
        if (Math.abs(idAttaquant) == 1 && Math.abs(idDefenseur) == 10) {
            assassinVsMarchal(pieceQuiAttaque, pieceCible, idAttaquant);
            return;
        }
        // combat des autres pieces
        combatNormal(pieceQuiAttaque, pieceCible, idAttaquant, idDefenseur);
    }


    //combat contre bombe démineur(3) peut desamorcer la bombe
    public static void combatContreBombe(int[] pieceQuiAttaque, int[] pieceCible, int idAttaquant) {
        if (Math.abs(idAttaquant) == 3) {
            plateau[pieceCible[0]][pieceCible[1]] = idAttaquant;
            plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
        } else {
            plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
        }
    }

    //attaquant vas sur case du drapeau
    public static void captureDrapeau(int[] pieceQuiAttaque, int[] pieceCible, int idAttaquant) {
        plateau[pieceCible[0]][pieceCible[1]] = idAttaquant;
        plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
    }

    // assassin peut tue maréchal
    public static void assassinVsMarchal(int[] pieceQuiAttaque, int[] pieceCible, int idAttaquant) {
        plateau[pieceCible[0]][pieceCible[1]] = idAttaquant;
        plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
    }

    // comparaison des valeurs/id des pions et 'garde' le pions plus grand
    public static void combatNormal(int[] pieceQuiAttaque, int[] pieceCible, int idAttaquant, int idDefenseur) {
        int valeurAttaquant = Math.abs(idAttaquant);
        int valeurDefenseur = Math.abs(idDefenseur);

        if (valeurAttaquant > valeurDefenseur) {
            //attaquant pions plus fort/gagne
            plateau[pieceCible[0]][pieceCible[1]] = idAttaquant;
            plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
        }
        else if (valeurAttaquant < valeurDefenseur) {
            //défenseur pions plus fort/gagne
            plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
        }
        else {
            //egalite les deux pièces sont retire
            plateau[pieceQuiAttaque[0]][pieceQuiAttaque[1]] = 0;
            plateau[pieceCible[0]][pieceCible[1]] = 0;
        }
    }

    // Permet la révélation de la pièce attaquante au joueur adverse au début de son tour
    public static void revelationPieceAttanquante(){
        System.out.println("La pièce adverse qui a initié un combat est un : " + choixAffichage(dernierePieceAttaquante));
        combatEffecute = false;
    }



    //------------------------------------------------------------------------------------------------------------------
    // Victoire



    public static boolean victoire() {
        boolean drapeauJ1Present = false;
        boolean drapeauJ2Present = false;
        boolean j1PeutBouger = false;
        boolean j2PeutBouger = false;

        //'sauvegarder' l'etat/quel joueur jou actuel du joueur
        boolean joueurActuel = joueur1;

        //verifie présence des drapeaux et si joueurs peuvent bouger
        for (int lig = 0; lig < LIGNES; lig++) {
            for (int col = 0; col < COLONNES; col++) {
                int piece = plateau[lig][col];

                //drapeaux
                if (piece == 12) drapeauJ1Present = true;
                if (piece == -12) drapeauJ2Present = true;
                //pièces mobiles du joueur 1
                if (piece > 0 && piece < 11) {
                    joueur1 = true;
                    if (pieceBougeable(lig, col, false)) j1PeutBouger = true;
                }
                //pièces mobiles du joueur 2
                if (piece < 0 && piece > -11) {
                    joueur1 = false;
                    if (pieceBougeable(lig, col, false)) j2PeutBouger = true;
                }
            }
        }

        //remet l'état du joueur comme au debut apres tchek des pieces
        joueur1 = joueurActuel;

        //conditions victoire
        if (!drapeauJ1Present || !j1PeutBouger) {//si j1 a plusde drapeau ou qu'il a plus de pions bougeable
            System.out.println( "\u001B[34m" +
                    "       $$$$$$\\                                                     $$\\                \n" +
                    "      $$  __$$\\                                                    $$ |               \n" +
                    "      $$ /  \\__| $$$$$$\\   $$$$$$\\  $$$$$$$\\   $$$$$$\\  $$$$$$$\\ $$$$$$\\              \n" +
                    "      $$ |$$$$\\  \\____$$\\ $$  __$$\\ $$  __$$\\  \\____$$\\ $$  __$$\\\\_$$  _|             \n" +
                    "      $$ |\\_$$ | $$$$$$$ |$$ /  $$ |$$ |  $$ | $$$$$$$ |$$ |  $$ | $$ |               \n" +
                    "      $$ |  $$ |$$  __$$ |$$ |  $$ |$$ |  $$ |$$  __$$ |$$ |  $$ | $$ |$$\\            \n" +
                    "      \\$$$$$$  |\\$$$$$$$ |\\$$$$$$$ |$$ |  $$ |\\$$$$$$$ |$$ |  $$ | \\$$$$  |           \n" +
                    "       \\______/  \\_______| \\____$$ |\\__|  \\__| \\_______|\\__|  \\__|  \\____/            \n" +
                    "                          $$\\   $$ |                                                  \n" +
                    "                          \\$$$$$$  |                                                  \n" +
                    "                           \\______/                                                   \n" +
                    "   $$$$$\\                                                          $$$$$$\\        $$\\ \n" +
                    "   \\__$$ |                                                        $$  __$$\\       $$ |\n" +
                    "      $$ | $$$$$$\\  $$\\   $$\\  $$$$$$\\  $$\\   $$\\  $$$$$$\\        \\__/  $$ |      $$ |\n" +
                    "      $$ |$$  __$$\\ $$ |  $$ |$$  __$$\\ $$ |  $$ |$$  __$$\\        $$$$$$  |      $$ |\n" +
                    "$$\\   $$ |$$ /  $$ |$$ |  $$ |$$$$$$$$ |$$ |  $$ |$$ |  \\__|      $$  ____/       \\__|\n" +
                    "$$ |  $$ |$$ |  $$ |$$ |  $$ |$$   ____|$$ |  $$ |$$ |            $$ |                \n" +
                    "\\$$$$$$  |\\$$$$$$  |\\$$$$$$  |\\$$$$$$$\\ \\$$$$$$  |$$ |            $$$$$$$$\\       $$\\ \n" +
                    " \\______/  \\______/  \\______/  \\_______| \\______/ \\__|            \\________|      \\__|\n" +
                    "                                                                                      \n" +
                    "                                                                                      \n" +
                    "                                                                                      " +
                    "\u001B[0m\n");

            menuFinPartie();
            return true;
        }
        if (!drapeauJ2Present || !j2PeutBouger) {//meme conditions
            System.out.println("\u001B[31m" +
                    "       $$$$$$\\                                                     $$\\              \n" +
                    "      $$  __$$\\                                                    $$ |             \n" +
                    "      $$ /  \\__| $$$$$$\\   $$$$$$\\  $$$$$$$\\   $$$$$$\\  $$$$$$$\\ $$$$$$\\            \n" +
                    "      $$ |$$$$\\  \\____$$\\ $$  __$$\\ $$  __$$\\  \\____$$\\ $$  __$$\\\\_$$  _|           \n" +
                    "      $$ |\\_$$ | $$$$$$$ |$$ /  $$ |$$ |  $$ | $$$$$$$ |$$ |  $$ | $$ |             \n" +
                    "      $$ |  $$ |$$  __$$ |$$ |  $$ |$$ |  $$ |$$  __$$ |$$ |  $$ | $$ |$$\\          \n" +
                    "      \\$$$$$$  |\\$$$$$$$ |\\$$$$$$$ |$$ |  $$ |\\$$$$$$$ |$$ |  $$ | \\$$$$  |         \n" +
                    "       \\______/  \\_______| \\____$$ |\\__|  \\__| \\_______|\\__|  \\__|  \\____/          \n" +
                    "                          $$\\   $$ |                                                \n" +
                    "                          \\$$$$$$  |                                                \n" +
                    "                           \\______/                                                 \n" +
                    "   $$$$$\\                                                           $$\\         $$\\ \n" +
                    "   \\__$$ |                                                        $$$$ |        $$ |\n" +
                    "      $$ | $$$$$$\\  $$\\   $$\\  $$$$$$\\  $$\\   $$\\  $$$$$$\\        \\_$$ |        $$ |\n" +
                    "      $$ |$$  __$$\\ $$ |  $$ |$$  __$$\\ $$ |  $$ |$$  __$$\\         $$ |        $$ |\n" +
                    "$$\\   $$ |$$ /  $$ |$$ |  $$ |$$$$$$$$ |$$ |  $$ |$$ |  \\__|        $$ |        \\__|\n" +
                    "$$ |  $$ |$$ |  $$ |$$ |  $$ |$$   ____|$$ |  $$ |$$ |              $$ |            \n" +
                    "\\$$$$$$  |\\$$$$$$  |\\$$$$$$  |\\$$$$$$$\\ \\$$$$$$  |$$ |            $$$$$$\\       $$\\ \n" +
                    " \\______/  \\______/  \\______/  \\_______| \\______/ \\__|            \\______|      \\__|\n" +
                    "                                                                                    \n" +
                    "                                                                                    \n" +
                    "                                                                                    "
                    + "\u001B[31m");
            menuFinPartie();
            return true;
        }
        return false;
    }



    //------------------------------------------------------------------------------------------------------------------
    // Menu fin



    // menu affiché à la fin de la partie pour 'fermer' programme ou relancer avec meme parametre que partie precedante
    public static void menuFinPartie() {
        String choix;
        System.out.println("Que voulez-vous faire ?");
        System.out.println("1. Rejouer une partie (garde les parametres)");
        System.out.println("2. Retour au menu principal");
        System.out.println("3. Quitter le jeu");
        System.out.print("Entrer votre choix : ");

        do{
            choix = scanner.nextLine();
            if (!choix.matches("[1-3]"))
                System.out.println("Choix invalide. Recommencer: ");

        } while (!choix.matches("[1-3]"));


        if (choix.equals("1")) {
            reinitialiserPartie();
            j1VsJ2(); //relance une nouvelle partie avec les paramettre mis au debut donc si pieces placer aleatoir relance jeu avec plateau aleatoire

        } else if (choix.equals("2")){
            System.out.println("Retour au menu principal\n\n");

        } else {
            System.out.println("Merci d'avoir joué ! À bientôt !");//ferme le programme/terminal
            System.exit(0); // Arrête le programme

        }
    }

    //remet plateau a vide ou si aleatoir avce les pions au hazar
    public static void reinitialiserPartie() {
        // Vider tout le plateau
        for (int lig = 0; lig < LIGNES; lig++) {
            for (int col = 0; col < COLONNES; col++) {
                plateau[lig][col] = 0;
            }
        }
        //remettre le j1 en premier pour le premier tour
        joueur1 = true;

        //recréer le plateau avec les lacs
        creationPlateau();
        System.out.println("\u001B[35m" +
                "  _   _                       _ _         _____           _   _        _ \n" +
                " | \\ | |                     | | |       |  __ \\         | | (_)      | |\n" +
                " |  \\| | ___  _   ___   _____| | | ___   | |__) |_ _ _ __| |_ _  ___  | |\n" +
                " | . ` |/ _ \\| | | \\ \\ / / _ \\ | |/ _ \\  |  ___/ _` | '__| __| |/ _ \\ | |\n" +
                " | |\\  | (_) | |_| |\\ V /  __/ | |  __/  | |  | (_| | |  | |_| |  __/ |_|\n" +
                " |_| \\_|\\___/ \\__,_| \\_/ \\___|_|_|\\___|  |_|   \\__,_|_|   \\__|_|\\___| (_)\n" +
                "                                                                         \n" +
                "                                                                         "
                + "\u001B[0m\n");
    }
}