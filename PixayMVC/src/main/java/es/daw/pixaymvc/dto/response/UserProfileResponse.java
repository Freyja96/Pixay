package es.daw.pixaymvc.dto.response;


public record UserProfileResponse(
        Long id,
        String username,
        String email,
        byte[] profilePicture,
        String description,
        int followersCount,
        int followingCount,

        // Visibilidad de Perfil
        boolean publicProfile,
        boolean SearchPrivacy,

        // Permisos
        boolean commentsPrivacy,
        boolean permitirDescargas,

        String fotoAjustes


) {
    // pintar foto de perfil
    public String getFotoBase64() {
        return profilePicture != null ? java.util.Base64.getEncoder().encodeToString(profilePicture) : null;
    }
}