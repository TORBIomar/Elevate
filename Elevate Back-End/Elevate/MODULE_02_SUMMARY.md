# MODULE 02: Job Market (Offers & Search)

## Structure Built:
- **Model**: `JobOffer`
- **Repository**: `JobOfferRepository`
- **DTOs**: `JobOfferRequest`, `JobOfferResponse`
- **Mapper**: `JobOfferMapper`
- **Service**: `JobOfferService` and `JobOfferServiceImpl`
- **Controller**: `JobOfferController`

## Dependencies added:
- `spring-boot-starter-validation` (jakarta.validation API added to pom.xml)

## Details:
- The recruiter can create, update, delete job offers.
- Any authenticated user can get a job offer, search using keywords, category and view paginated jobs.
- The recruiter and candidates are filtered appropriately.

