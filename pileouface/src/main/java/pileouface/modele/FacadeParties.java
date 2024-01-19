package pileouface.modele;

import java.util.Collection;

public interface FacadeParties {

    /**
     * Permet de jouer une partie
     *
     * @param idConnexio
     * @param choix
     * @return le résultat de la partie
     * @throws MauvaisIdentifiantConnexionException
     */
    Partie jouer(String idConnexio, String choix) throws MauvaisIdentifiantConnexionException;


    /**
     * Permet de récupérer les statistiques d'un joueur
     * @param idConnexion
     * @return
     * @throws MauvaisIdentifiantConnexionException
     */

    Statistiques getStatistiques(String idConnexion) throws MauvaisIdentifiantConnexionException;

    /**
     * Permet de récupérer l'historique des parties d'un joueur connecté
     *
     * @param idConnexion
     * @return
     * @throws MauvaisIdentifiantConnexionException
     */

    Collection<Partie> getAllParties(String idConnexion) throws MauvaisIdentifiantConnexionException;


    /**
     * Permet de récupérer un joueur par son pseudo s'il existe.
     * S'il n'existe pas, un nouveau joueur est créé
     * @param pseudo
     * @return
     */
    Joueur getJoueur(String pseudo);


    /**
     * Permet de supprimer un joueur du SI
     * @param pseudo
     */
    void suppressionJoueur(String pseudo);
}
