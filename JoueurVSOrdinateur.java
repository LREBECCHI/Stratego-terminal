import javax.print.attribute.standard.OrientationRequested;

    public class JoueurVSOrdinateur {
    public static boolean tourJoueurHumain = false;
    public static boolean humainJoueur1 = false;

    public static void changerTourHumainOrdi(){
        tourJoueurHumain = !tourJoueurHumain;
    }

    public static void Installation(){
        JoueurVsJoueur.reinitialiserPartie();

       // Le joueur est au hasard le premier ou deuxième joueur
        int joueur =  (int) ((Math.random() * 2) + 1);
        if (joueur == 1){
            tourJoueurHumain = true;
            humainJoueur1 = true;

        }


        JoueurVsJoueur.creationPlateau();

        // Placement des pièces
        if (tourJoueurHumain){
            // Joueur commence
            System.out.println("Vous commencer.");
            if (Menu.placementNormal){
                System.out.println("Veuillez placez vos pièces :");
                JoueurVsJoueur.placerPiecesNormal();
            }
            else{
                System.out.println("Placement aléatoires de vos pièces");
                JoueurVsJoueur.placerPiecesAleatoirement();
            }
            JoueurVsJoueur.finTourJoueur();
            JoueurVsJoueur.changerTourJoueur();
            changerTourHumainOrdi();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");


            // Au tour de l'ordi
            System.out.println("L'ordinateur place ses pièces.");
            JoueurVsJoueur.placerPiecesAleatoirement();
        }
        else{
            // Ordi commence
            System.out.println("L'ordinateur commence.");
            JoueurVsJoueur.placerPiecesAleatoirement();
            JoueurVsJoueur.changerTourJoueur();
            changerTourHumainOrdi();

            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");

            // Au tour du joueur
            System.out.println("A votre tour.");
            if (Menu.placementNormal){
                System.out.println("Veuillez placez vos pièces : ");
                JoueurVsJoueur.placerPiecesNormal();
            }
            else{
                System.out.println("Placement aléatoires de vos pièces");
                JoueurVsJoueur.placerPiecesAleatoirement();
            }
            JoueurVsJoueur.finTourJoueur();
        }

        JoueurVsJoueur.changerTourJoueur();
        changerTourHumainOrdi();
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n");



        // Début du jeu
        jeu();
    }



    //------------------------------------------------------------------------------------------------------------------
    //Jeu

    /* Gestion des tours entre ordinateur et joueur pour les mouvements jusqu'à la victoire */
    public static void jeu(){

        while(!victoire()){
            if (JoueurVsJoueur.combatEffecute == true){
                JoueurVsJoueur.revelationPieceAttanquante();
            }

            // Gestion des tours selon si le joueur est premier ou deuxième
            if (tourJoueurHumain){
                JoueurVsJoueur.affichePlateau();
                JoueurVsJoueur.bougerPiece();
                JoueurVsJoueur.affichePlateau();
                JoueurVsJoueur.finTourJoueur();
            }
            else{
                mouvementOrdinateur();

            }

            JoueurVsJoueur.changerTourJoueur();
            changerTourHumainOrdi();
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n");
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    // Mouvement Ordinateur

    /* Bouge une pièce aléatoirement */
    public static void mouvementOrdinateur() {
        int[] coordonneesPiece = choisirPieceAleatoire();
        int[] coordonneesCase = deplacementAleatoire(coordonneesPiece);
        JoueurVsJoueur.mouvement(coordonneesPiece, coordonneesCase);
    }

    /* Choisit une pièce aléatoirement */
    public static int[] choisirPieceAleatoire() {
        int lig, col;
        do{
            lig = (int) (Math.random() * JoueurVsJoueur.LIGNES);
            col = (int) (Math.random() * JoueurVsJoueur.COLONNES);

        } while(!JoueurVsJoueur.verifExistancePiece(lig, col) || !JoueurVsJoueur.pieceBougeable(lig, col, false));

        return new int[] {lig, col};
    }

    /* Déplace la pièce sélectionnée aléatoirement */
    public static int[] deplacementAleatoire(int[] coordonnesPiece) {
        int lig = coordonnesPiece[0];
        int col = coordonnesPiece[1];
        int testLig, testCol, hasard;
        boolean valide = false;

        // Favorise le déplacement en avant des troupes de l'ordi
        if (JoueurVsJoueur.joueur1){
            testLig = lig - 1;
            testCol = col;
        }
        else{
            testLig = lig + 1;
            testCol = col;

        }

        if (valideMouvementOrdi(testLig, testCol)){
            return new int[] {testLig, testCol};
        }

        else{
            do{
                hasard = (int) (Math.random() * 4);
                if (hasard == 0){
                    testLig = lig + 1;
                    testCol = col;
                }
                else if( hasard == 1){
                    testLig = lig - 1;
                    testCol = col;
                }

                else if (hasard ==2){
                    testLig = lig;
                    testCol = col + 1;
                }

                else{
                    testLig = lig;
                    testCol = col - 1;
                }

            } while (!valideMouvementOrdi(testLig, testCol));

        }


        return new int[] {testLig, testCol};
    }

    /* Vérifie que c'est pas en dehors du plateau et soit une case vide ou une pièce adverse*/
    public static boolean valideMouvementOrdi(int testLig, int testCol){

        int testCase;

        if (testLig >= 0 && testLig < JoueurVsJoueur.LIGNES){
            if (testCol >= 0 && testCol < JoueurVsJoueur.COLONNES){
                testCase = JoueurVsJoueur.plateau[testLig][testCol];
                if (JoueurVsJoueur.joueur1) {
                    if (testCase <= 0)
                        return true;
                }
                else {
                    if (testCase >= 0 && testCase < 13)
                        return true;
                }
            }
        }

        return false;
    }



    //------------------------------------------------------------------------------------------------------------------
    // Victoire

    public static boolean victoire() {
        boolean drapeauJ1Present = false;
        boolean drapeauJ2Present = false;
        boolean j1PeutBouger = false;
        boolean j2PeutBouger = false;

        //'sauvegarder' l'etat/quel joueur jou actuel du joueur
        boolean joueurActuel = JoueurVsJoueur.joueur1;

        //verifie présence des drapeaux et si joueurs peuvent bouger
        for (int lig = 0; lig < JoueurVsJoueur.LIGNES; lig++) {
            for (int col = 0; col < JoueurVsJoueur.COLONNES; col++) {
                int piece = JoueurVsJoueur.plateau[lig][col];

                //drapeaux
                if (piece == 12) drapeauJ1Present = true;
                if (piece == -12) drapeauJ2Present = true;
                //pièces mobiles du joueur 1
                if (piece > 0 && piece < 11) {
                    JoueurVsJoueur.joueur1 = true;
                    if (JoueurVsJoueur.pieceBougeable(lig, col, false)) j1PeutBouger = true;
                }
                //pièces mobiles du joueur 2
                if (piece < 0 && piece > -11) {
                    JoueurVsJoueur.joueur1 = false;
                    if (JoueurVsJoueur.pieceBougeable(lig, col, false)) j2PeutBouger = true;
                }
            }
        }

        //remet l'état du joueur comme au debut apres tchek des pieces
        JoueurVsJoueur.joueur1 = joueurActuel;

        //conditions victoire
        if (!drapeauJ1Present || !j1PeutBouger) {//si j1 a plusde drapeau ou qu'il a plus de pions bougeable

            if (humainJoueur1){
                System.out.println( "\u001B[34m" + "" +
                        "########  ######## ########  ########  ##     ## \n" +
                        "##     ## ##       ##     ## ##     ## ##     ## \n" +
                        "##     ## ##       ##     ## ##     ## ##     ## \n" +
                        "########  ######   ########  ##     ## ##     ## \n" +
                        "##        ##       ##   ##   ##     ## ##     ## \n" +
                        "##        ##       ##    ##  ##     ## ##     ## \n" +
                        "##        ######## ##     ## ########   #######  " +
                        "\u001B[0m\n");
            }
            else{
                System.out.println( "\u001B[34m" +
                        "##     ## ####  ######  ########  #######  #### ########  ######## \n" +
                        "##     ##  ##  ##    ##    ##    ##     ##  ##  ##     ## ##       \n" +
                        "##     ##  ##  ##          ##    ##     ##  ##  ##     ## ##       \n" +
                        "##     ##  ##  ##          ##    ##     ##  ##  ########  ######   \n" +
                        " ##   ##   ##  ##          ##    ##     ##  ##  ##   ##   ##       \n" +
                        "  ## ##    ##  ##    ##    ##    ##     ##  ##  ##    ##  ##       \n" +
                        "   ###    ####  ######     ##     #######  #### ##     ## ######## "
                        +"\u001B[0m\n");
            }

            JoueurVsJoueur.menuFinPartie();
            return true;
        }

        if (!drapeauJ2Present || !j2PeutBouger) {//meme conditions
            if (humainJoueur1){
                System.out.println( "\u001B[34m" +
                        "##     ## ####  ######  ########  #######  #### ########  ######## \n" +
                        "##     ##  ##  ##    ##    ##    ##     ##  ##  ##     ## ##       \n" +
                        "##     ##  ##  ##          ##    ##     ##  ##  ##     ## ##       \n" +
                        "##     ##  ##  ##          ##    ##     ##  ##  ########  ######   \n" +
                        " ##   ##   ##  ##          ##    ##     ##  ##  ##   ##   ##       \n" +
                        "  ## ##    ##  ##    ##    ##    ##     ##  ##  ##    ##  ##       \n" +
                        "   ###    ####  ######     ##     #######  #### ##     ## ######## "
                        +"\u001B[0m\n");
            }
            else{
                System.out.println( "\u001B[34m" +
                        "########  ######## ########  ########  ##     ## \n" +
                        "##     ## ##       ##     ## ##     ## ##     ## \n" +
                        "##     ## ##       ##     ## ##     ## ##     ## \n" +
                        "########  ######   ########  ##     ## ##     ## \n" +
                        "##        ##       ##   ##   ##     ## ##     ## \n" +
                        "##        ##       ##    ##  ##     ## ##     ## \n" +
                        "##        ######## ##     ## ########   #######  "
                        +"\u001B[0m\n");
            }

            JoueurVsJoueur.menuFinPartie();
            return true;
        }
        return false;
    }

}
