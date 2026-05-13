package ma.emsi.elevate.repository;

import ma.emsi.elevate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Référentiel JPA pour la gestion des utilisateurs ({@link ma.emsi.elevate.model.User}) en base de données.
 *
 * <p>Hérite de {@link JpaRepository} et fournit les opérations CRUD standard, ainsi que
 * des méthodes personnalisées pour la recherche par e-mail.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Recherche un utilisateur par son adresse e-mail.
     *
     * @param email l'adresse e-mail de l'utilisateur
     * @return un {@link Optional} contenant l'utilisateur s'il existe, vide sinon
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un utilisateur existe avec l'adresse e-mail donnée.
     *
     * @param email l'adresse e-mail à vérifier
     * @return {@code true} si un compte avec cet e-mail existe déjà
     */
    boolean existsByEmail(String email);
}

