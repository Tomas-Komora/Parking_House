package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;

import sk.stuba.fei.uim.vsa.pr2.response.ParkingSpotResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkFloorDto;

import java.util.ArrayList;
import java.util.List;

public class CarParkFloorFactory implements ResponseFactory<CarParkFloor, CarParkFloorDto> {
    @Override
    public CarParkFloorDto transformToDto(CarParkFloor entity) {
        CarParkFloorDto carParkFloorDto = new CarParkFloorDto();
        carParkFloorDto.setId(entity.getId());
        carParkFloorDto.setIdentifier(entity.getFloorName());
        carParkFloorDto.setCarPark(entity.getCarPark().getId());

        List<ParkingSpot> tmp = entity.getParkingSpotList();
        List<ParkingSpotResponse> finalList = new ArrayList<>();
        for (ParkingSpot cpf:tmp) {
            finalList.add(new ParkingSpotResponse(cpf));
        }
        carParkFloorDto.setSpots(finalList);

        //carParkFloorDto.setSpots(entity.getParkingSpotList());
        return carParkFloorDto;
    }

    @Override
    public CarParkFloor transformToEntity(CarParkFloorDto dto) {
        CarParkFloor carParkFloor = new CarParkFloor();
        carParkFloor.setId(dto.getId());
        carParkFloor.setFloorName(dto.getIdentifier());
        carParkFloor.setCarParkId(dto.getCarPark());

        List<ParkingSpotResponse> tmp = dto.getSpots();
        List<ParkingSpot> finalList = new ArrayList<>();
        for (ParkingSpotResponse cpf:tmp) {
            finalList.add(new ParkingSpot(cpf.getCarPark(), cpf.getCarParkFloor(), cpf.getIdentifier()));
        }
        carParkFloor.setParkingSpotList(finalList);

        return carParkFloor;
    }
}
