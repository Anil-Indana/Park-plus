package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setAddress(address);
        parkingLot.setName(name);
        return parkingLotRepository1.save(parkingLot);
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = new Spot();
        SpotType spotType = null;
        if(numberOfWheels == 2) spotType = SpotType.TWO_WHEELER;
        else if(numberOfWheels == 4) spotType = SpotType.FOUR_WHEELER;
        else spotType = SpotType.OTHERS;
        spot.setSpotType(spotType);
        spot.setOccupied(false);
        spot.setPricePerHour(pricePerHour);
        spot.setParkingLot(parkingLot);
        parkingLot.getSpotList().add(spot);

       // parkingLotRepository1.save(parkingLot); // saves parkinglot and spot

        return spotRepository1.save(spot);
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot = spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);
        spotRepository1.deleteById(spotId);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot spot = spotRepository1.findById(spotId).get();
        ParkingLot parkingLot  = spot.getParkingLot();
        parkingLot.getSpotList().remove(spot);
        spot.setPricePerHour(pricePerHour);
        parkingLot.getSpotList().add(spot);
        return spotRepository1.save(spot);
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spots = parkingLot.getSpotList();
        for(Spot spot : spots){
            spotRepository1.deleteById(spot.getId());
        }
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
