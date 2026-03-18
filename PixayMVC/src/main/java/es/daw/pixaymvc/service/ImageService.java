package es.daw.pixaymvc.service;

import es.daw.pixaymvc.entity.Image;
import es.daw.pixaymvc.entity.User;
import es.daw.pixaymvc.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void saveImage(byte[] imageBytes, User user) {
        Image image = new Image();

        image.setContent(imageBytes); // Set al array de bytes
        image.setUserId(user); // Vincular --> usuario logueado
        imageRepository.save(image);
    }
}