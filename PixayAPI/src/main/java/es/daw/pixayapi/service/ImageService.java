package es.daw.pixayapi.service;

import es.daw.pixayapi.entity.Category;
import es.daw.pixayapi.entity.Image;
import es.daw.pixayapi.entity.User;
import es.daw.pixayapi.repository.CategoryRepository;
import es.daw.pixayapi.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    public Slice<Image> getAllImagesPaged(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return imageRepository.findAllByOrderByIdDesc(pageable);
    }

    public Slice<Image> getImagesByUser(User user, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return imageRepository.findByUser(user, pageable);
    }

    public Slice<Image> getImagesByCategory(String category, Pageable pageable){
        return imageRepository.findByCategory(category, pageable);
    }

    public Image saveImage(MultipartFile file, String title, String categoryName, String subcategory, User user){
        try {
        Category cat = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + categoryName));

        Image image = new Image();
        image.setContent(file.getBytes());
        image.setTitle(title);
        image.setCategory(cat);
        image.setSubcategory(subcategory);
        image.setUser(user);

        return imageRepository.save(image);
        } catch (IOException e){
            throw new RuntimeException("Error al leer los bytes de la imagen: ", e);
        }
    }
}