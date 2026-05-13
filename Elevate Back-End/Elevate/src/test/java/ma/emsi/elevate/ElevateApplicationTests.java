package ma.emsi.elevate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Classe de tests d'intégration pour vérifier le chargement du contexte Spring Boot.
 *
 * <p>Ce test vérifie que l'application démarre correctement et que le contexte Spring
 * se charge sans erreur. Il s'agit du test de "fumée" (smoke test) de base généré
 * automatiquement par Spring Initializr.</p>
 *
 * <p>Pour exécuter ce test, une base de données MySQL doit être disponible et configurée
 * dans {@code application.properties}.</p>
 */
@SpringBootTest
class ElevateApplicationTests {

    /**
     * Vérifie que le contexte Spring Boot se charge correctement.
     * Ce test échouera si un bean est mal configuré ou si une dépendance circulaire
     * non gérée est détectée.
     */
    @Test
    void contextLoads() {
    }

}
