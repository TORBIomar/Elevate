package ma.emsi.elevate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l'application Elevate.
 *
 * <p>Elevate est une plateforme de recrutement permettant aux candidats de postuler à des offres
 * d'emploi et aux recruteurs de gérer leurs offres, candidatures et entretiens.</p>
 *
 * <p>Cette classe sert de point d'entrée pour l'application Spring Boot. Elle active
 * la configuration automatique, le scan des composants et la configuration Spring.</p>
 *
 * @author Équipe EMSI
 * @version 1.0
 */
@SpringBootApplication
public class ElevateApplication {

    /**
     * Méthode principale qui lance l'application Spring Boot.
     *
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        SpringApplication.run(ElevateApplication.class, args);
    }

}
