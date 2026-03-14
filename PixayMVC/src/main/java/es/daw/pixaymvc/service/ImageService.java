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
        //TODO arreglar esto
        image.setContent(imageBytes); // Seteas el array de bytes
        image.setUser_id(user);          // Vinculas con el usuario logueado
        imageRepository.save(image);
    }
}