package authent.modele;

public interface FacadeJoueur {
    /**
     * Inscription d'un nouveau joueur à la POFOL
     *
     * @param nouveauJoueur
     * @param mdp
     * @throws PseudoDejaPrisException
     */
    void inscription(String nouveauJoueur, String mdp) throws PseudoDejaPrisException;

    /**
     * Connexion à POFOL
     *
     * @param pseudo
     * @param mdp
     * @return
     * @throws JoueurInexistantException
     */
    String genererToken(String pseudo, String mdp) throws
            JoueurInexistantException, OperationNonAutorisee;


    /**
     * Permet de se désinscrire de la plate-forme
     * @param pseudo
     * @param mdp
     * @throws JoueurInexistantException
     */
    void desincription(String pseudo, String mdp) throws JoueurInexistantException, OperationNonAutorisee;


    /**
     * Permet de récupérer le pseudo du joueur possédant ce token
     * @param token
     * @return le pseudo correspondant au token
     * @throws MauvaisTokenException
     */
    String checkToken(String token) throws MauvaisTokenException;
}
