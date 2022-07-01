package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.User;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ParkingSpotResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class UserFactory implements ResponseFactory<User, UserDto>{
    @Override
    public UserDto transformToDto(User entity) {
        UserDto userDto = new UserDto();
        userDto.setId(entity.getId());
        userDto.setFirstName(entity.getName());
        userDto.setLastName(entity.getSurname());
        userDto.setEmail(entity.getEmail());
        List<Car> tmp = entity.getCarList();
        List<CarResponse> finalList = new ArrayList<>();
        for (Car cpf:tmp) {
            finalList.add(new CarResponse(cpf));
        }
        userDto.setCars(finalList);

        return userDto;
    }

    @Override
    public User transformToEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getFirstName());
        user.setSurname(dto.getLastName());
        user.setEmail(dto.getEmail());

        List<CarResponse> tmp = dto.getCars();
        List<Car> finalList = new ArrayList<>();
        for (CarResponse cpf:tmp) {
            finalList.add(new Car(cpf.getBrand(), cpf.getModel(), cpf.getColour(), cpf.getVrp()));
        }
        user.setCarList(finalList);

        return user;
    }
}
