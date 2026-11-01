abstract class Shape{
    public abstract double area();
    final static double PI = 3.14;
}
class Circle extends Shape{

        double rad;
        public Circle(double rad){
            this.rad = rad;
        }
        public double area(){ 
        System.out.printf(" -> Area of the Circle with radius: " + rad + " is: %.2f \n", (PI * rad * rad));
        return (PI * rad * rad);
        }
}
class Rectangle extends Shape{
        double L, W;
        public Rectangle(double L, double W){
            this.L = L;
            this.W = W;
        }
        public double area(){
        System.out.println(" -> Area of the Rectangle with Length: " + L + " and Width: " + W + " is: " + (L*W));
        return (L * W);
        }
}
class Triangle extends Shape{
    double base, height;
    public Triangle(double base, double height){
        this.base = base;
        this.height = height;
    }
        public double area(){
            System.out.println(" -> Area of the Triangle with Base: " + base + " and Height: " + height + " is: " + (0.5 * base * height));
            return (0.5 * base * height);
        }
    }

public class TestShape{
    public static void main(String[] args){
        System.out.println("\n");
        Shape[] shapes = {
            new Circle(5.4),
            new Rectangle(10, 20),
            new Triangle(5, 10),
        };
        double runningTotal = 0;
        double maxArea = 0;
        Shape largestShape = null;
        for(Shape s2 : shapes){
            double area = s2.area();
            if(area>maxArea){
                maxArea = area;
                largestShape = s2;
            }
            runningTotal += area;
        }

        System.out.printf("\n Running total: %.2f", runningTotal);
        if(largestShape!=null){
            System.out.printf("\n Largest Shape: %s (Area: %.2f)", largestShape.getClass().getSimpleName(), maxArea);
        }
        System.out.println("\n");
    }
}
