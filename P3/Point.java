public class Point{
    private int x, y;
        public Point(){

        }
        public Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        public String toString(){
            return "(" + x + "," + y + ")";
        }

        public boolean equals(Object o1){
            Point sumn = (Point) o1;
            return(this.x == sumn.x && this.y == sumn.y);
        }


    public static void main(String[] args){
        Point p1 = new Point(1,2);
        Point p2 = p1;
        Point p3 = new Point(2,4);

        System.out.println("\n");
        System.out.println(p1.toString());
        System.out.println((p1.hashCode() == p2.hashCode())); // as p1 and p2 are the same references, have the same hex address
        System.out.println((p1.hashCode() == p3.hashCode()));

        System.out.println("Is P1 equal to P2?: " + p1.equals(p2));
        System.out.println("Is P1 equal to P3?: " + p1.equals(p3));

        System.out.println("\n");
    }
        
}
