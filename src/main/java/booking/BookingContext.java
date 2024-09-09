package booking;

import booking.Booking;

/**
 * A context class allows different behaviour to be displayed at run time depending on the object passed
 * into the constructor.
 */
public class BookingContext {
    private Booking booking;

    /**
     * Constructor for BookingContext
     * @param newBooking the booking object.
     */
    public BookingContext(Booking newBooking) {
        booking = newBooking;
    }

    /**
     * Different types of bookings would be created depending on the object passed into the constructor.
     */
    public void addBookingToDatabase() throws Exception {
        booking.createBooking();
    }
}
