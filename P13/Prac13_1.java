
public class Prac13_1 {

    static class Box<T> {
        private T value;

        public void set(T value) {
            this.value = value;
        }

        public T get() {
            return value;
        }
    }

    static class Pair<A, B> {
        private A first;
        private B second;

        public Pair(A first, B second) {
            this.first = first;
            this.second = second;
        }

        public A getFirst() {
            return first;
        }

        public B getSecond() {
            return second;
        }
    }

    public static <T> T getFirstElement(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }

        return array[0];
    }

    public static void main(String[] args) {

        Box<String> stringBox = new Box<>();
        stringBox.set("Hello Java");
        System.out.println("String Box: " + stringBox.get());

        Box<Integer> intBox = new Box<>();
        intBox.set(100);
        System.out.println("Integer Box: " + intBox.get());

        Pair<String, Integer> student =
                new Pair<>("Yash", 20);

        System.out.println("\n");
        System.out.println("Student Name: " + student.getFirst());
        System.out.println("Student Age: " + student.getSecond());

        Pair<Integer, Double> product =
                new Pair<>(101, 499.50);

        System.out.println("\n");
        System.out.println("Product ID: " + product.getFirst());
        System.out.println("Product Price: " + product.getSecond());

        String[] names = {"Kanu", "Riya", "Sonu"};

        System.out.println("\n");
        System.out.println("First Name: " + getFirstElement(names));

        Integer[] numbers = {10, 20, 30};
        System.out.println("First Number: " + getFirstElement(numbers));
    }
}
