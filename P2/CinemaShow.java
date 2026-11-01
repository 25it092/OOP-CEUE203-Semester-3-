public class CinemaShow {
    private String title;
    private int seatsAvailable;
    private final int CAPACITY;
    private static int totalBooked = 0;

    public CinemaShow(String title, int CAPACITY){
        this.title = title;
        this.CAPACITY = CAPACITY;
        this.seatsAvailable = CAPACITY;
    }

    public CinemaShow(String title){
        this(title, 100);
    }

    public boolean book(int n){
        if(n <= seatsAvailable){
            seatsAvailable = seatsAvailable - n;
            totalBooked = totalBooked + n;
            return true;
        }
        else{
            return false;
        }
    }

    public void cancel(int n){
        seatsAvailable = seatsAvailable + n;
        totalBooked = totalBooked - n;
        if(seatsAvailable >= CAPACITY){
            seatsAvailable = CAPACITY;
            totalBooked = 0;
        }
    }

    public String getTitle(){
        return title;
    }

    public int getSeatsAvailable(){
        return seatsAvailable;
    }

    public static int getTotalBooked(){
        return totalBooked;
    }

    public static void main(String args[]){
        CinemaShow S1 = new CinemaShow("Tenet", 50);
        System.out.println("\n ->*Show 1 created with title: " + S1.getTitle() + " and seats available are: " + S1.getSeatsAvailable() + "*<-");
        System.out.println("\n---------------------------------------------------------------------------------------------");

        System.out.println("\n ->Booking 15 seats for show 1:- ");
        boolean result1 = S1.book(15);
        System.out.println(" " + result1 + ", remaining seats are: " + S1.getSeatsAvailable());

        System.out.println("\n ->Booking 40 more seats for show 1:- ");
        boolean result2 = S1.book(40);
        System.out.println(" " + result2 + ", remaining seats are: " + S1.getSeatsAvailable());

        System.out.println("\n ->Canceling 5 seats for show 1:- ");
        S1.cancel(5);
        System.out.println(" Remaining seats after cancellation of 5 seats: " + S1.getSeatsAvailable());

        /*System.out.println("\n ->Trying to cancel 60 seats(more than the capacity)");
        S1.cancel(60);
        System.out.println(" Remaining seats after cancellation of 60 seats:" + S1.getSeatsAvailable());*/

        System.out.println("\n ---------------------------------------------------------------------------------------------");
        System.out.println("\n -->Total successful bookings: " + S1.getTotalBooked());
        System.out.println("\n ");
    }
}
