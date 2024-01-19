package pileouface.controleur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pileouface.modele.*;

import javax.servlet.http.Part;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;


@RestController
@RequestMapping(value = "/jeu",produces = {MediaType.APPLICATION_JSON_VALUE})
public class ControleurParties {


    @Autowired
    FacadeParties facadeParties;


    private final static String URI_PILEOUFACE= "http://localhost:8080/api/auth/token";


    @PostMapping(value = "/partie")
    public ResponseEntity<Partie> jouerPartie(@RequestHeader String token, @RequestParam String prediction ){
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(URI_PILEOUFACE+"?token="+token)).GET().build();
        try {
            HttpResponse<String> response=httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()== HttpStatus.OK.value()){
                String pseudo =response.body();
                Joueur j = facadeParties.getJoueur(pseudo);
                Partie p =j.jouer(prediction);
                ResponseEntity<Partie> responseEntity = ResponseEntity.ok(p);
                return responseEntity;
            }
            else {

                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

    }



    @GetMapping(value = "/partie")
    public ResponseEntity<Collection<Partie>> getParties(@RequestHeader String token) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(URI_PILEOUFACE+"?token="+token)).GET().build();
        try {
            HttpResponse<String> response=httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()== HttpStatus.OK.value()){
                String pseudo =response.body();
                Collection<Partie> parties = this.facadeParties.getAllParties(pseudo);
                ResponseEntity<Collection<Partie>> responseEntity = ResponseEntity.ok(parties);
                return responseEntity;
            }
            else {

                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MauvaisIdentifiantConnexionException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @GetMapping(value = "/statistiques")
    public ResponseEntity<Statistiques> getStatistiques(@RequestHeader String token) {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(URI_PILEOUFACE+"?token="+token)).GET().build();
        try {
            HttpResponse<String> response=httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode()== HttpStatus.OK.value()){
                String pseudo =response.body();
                Statistiques statistiques = this.facadeParties.getStatistiques(pseudo);
                ResponseEntity<Statistiques> responseEntity = ResponseEntity.ok(statistiques);
                return responseEntity;
            }
            else {

                return ResponseEntity.notFound().build();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MauvaisIdentifiantConnexionException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }



    @DeleteMapping(value = "/joueur/{pseudo}")
    public ResponseEntity<String> supprimerJoueur(@PathVariable String pseudo){
        this.facadeParties.suppressionJoueur(pseudo);
        return ResponseEntity.ok("Joueur supprimé");

    }





}
