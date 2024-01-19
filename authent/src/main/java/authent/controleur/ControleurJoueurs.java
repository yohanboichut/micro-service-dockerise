package authent.controleur;

import authent.modele.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@RestController
@RequestMapping(value = "/authent")
public class ControleurJoueurs {


    @Autowired
    FacadeJoueur facadeJoueurs;



    @PostMapping(value = "/inscription")
    public ResponseEntity<String> inscription(@RequestParam String pseudo, @RequestParam String mdp) {
        try {
            this.facadeJoueurs.inscription(pseudo,mdp);
            return ResponseEntity.ok("Compte créé !");
        } catch (PseudoDejaPrisException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Pseudo "+pseudo+" déjà pris");
        }
    }


    @DeleteMapping(value = "/inscription/{pseudo}")
    public ResponseEntity<String> desinscription(@PathVariable String pseudo, @RequestParam String mdp) {
        try {
            this.facadeJoueurs.desincription(pseudo,mdp);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/pileouface/joueur/"+pseudo)).DELETE()
                    .build();

            HttpResponse response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==HttpStatus.OK.value()) {
                return ResponseEntity.ok("Compte supprimé !");
            }
            else {
                return ResponseEntity.ok("Compte supprimé mais soucis " +
                        "côté pileouface !");
            }

        } catch (OperationNonAutorisee e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Le mot de passe n'est pas correct !");
        } catch (JoueurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mauvaises informations transmises !");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (InterruptedException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }



    @PostMapping(value = "/token")
    public ResponseEntity<String> creationToken(@RequestParam String pseudo, @RequestParam String mdp){

        try {
            String token = this.facadeJoueurs.genererToken(pseudo,mdp);
            return ResponseEntity.status(HttpStatus.OK).header("token",token).body("Token généré disponible dans le header !");
        } catch (JoueurInexistantException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mauvais identifiants !");
        } catch (OperationNonAutorisee operationNonAutorisee) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mauvais mot de passe");
        }

    }

    @GetMapping(value = "/token")
    public ResponseEntity<String> checkToken(@RequestParam String token){
        try {
            return ResponseEntity.ok(this.facadeJoueurs.checkToken(token));
        } catch (MauvaisTokenException e) {
            return ResponseEntity.notFound().build();
        }
    }





}
