package finquest.finquest.model.tm;

public class UserFinancialInstitutionAssociationTM {
    private String userInstitutionId;
    private String userId;
    private String institutionId;
    private String createdAt;
    private String updatedAt;

    // Constructors
    public UserFinancialInstitutionAssociationTM() {
    }

    public UserFinancialInstitutionAssociationTM(String userInstitutionId, String userId, String institutionId, String createdAt, String updatedAt) {
        this.userInstitutionId = userInstitutionId;
        this.userId = userId;
        this.institutionId = institutionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getUserInstitutionId() {
        return userInstitutionId;
    }

    public void setUserInstitutionId(String userInstitutionId) {
        this.userInstitutionId = userInstitutionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString method
    @Override
    public String toString() {
        return "UserFinancialInstitutionAssociationTM{" +
                "userInstitutionId='" + userInstitutionId + '\'' +
                ", userId='" + userId + '\'' +
                ", institutionId='" + institutionId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
