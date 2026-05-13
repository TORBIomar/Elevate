package ma.emsi.elevate.service;

import ma.emsi.elevate.dto.request.InterviewRequest;
import ma.emsi.elevate.dto.response.InterviewResponse;
import ma.emsi.elevate.model.InterviewStatus;
import java.util.List;

/**
 * Interface définissant le contrat du service de gestion des entretiens.
 *
 * <p>Permet au recruteur de planifier des entretiens, et aux utilisateurs (candidats
 * ou recruteurs) de consulter et mettre à jour le statut des entretiens qui les concernent.</p>
 */
public interface InterviewService {

    /**
     * Planifie un entretien pour une candidature donnée.
     * Réservé au recruteur propriétaire de l'offre d'emploi associée.
     * Une notification est automatiquement envoyée au candidat.
     *
     * @param request  les données de l'entretien (identifiant candidature, date, lieu/lien)
     * @param username e-mail du recruteur connecté
     * @return le DTO {@link InterviewResponse} représentant l'entretien planifié
     */
    InterviewResponse scheduleInterview(InterviewRequest request, String username);

    /**
     * Récupère la liste des entretiens de l'utilisateur connecté.
     * Si l'utilisateur est un candidat, retourne ses entretiens.
     * Si l'utilisateur est un recruteur, retourne les entretiens liés à ses offres.
     *
     * @param username e-mail de l'utilisateur connecté
     * @return liste des DTOs {@link InterviewResponse}
     */
    List<InterviewResponse> getMyInterviews(String username);

    /**
     * Met à jour le statut d'un entretien.
     * Une notification est envoyée au candidat concerné.
     *
     * @param interviewId identifiant de l'entretien
     * @param status      nouveau statut à appliquer (SCHEDULED, COMPLETED ou CANCELED)
     * @param username    e-mail de l'utilisateur connecté
     * @return le DTO {@link InterviewResponse} mis à jour
     */
    InterviewResponse updateInterviewStatus(Long interviewId, InterviewStatus status, String username);
}

