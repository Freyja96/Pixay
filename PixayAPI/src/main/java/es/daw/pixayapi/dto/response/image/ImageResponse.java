package es.daw.pixayapi.dto.response.image;

public record ImageResponse (
    Long id,
    String title,
    byte[] content,
    Long userId,
    String category
) {}