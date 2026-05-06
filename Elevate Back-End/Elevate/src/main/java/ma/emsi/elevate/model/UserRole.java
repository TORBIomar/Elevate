package ma.emsi.elevate.model;

public enum UserRole {
    CANDIDATE("Candidate"),
    RECRUITER("Recruiter"),
    ADMIN("Admin");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

