package es.daw.pixaymvc.dto.response;

import java.util.Base64;

public record ImageResponse(
        Long id,
        String title,
        byte[] content,
        Long userId,
        String category,
        String subcategory
) {
    public String getContentBase64() {
        if (content == null || content.length == 0) return "";
        return Base64.getEncoder().encodeToString(content);
    }
}