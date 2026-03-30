package es.daw.pixayapi.dto.response;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        byte[] profilePicture,
        String description,
        int followersCount,
        int followingCount
) {}