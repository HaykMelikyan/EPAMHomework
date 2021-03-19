import org.testng.*;
import org.testng.annotations.*;

public class CalculatorTest {
    
    // Positive cases
    @Test(description = "Addition positive test")
    public void additionTest() {
        double actualResult = Calculator.addition(15.3, 2.8);
        double expectedResult = 15.3 + 2.8;
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test(description = "Subtraction positive test")
    public void subtractionTest() {
        double actualResult = Calculator.subtraction(15.3, 2.8);
        double expectedResult = 15.3 - 2.8;
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test(description = "Multiplication positive test")
    public void multiplicationTest() {
        double actualResult = Calculator.multiplication(15.3, 2.8);
        double expectedResult = 15.3 * 2.8;
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test(description = "Division positive test")
    public void divisionTest() {
        double actualResult = Calculator.division(15.3, 2.8);
        double expectedResult = 15.3 / 2.8;
        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test(description = "Division by zero exception", expectedExceptions = ArithmeticException.class)
    public void divisionByZeroTest() {
        Calculator.division(15.0, 0.0);
    }


    // Negative cases
    @Test(description = "Addition negative test")
    public void additionNegTest() {
        double actualResult = Calculator.addition(15.3, 2.8);
        double expectedResult = 15.3 + 2.8 + 1;
        Assert.assertNotEquals(actualResult, expectedResult);
    }

    @Test(description = "Subtraction negative test")
    public void subtractionNegTest() {
        double actualResult = Calculator.subtraction(15.3, 2.8);
        double expectedResult = 15.3 - 2.8 + 1;
        Assert.assertNotEquals(actualResult, expectedResult);
    }

    @Test(description = "Multiplication negative test")
    public void multiplicationNegTest() {
        double actualResult = Calculator.multiplication(15.3, 2.8);
        double expectedResult = 15.3 * 2.8 + 1;
        Assert.assertNotEquals(actualResult, expectedResult);
    }

    @Test(description = "Division negative test")
    public void divisionNegTest() {
        double actualResult = Calculator.division(15.3, 2.8);
        double expectedResult = 15.3 / 2.8 + 1;
        Assert.assertNotEquals(actualResult, expectedResult);
    }

    // Addition test with assertTrue method
    @Test(description = "Addition positive test")
    public void additionBoolTest() {
        Double actualResult = Calculator.addition(15.3, 2.8);
        Double expectedResult = 15.3 + 2.8;
        Assert.assertTrue(actualResult.equals(expectedResult));
    }
}
