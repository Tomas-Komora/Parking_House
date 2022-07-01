package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.entities.CarType;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.response.CarTypeResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ParkingSpotResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ReservationResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.CarTypeDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ParkingSpotDto;

import java.util.ArrayList;
import java.util.List;

public class ParkingSpotFactory implements ResponseFactory<ParkingSpot, ParkingSpotDto>{

    CarTypeFactory carTypeFactory = new CarTypeFactory();
    @Override
    public ParkingSpotDto transformToDto(ParkingSpot entity) {
        ParkingSpotDto parkingSpotDto = new ParkingSpotDto();
        parkingSpotDto.setId(entity.getId());
        parkingSpotDto.setCarPark(entity.getCarParkId());
        parkingSpotDto.setIdentifier(entity.getSpotName());
        parkingSpotDto.setCarParkFloor(entity.getCarParkFloor().getFloorName());
        parkingSpotDto.setType(new CarTypeResponse(entity.getCarType()));
        parkingSpotDto.setFree(entity.getReserved());

        List<Reservation> tmp = entity.getReservationList();
        List<ReservationResponse> finalList = new ArrayList<>();
        for (Reservation cpf:tmp) {
            finalList.add(new ReservationResponse(cpf));
        }
        parkingSpotDto.setReservations(finalList);
        return parkingSpotDto;
    }

    public ParkingSpotResponse transformToResponce(ParkingSpot entity){
        ParkingSpotResponse parkingSpotDto = new ParkingSpotResponse();
        parkingSpotDto.setId(entity.getId());
        parkingSpotDto.setCarPark(entity.getCarParkId());
        parkingSpotDto.setIdentifier(entity.getSpotName());
        parkingSpotDto.setCarParkFloor(entity.getCarParkFloor().getFloorName());
        parkingSpotDto.setType(new CarTypeDto(entity.getCarType()));
        parkingSpotDto.setFree(entity.getReserved());

        List<Reservation> tmp = entity.getReservationList();
        List<ReservationResponse> finalList = new ArrayList<>();
        for (Reservation cpf:tmp) {
            finalList.add(new ReservationResponse(cpf));
        }
        parkingSpotDto.setReservations(finalList);
        return parkingSpotDto;
    }


    @Override
    public ParkingSpot transformToEntity(ParkingSpotDto dto) {
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setId(dto.getId());
        parkingSpot.setSpotName(dto.getIdentifier());
        parkingSpot.setCarParkFloorName(dto.getCarParkFloor());
        parkingSpot.setCarType(new CarType(dto.getType().getName()));
        parkingSpot.setCarParkId(dto.getCarPark());
        parkingSpot.setReserved(dto.getFree());

        List<ReservationResponse> tmp = dto.getReservations();
        List<Reservation> finalList = new ArrayList<>();
        for (ReservationResponse cpf:tmp) {
            finalList.add(new Reservation(cpf.getCar(), cpf.getSpot()));
        }
        parkingSpot.setReservationList(finalList);

        return parkingSpot;
    }
}
