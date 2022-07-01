package sk.stuba.fei.uim.vsa.pr2.factories;

import sk.stuba.fei.uim.vsa.pr2.entities.Car;
import sk.stuba.fei.uim.vsa.pr2.entities.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.entities.Reservation;
import sk.stuba.fei.uim.vsa.pr2.response.CarResponse;
import sk.stuba.fei.uim.vsa.pr2.response.ParkingSpotResponse;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.response.dto.ReservationDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class ReservationFactory implements ResponseFactory<Reservation, ReservationDto>{
    @Override
    public ReservationDto transformToDto(Reservation entity) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(entity.getId());
        reservationDto.setStart(format.format(entity.getStartDate()));
        if (entity.getEndDate()!=null)
            reservationDto.setEnd(format.format(entity.getEndDate()));


        //reservationDto.setStart(entity.getStartDate());
        //reservationDto.setEnd(entity.getEndDate());
        reservationDto.setPrices(entity.getCena());

        if(entity.getParkingSpot().getId() != null)
            reservationDto.setSpot(entity.getParkingSpot().getId());
        else {
            reservationDto.setSpot(0L);
        }
        if(entity.getCar().getId()!=null)
            reservationDto.setCar(entity.getCar().getId());
        else {
            reservationDto.setCar(0L);
        }
        return reservationDto;

    }

    @Override
    public Reservation transformToEntity(ReservationDto dto) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        Instant timestamp = null;
        //reservation.setStartDate(format.parse(dto.getStart()));
        Date dateStart= new Date();
        Date dateEnd = new Date();
        try {
            dateStart = format.parse(dto.getStart());
            System.out.println(dateStart);
            dateEnd = format.parse(dto.getEnd());
            System.out.println(dateEnd);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        reservation.setStartDate(dateStart);
        reservation.setEndDate(dateEnd);
        //reservation.setStartDate(dto.getStart());
        //reservation.setEndDate(dto.getEnd());
        reservation.setCena(dto.getPrices());
        if(dto.getSpot()!=null)
            reservation.setParkingSpotId(dto.getSpot());
        else {
            reservation.setParkingSpotId(0L);
        }
        if(dto.getCar()!=null)
            reservation.setCarId(dto.getCar());
        else {
            reservation.setCarId(0L);
        }

        return reservation;
    }
}
