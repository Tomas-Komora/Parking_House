package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.response.CarParkFloorResponce;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;

import java.util.ArrayList;
import java.util.List;


public class CarParkFactory implements ResponseFactory<CarPark, CarParkDto> {

    @Override
    public CarParkDto transformToDto(CarPark entity) {
        CarParkDto carParkDto = new CarParkDto();
        carParkDto.setId(entity.getId());
        carParkDto.setName(entity.getName());
        carParkDto.setAddress(entity.getAddress());
        carParkDto.setPrices(entity.getPrice());

        List<CarParkFloor> tmp = entity.getList_car_park_floor();
        List<CarParkFloorResponce> finalList = new ArrayList<>();
        for (CarParkFloor cpf:tmp) {
            finalList.add(new CarParkFloorResponce(cpf));
        }
        carParkDto.setFloors(finalList);

        return carParkDto;
    }

    @Override
    public CarPark transformToEntity(CarParkDto dto) {
        CarPark carPark = new CarPark();
        carPark.setId(dto.getId());
        carPark.setName(dto.getName());
        carPark.setAddress(dto.getAddress());
        carPark.setPrice(dto.getPrices());

        List<CarParkFloorResponce> tmp = dto.getFloors();
        List<CarParkFloor> finalList = new ArrayList<>();
        for (CarParkFloorResponce cpf:tmp) {
            finalList.add(new CarParkFloor(cpf.getId(),cpf.getIdentifier()));
        }
        carPark.setList_car_park_floor(finalList);

        return carPark;
    }
}
