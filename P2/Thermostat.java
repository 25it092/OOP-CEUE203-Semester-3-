public class Thermostat {
    private String location;
    private int temperature;
    private static final int MIN = 16;
    private static final int MAX = 30;
    private static int activeCount = 0;
    
    public Thermostat(String location, int startTemp){
        this.location = location;
        if(startTemp >= MIN && startTemp <= MAX){
            this.temperature = startTemp;
        }
        else{
            this.temperature = 22;
        }
        activeCount++;
    }
    public Thermostat(String location){
        this(location, 22);
    }
    public void raise(){
        if(temperature < MAX){
            temperature++;
        }
        /*else{
            System.out.println("Already at maximum(30)");
        }*/
    }
    public void lower(){
        if(temperature > MIN){
            temperature--;
        }
        /*else{
            System.out.println("Already at minimum(16)");
        }*/
    }
    public int getTemperature(){
        return temperature;
    }
    public static int getactiveCount(){
        return activeCount;
    }

    public static void main(String args[]){
        Thermostat t1 = new Thermostat("Ahmedabad", 18);
        Thermostat t2 = new Thermostat("Vadodara");

    System.out.println("\n");
    System.out.println("Temp for t1 and t2: " + t1.getTemperature() + " " + t2.getTemperature());
    System.out.println("\n -- Raising Temperature(s) for t1 -- ");
    for(int i=0;i<10;i++){
        t1.raise();
        System.out.println((i+1) + ". Temperature is: " + t1.getTemperature());

        if(t1.getTemperature() == 30){
            System.out.println("->Already at maximum(30)");
            break;
        }
    }
    
    System.out.println("\n -- Lowering Temperature(s) for t1 --");
    for(int i=0;i<20;i++){
        t1.lower();
        System.out.println((i+1) + ". Temperature is: " + t1.getTemperature());

        if(t1.getTemperature() == 16){
            System.out.println("->Already at minimum(16)");
            break;
        }
    }
    
    System.out.println("\n-->Active Count (No. of Thermostat(s): " + Thermostat.getactiveCount());
    System.out.println("\n");
    }
    
}
