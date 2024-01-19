package pileouface.modele;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component("facadeParties")
public class FacadePartiesImpl implements FacadeParties {


    /**
     * Dictionnaire des joueurs connectés indexés par leur pseudo
     */
    private Map<String,Joueur> joueursActuels;

    public FacadePartiesImpl() {
        this.joueursActuels = new HashMap<>();
    }


    private void checkIdConnexion(String idConnexion) throws MauvaisIdentifiantConnexionException {
        if (!this.joueursActuels.containsKey(idConnexion))
            throw new MauvaisIdentifiantConnexionException();
    }




    @Override
    public Partie jouer(String idConnexio, String choix) throws MauvaisIdentifiantConnexionException {
        this.checkIdConnexion(idConnexio);
        Joueur j = this.joueursActuels.get(idConnexio);
        Partie partie = j.jouer(choix);
        return partie;
    }



    /**
     * Permet de récupérer les statistiques d'un utilisateur connecté
     * @param idConnexion
     * @return
     * @throws MauvaisIdentifiantConnexionException
     */
    @Override
    public Statistiques getStatistiques(String idConnexion) throws MauvaisIdentifiantConnexionException {
        this.checkIdConnexion(idConnexion);
        Joueur j = this.joueursActuels.get(idConnexion);
        int nb = j.getNbPartiesJouees();
        double ratio = (double)this.getJoueur(idConnexion).getNbPartiesGagnees()/((double)nb);
        return new Statistiques(nb,ratio);
    }



    /**
     * Permet de récupérer l'historique des parties d'un joueur connecté
     * @param idConnexion
     * @return
     * @throws MauvaisIdentifiantConnexionException
     */

    @Override
    public Collection<Partie> getAllParties(String idConnexion) throws MauvaisIdentifiantConnexionException {
        this.checkIdConnexion(idConnexion);
        return this.joueursActuels.get(idConnexion).getHistorique();
    }

    @Override
    public Joueur getJoueur(String pseudo) {

        if (joueursActuels.containsKey(pseudo)){
            return joueursActuels.get(pseudo);
        }
        Joueur j = new Joueur(pseudo);
        joueursActuels.put(pseudo,j);
        return j;
    }

    @Override
    public void suppressionJoueur(String pseudo) {
        this.joueursActuels.remove(pseudo);
    }


}
