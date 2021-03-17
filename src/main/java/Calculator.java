public class Calculator {

    public static double addition(double a,double b) {
        return a + b;
    }

    public static double subtraction(double a,double b) {
        return a - b;
    }

    public static double multiplication(double a,double b) {
        return a * b;
    }

    public static double division(double a,double b) {
        Double B = b;
        if(B.equals(0.0)){
            throw new ArithmeticException();
        }
        return a / b;
    }



}
