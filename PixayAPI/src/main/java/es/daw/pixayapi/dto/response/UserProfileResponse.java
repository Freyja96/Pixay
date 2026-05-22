package es.daw.pixayapi.dto.response;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        byte[] profilePicture,
        String description,
        int followersCount,
        int followingCount,
        boolean isFollowing
) {
    // pintar foto de perfil
    public String getFotoBase64() {
        return profilePicture != null ? java.util.Base64.getEncoder().encodeToString(profilePicture) : null;
    }
}