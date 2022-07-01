package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.CarPark;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;
import sk.stuba.fei.uim.vsa.pr2.response.CarTypeResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ReservationResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkDto;

import java.util.ArrayList;
import java.util.List;

public class CarFactory implements ResponseFactory<Car, CarDto>{
    @Override
    public CarDto transformToDto(Car entity) {
        CarDto carDto = new CarDto();
        carDto.setId(entity.getId());
        carDto.setBrand(entity.getBrand());
        carDto.setModel(entity.getModel());
        carDto.setColour(entity.getColor());
        carDto.setVrp(entity.getEcv());
        carDto.setOwner(entity.getUserId());
        carDto.setType(new CarTypeResponse(entity.getCarType()));
        List<Reservation> tmp = entity.getReservationList();
        List<ReservationResponse> finalList = new ArrayList<>();
        for (Reservation cpf:tmp) {
            finalList.add(new ReservationResponse(cpf));
        }
        carDto.setReservations(finalList);

        return carDto;
    }

    @Override
    public Car transformToEntity(CarDto dto) {
        Car car = new Car();
        car.setId(dto.getId());
        car.setCarType(new CarType(dto.getType().getName()));
        car.setColor(dto.getColour());
        car.setEcv(dto.getVrp());
        car.setUserId(dto.getOwner());
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());

        List<ReservationResponse> tmp = dto.getReservations();
        List<Reservation> finalList = new ArrayList<>();
        for (ReservationResponse cpf:tmp) {
            finalList.add(new Reservation(cpf.getCar(), cpf.getSpot()));
        }
        car.setReservationList(finalList);

        return car;
    }
}
