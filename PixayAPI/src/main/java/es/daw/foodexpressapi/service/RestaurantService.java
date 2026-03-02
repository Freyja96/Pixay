package es.daw.foodexpressapi.service;

import es.daw.foodexpressapi.dto.RestaurantDTO;
import es.daw.foodexpressapi.entity.Restaurant;
import es.daw.foodexpressapi.mapper.RestaurantMapper;
import es.daw.foodexpressapi.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                //.map(this::toDTO)
                .map(restaurantMapper::toDTO)
                .toList();
    }

    public Optional<RestaurantDTO> create(RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantMapper.toEntity(restaurantDTO);
        //Restaurant restaurant = toEntity(restaurantDTO);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return Optional.of(restaurantMapper.toDTO(savedRestaurant));
        //return Optional.of(toDTO(savedRestaurant));
    }

    /**
     * sin gestionar excepción "NoExisteRestauranteException"
     * @param id
     * @return
     */
    public boolean delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            return false;
        }

        restaurantRepository.deleteById(id);
        return true;
    }

    /**
     * El servicio no devuelve un Optional...
     * @param id
     * @param restaurantDTO
     * @return
     */
    public RestaurantDTO update(Long id, RestaurantDTO restaurantDTO) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el restaurante con id: " + id));

        restaurant.setName(restaurantDTO.getName());
        restaurant.setAddress(restaurantDTO.getAddress());
        restaurant.setPhone(restaurantDTO.getPhone());

        Restaurant updated = restaurantRepository.save(restaurant);

        return restaurantMapper.toDTO(updated);
    }
    
    //-----------------------------------------------------------------------------
//    public Restaurant toEntity(RestaurantDTO dto) {
//        Restaurant restaurant = new Restaurant();
//        restaurant.setName(dto.getName());
//        restaurant.setAddress(dto.getAddress());
//        restaurant.setPhone(dto.getPhone());
//        return restaurant;
//    }
//    public RestaurantDTO toDTO(Restaurant restaurant) {
//        RestaurantDTO dto = new RestaurantDTO();
//        dto.setName(restaurant.getName());
//        dto.setAddress(restaurant.getAddress());
//        dto.setPhone(restaurant.getPhone());
//        return dto;
//    }
}
