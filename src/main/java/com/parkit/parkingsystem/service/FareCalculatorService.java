package com.parkit.parkingsystem.service;

import static com.parkit.parkingsystem.constants.Fare.FREE_PARKING_TIME_PERIOD_IN_MINUTES;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

  public void calculateFare(Ticket ticket) {
    if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
      throw new IllegalArgumentException(
          "Out time provided is incorrect:" + ticket.getOutTime().toString());
    }

    long inHour = ticket.getInTime().getTime();
    long outHour = ticket.getOutTime().getTime();

    // duration in minutes
    long duration = (outHour - inHour) / 1000 / 60;
    System.out.println("duration of parking " + duration + " minutes");

    if (duration <= FREE_PARKING_TIME_PERIOD_IN_MINUTES) {
      ticket.setPrice(0);
    } else {
      switch (ticket.getParkingSpot().getParkingType()) {
        case CAR: {
          ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR / 60);

          break;
        }
        case BIKE: {
          ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR / 60);
          break;
        }
        default:
          throw new IllegalArgumentException("Unkown Parking Type");
      }
    }
    if (ticket.isRecurringCustomer()) {
      System.out.println("Thank you for coming again");
      ticket.setPrice(ticket.getPrice() * 0.95);
    }
  }
}