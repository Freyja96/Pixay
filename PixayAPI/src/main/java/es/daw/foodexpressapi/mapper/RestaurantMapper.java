package es.daw.foodexpressapi.mapper;

import es.daw.foodexpressapi.dto.RestaurantDTO;
import es.daw.foodexpressapi.entity.Restaurant;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantDTO dto);
    RestaurantDTO toDTO(Restaurant restaurant);

    List<RestaurantDTO> toDTOs(List<Restaurant> restaurants);

    List<Restaurant> toEntities(List<RestaurantDTO> dtos);
}
