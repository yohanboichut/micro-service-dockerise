package authent.modele;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component("facadeJoueurs")
public class FacadeJoueurImpl implements FacadeJoueur {


    /**
     * Dictionnaire des joueurs inscrits
     */
    private Map<String,Joueur> joueurs;

    /**
     * Dictionnaire des joueurs connectés indexés par une clé aléatoire
     */
    private Map<String,Joueur> joueursConnectes;

    public FacadeJoueurImpl() {
        this.joueurs = new HashMap<>();
        this.joueursConnectes = new HashMap<>();
    }


    @Override
    public void inscription(String nouveauJoueur, String mdp) throws PseudoDejaPrisException {

        if (joueurs.containsKey(nouveauJoueur))
            throw new PseudoDejaPrisException();

        this.joueurs.put(nouveauJoueur,new Joueur(nouveauJoueur,mdp));
    }

    private void checkIdConnexion(String idConnexion) throws MauvaisTokenException {
        if (!this.joueursConnectes.containsKey(idConnexion))
            throw new MauvaisTokenException();
    }

    /**
     * Génération d'un token pour jouer
     * @param nouveauJoueur
     * @param mdp
     * @return
     * @throws JoueurInexistantException
     */
    @Override
    public String genererToken(String nouveauJoueur, String mdp) throws
            JoueurInexistantException, OperationNonAutorisee {

        if (!joueurs.containsKey(nouveauJoueur))
            throw new JoueurInexistantException();

        Joueur j = joueurs.get(nouveauJoueur);
        if (j.checkPassword(mdp)) {
            String idConnection = UUID.randomUUID().toString();
            this.joueursConnectes.put(idConnection, j);
            return idConnection;
        }
        else {
            throw new OperationNonAutorisee();
        }
    }


    @Override
    public void desincription(String pseudo, String mdp) throws JoueurInexistantException, OperationNonAutorisee {
        if (!joueurs.containsKey(pseudo))
            throw new JoueurInexistantException();

        Joueur j = joueurs.get(pseudo);
        if (j.checkPassword(mdp)) {
            this.joueurs.remove(pseudo);
        }
        else {
            throw new OperationNonAutorisee();
        }


    }

    @Override
    public String checkToken(String token) throws MauvaisTokenException {
        if (joueursConnectes.containsKey(token)){
            return joueursConnectes.get(token).getNomJoueur();
        }
        else {
            throw new MauvaisTokenException();
        }
    }


}
