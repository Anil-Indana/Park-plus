package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {

        Reservation reservation = reservationRepository2.findById(reservationId).get();
        PaymentMode paymentMode = null;
        mode = mode.toUpperCase();
        if(mode.equals("CARD")) paymentMode = PaymentMode.CARD;
        else if(mode.equals("UPI")) paymentMode = PaymentMode.UPI;
        else if(mode.equals("CASH")) paymentMode = PaymentMode.CASH;
        if(paymentMode == null) throw new Exception("Payment mode not detected");

        int amountRequired = reservation.getNumberOfHours()*reservation.getSpot().getPricePerHour();
        if(amountRequired > amountSent) throw new Exception("Insufficient Amount");

        Payment payment = new Payment();
        payment.setPaymentMode(paymentMode);
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        return paymentRepository2.save(payment);
    }
}
