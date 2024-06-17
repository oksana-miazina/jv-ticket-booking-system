package mate.academy;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketBookingSystem {
    private static final String NO_SEATS_MSG = "No seats available.";
    private static final String SUCCESS_MSG = "Booking successful.";

    private final Semaphore semaphore;
    private final AtomicInteger availableSeats = new AtomicInteger(0);

    public TicketBookingSystem(int totalSeats) {
        this.availableSeats.set(totalSeats);
        semaphore = new Semaphore(totalSeats, true);
    }

    public BookingResult attemptBooking(String user) {
        BookingResult result = new BookingResult(user, false, NO_SEATS_MSG);
        try {
            semaphore.acquire();
            if (availableSeats.get() > 0) {
                result = new BookingResult(user, true, SUCCESS_MSG);
                availableSeats.decrementAndGet();
            }
            semaphore.release();
        } catch (InterruptedException e) {
            System.out.println("Something went wrong for " + user);
        }
        return result;
    }
}
