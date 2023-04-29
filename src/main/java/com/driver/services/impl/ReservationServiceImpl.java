package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Autowired
    PaymentServiceImpl paymentService;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        ParkingLot parkingLot;
        try{
            parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        User user;
        try{
            user = userRepository3.findById(userId).get();
        }
        catch (Exception e){
            throw new Exception("Cannot make reservation");
        }
        List<Spot> spots = parkingLot.getSpots();
        SpotType spotType = null;
        if(numberOfWheels == 2) spotType = SpotType.TWO_WHEELER;
        else if(numberOfWheels == 4) spotType = SpotType.FOUR_WHEELER;
        else spotType = SpotType.OTHERS;
        Spot spot = null;
        int price = Integer.MAX_VALUE;
        for(Spot spot1 : spots){
            if(!spot1.isOccupied() && spot1.getSpotType() == spotType){
                if(spot1.getPricePerHour() < price){
                    price = spot1.getPricePerHour();
                    spot = spot1;
                }
            }
        }
        if(spot == null) throw new Exception("Cannot make reservation");

        Reservation reservation = new Reservation();
        reservation.setSpot(spot);
        reservation.setUser(user);
        reservation.setNumberOfHours(timeInHours);
        spot.setOccupied(true);
        parkingLotRepository3.save(parkingLot);
        return reservationRepository3.save(reservation);
    }
}
