package driver;
import externals.ComplexNum;
import externals.RootsOfUnity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class PolynomFFTCalculator {

    public static void displayPoly(ArrayList<ComplexNum> Poly) { // Handles the display of polynomials in coefficient representation
        boolean firstCoeff = true;
        for (int i = Poly.size()-1; i >= 0; i--) {
            // Numbers and its signs
            if (Math.abs(Math.round(Poly.get(i).re())) < 1) { // Will not print number if smaller than 1
                System.out.print("");
            } else if (firstCoeff) { // Only applies for number which appears first in the polynomial
                if (Math.round(Poly.get(i).re()) > 1) { // Will print number without the plus sign if number greater than 1
                    System.out.print(String.format("%d", Math.round(Poly.get(i).re())));
                    firstCoeff = false;
                } else if (Math.round(Poly.get(i).re()) == 1) { // Will not print anything if number equal to 1
                    System.out.print("");
                    firstCoeff = false;
                } else if (Math.round(Poly.get(i).re()) == -1) { // Will only print the minus sign if number equal to -1
                    System.out.print("-");
                    firstCoeff = false;
                } else { // Will print negative number if number smaller than -1
                    System.out.print(String.format("%d", Math.round(Poly.get(i).re())));
                    firstCoeff = false;
                }
            } else if (Math.round(Poly.get(i).re()) > 1 || i == 0 && Math.round(Poly.get(i).re()) == 1) { // Will print positive integer and its sign if greater than 1 OR for constants (x^0)
                System.out.print(" + ");
                System.out.print(String.format("%d", Math.round(Poly.get(i).re())));
            } else if (Math.round(Poly.get(i).re()) == 1) { // Will only print the positive sign if number equal to 1
                System.out.print(" + ");
            } else if (Math.round(Poly.get(i).re()) < -1 || i == 0 && Math.round(Poly.get(i).re()) == -1) { // Will print negative integer and its sign if smaller than -1 OR for constants (x^0)
                System.out.print(" - ");
                System.out.print(String.format("%d", -Math.round(Poly.get(i).re())));
            } else if (Math.round(Poly.get(i).re()) == -1) { // Will only print the negative sign if number equal to -1
                System.out.print(" - ");
            }

            // X-variables and its exponents
            if (Math.round(Poly.get(i).re()) == 0) { // Will not print variable if number equal to 0
                System.out.print("");
            } else if (i == 1) { // Will only print variable without the exponent if x^1
                System.out.print("x");
            } else if (i == 0) { // Will not print variable if x^0
                System.out.print("");
            } else { // Otherwise, the variable and its exponent will be printed
                System.out.print(String.format("x^%d", i));
            }
        }
        System.out.print("\n");
    }

    public static void displayVal(ArrayList<ComplexNum> Val) { // Handles the display of polynomials in point-value representation
        System.out.print("[");
        // All values printed will be accurate up to 3 digits
        for (int j = 0; j<Val.size();j++) {
            if (Math.round(Val.get(j).re()) > 0) { // Only applies when real component greater than 0
                if (Val.get(j).im() > 0) { // Will print real and imaginary component with plus sign if imaginary component greater than 0
                    System.out.print(String.format("%.3f", (float) Val.get(j).re()));
                    System.out.print("+");
                    System.out.print(String.format("%.3f", (float) Val.get(j).im()));
                    System.out.print("i");
                } else if (Val.get(j).im() < 0) { // Will only print real and imaginary component if imaginary component smaller than 0
                    System.out.print(String.format("%.3f", (float) Val.get(j).re()));
                    System.out.print(String.format("%.3f", (float) Val.get(j).im()));
                    System.out.print("i");
                } else if (Val.get(j).im() == 0) { // Will only print real component if imaginary component equal to 0
                    System.out.print(String.format("%.3f", (float) Val.get(j).re()));
                }
            } else { // Will only print imaginary component
                System.out.print(String.format("%.3f", (float) Val.get(j).im()));
                System.out.print("i");
            }

            if (j == Val.size()-1) { // Will print "]" after all values are printed
                System.out.print("]");
            } else { // Will print ", " after every value that is not the last value
                System.out.print(", ");
            }
        }
        System.out.print("\n");
    }

    public static double calcMaxROU(int poly1Degree, int poly2Degree) {
        // Calculates the largest roots of unity required to perform the polynomial multiplication
        double maxROU;
        if (poly1Degree+poly2Degree == Math.pow(2,Math.log(poly1Degree+poly2Degree)/Math.log(2))) {
            maxROU = Math.pow(2, Math.ceil(Math.log(poly1Degree + poly2Degree) / Math.log(2))+1);
        } else {
            maxROU = Math.pow(2, Math.ceil(Math.log(poly1Degree + poly2Degree) / Math.log(2)));
        }
        return maxROU;
    }

    public static ArrayList<ComplexNum> FFT(ArrayList<ComplexNum> polyCoeff) { // Fast Fourier Transform
        // n = len(P)
//        double temp = Math.ceil(Math.log(polyCoeff.size())/Math.log(2));
        int n = (int) Math.pow(2,Math.ceil(Math.log(polyCoeff.size())/Math.log(2)));
        // Take in an array of integers representing polynomial coefficients (n must be a power of 2)

        // if n == 1
        if (n == 1) {
            return polyCoeff;
        }
        // Base case: If array contains only one integer, return the integer without modification
        // ⍵ = e^((2pi*i)/n) has been replaced with the RootsOfUnity class instance
        // Pe,Po = P[::2],P[1::2]
        ArrayList<ComplexNum> polyEven = new ArrayList<>();
        ArrayList<ComplexNum> polyOdd = new ArrayList<>();
            // Creates empty ArrayList objects to store even degree and odd degree coefficients
        for (int j = 0; j < polyCoeff.size(); j += 2) {
            polyEven.add(polyCoeff.get(j));
        }
            // Stores even degree coefficients to Pe
        for (int j = 1; j < polyCoeff.size(); j += 2) {
            polyOdd.add(polyCoeff.get(j));
        }
            // Stores odd degree coefficients to Po
        // Separate the polynomial to even degree terms and odd degree terms

        // ye,yo = FFT(Pe),FFT(Po)
        ArrayList<ComplexNum> yEven = FFT(polyEven);
        ArrayList<ComplexNum> yOdd = FFT(polyOdd);
        // Recursively calls FFT on the even degree terms and odd degree terms of the polynomial

        // y = [0] * n
        ArrayList<ComplexNum> y = new ArrayList<>(Collections.nCopies(n,new ComplexNum()));
        // Creates a y ArrayList containing n-amount of zeroes

        // for (j in range(n/2):
        for (int j = 0; j < n/2; j++) {
            // y[j] = ye[j] + ⍵^j yo[j]
            y.set(j, yEven.get(j).plus(new RootsOfUnity(j,n).z().times(yOdd.get(j))));
            // y[j+n/2] = ye[j] - ⍵^j yo[j]
            y.set(j+(n/2), yEven.get(j).minus(new RootsOfUnity(j,n).z().times(yOdd.get(j))));
        }
        // Calculates the point-value representation of the polynomial at the nth root of unity
        return y;
    }

    public static ArrayList<ComplexNum> FFT_polynomMultiply(ArrayList<ComplexNum> leftPoly, ArrayList<ComplexNum> rightPoly) {
        ArrayList<ComplexNum> leftPolyVal = FFT(leftPoly);
        ArrayList<ComplexNum> rightPolyVal = FFT(rightPoly);
        // Calculates the Fast Fourier Transform of each polynomial

        ArrayList<ComplexNum> multVal = new ArrayList<>();
        for (int i = 0; i < leftPoly.size(); i++) {
            multVal.add(leftPolyVal.get(i).times(rightPolyVal.get(i)));
        }
        // Multiplies the point-value representations of both polynomials together

        return IFFT(multVal);
        // Call IFFT on the point-value representation product
    }

    public static ArrayList<ComplexNum> IFFT(ArrayList<ComplexNum> polyCoeff) {
        // n = len(P)
        double temp = Math.ceil(Math.log(polyCoeff.size())/Math.log(2));
        int n = (int) Math.pow(2,temp);
        // Take in an array of integers representing polynomial coefficients (n must be a power of 2)

        // if n == 1
        if (n == 1) {
            return polyCoeff;
        }
        // Base case: If array contains only one integer, return the integer without modification
        // ⍵ = e^(-(2pi*i)/n) has been replaced with the RootsOfUnity class instance
        // Pe,Po = P[::2],P[1::2]
        ArrayList<ComplexNum> polyEven = new ArrayList<>();
        ArrayList<ComplexNum> polyOdd = new ArrayList<>();
            // Creates empty ArrayList objects to store even degree and odd degree coefficients
        for (int j = 0; j < polyCoeff.size(); j += 2) {
            polyEven.add(polyCoeff.get(j));
        }
            // Stores even degree coefficients to Pe
        for (int j = 1; j < polyCoeff.size(); j += 2) {
            polyOdd.add(polyCoeff.get(j));
        }
            // Stores odd degree coefficients to Po
        // Separate the polynomial to even degree terms and odd degree terms

        // ye,yo = IFFT(Pe),IFFT(Po)
        ArrayList<ComplexNum> yEven = IFFT(polyEven);
        ArrayList<ComplexNum> yOdd = IFFT(polyOdd);
        // Recursively calls IFFT on the even degree terms and odd degree terms of the polynomial

        // y = [0] * n
        ArrayList<ComplexNum> y = new ArrayList<>(Collections.nCopies(n,new ComplexNum()));
        // Creates a y ArrayList containing n-amount of zeroes

        // for (j in range(n/2):
        for (int j = 0; j < n/2; j++) {
            // y[j] = ye[j] + ⍵^j yo[j]
            y.set(j, yEven.get(j).plus(new RootsOfUnity(j,n).z().reciprocal().times(yOdd.get(j))));
            // y[j+n/2] = ye[j] - ⍵^j yo[j]
            y.set(j+(n/2), yEven.get(j).minus(new RootsOfUnity(j,n).z().reciprocal().times(yOdd.get(j))));
        }
        // Calculates the point-value representation of the polynomial at the nth root of unity
        return y;
    }

    public static void main(String[] args) {
        Scanner userInp = new Scanner(System.in);
        int choice;
        while (true) {
            System.out.println("(1) Quick polynomial multiplication");
            System.out.println("(2) Polynomial multiplication in detail");
            System.out.println("(3) Help");
            System.out.println("(4) Exit");
            choice = userInp.nextInt();
            if (choice == 1) {
                int poly1Degree, poly2Degree;
                ArrayList<ComplexNum> poly1 = new ArrayList<>();
                ArrayList<ComplexNum> poly2 = new ArrayList<>();
                System.out.print("Enter degree of 1st polynomial: ");
                poly1Degree = userInp.nextInt();
                System.out.println("Enter coefficients of polynomial: ");
                for (int i = 0; i < poly1Degree+1; i++) {
                    poly1.add(new ComplexNum(userInp.nextInt(),0));
                }
                System.out.print("Enter degree of 2nd polynomial: ");
                poly2Degree = userInp.nextInt();
                System.out.println("Enter coefficients of polynomial: ");
                for (int i = 0; i < poly2Degree+1; i++) {
                    poly2.add(new ComplexNum(userInp.nextInt(),0));
                }
                double maxROU = calcMaxROU(poly1Degree, poly2Degree);
                // Calculates the largest roots of unity required to perform the polynomial multiplication
                double poly1Zeros = maxROU - poly1.size();
                double poly2Zeros = maxROU - poly2.size();
                for (int i = 0; i < (int) poly1Zeros; i++) {
                    poly1.add(new ComplexNum());
                }
                for (int i = 0; i < (int) poly2Zeros; i++) {
                    poly2.add(new ComplexNum());
                }
                // Add zeroes as needed until the length of each polynomial equals that of the nth root of unity
                ArrayList<ComplexNum> result = FFT_polynomMultiply(poly1,poly2);
                for (int i = 0; i<result.size(); i++) {
                    result.set(i, result.get(i).divides(new ComplexNum(maxROU,0)));
                }
                // Divides the IFFT result by the largest roots of unity

                displayPoly(result);
                // Displays the output
                System.out.print("\n");
            } else if (choice == 2) {
                int poly1Degree;
                int poly2Degree;
                ArrayList<ComplexNum> poly1 = new ArrayList<>();
                ArrayList<ComplexNum> poly2 = new ArrayList<>();
                System.out.print("Enter degree of 1st polynomial: ");
                poly1Degree = userInp.nextInt();
                System.out.println("Enter coefficients of polynomial: ");
                for (int i = 0; i < poly1Degree+1; i++) {
                    poly1.add(new ComplexNum(userInp.nextInt(),0));
                }
                System.out.print("Enter degree of 2nd polynomial: ");
                poly2Degree = userInp.nextInt();
                System.out.println("Enter coefficients of polynomial: ");
                for (int i = 0; i < poly2Degree+1; i++) {
                    poly2.add(new ComplexNum(userInp.nextInt(),0));
                }
                System.out.println("Coefficient representation of 1st polynomial: ");
                displayPoly(poly1);
                System.out.print("\n");
                System.out.println("Coefficient representation of 2nd polynomial: ");
                displayPoly(poly2);
                System.out.print("\n");
                double maxROU = calcMaxROU(poly1Degree, poly2Degree);
                double poly1Zeros = maxROU - poly1.size();
                double poly2Zeros = maxROU - poly2.size();
                for (int i = 0; i < (int) poly1Zeros; i++) {
                    poly1.add(new ComplexNum(0,0));
                }
                for (int i = 0; i < (int) poly2Zeros; i++) {
                    poly2.add(new ComplexNum(0,0));
                }
                System.out.println("Point-value representation of 1st polynomial: ");
                displayVal(FFT(poly1));
                System.out.print("\n");
                System.out.println("Point-value representation of 2nd polynomial: ");
                displayVal(FFT(poly2));
                System.out.print("\n");
                ArrayList<ComplexNum> result = FFT_polynomMultiply(poly1,poly2);
                for (int i = 0; i<result.size(); i++) {
                    result.set(i, result.get(i).divides(new ComplexNum(maxROU,0)));
                }
                System.out.println("Final result: ");
                displayPoly(result);
                System.out.print("\n");
            } else if (choice == 3) {
                System.out.println("About - This program is polynomial multiplication calculator which uses the Fast Fourier Transform.\n");
                System.out.println("Further information - https://youtu.be/h7apO7q16V0\n");
                System.out.println("Tip - When entering the coefficients for the polynomial, it will always begin from x^0 until x^n");
                System.out.println("Example: ");
                System.out.println("Enter degree of 1st polynomial: 3");
                System.out.println("Enter coefficients of polynomial: ");
                System.out.println("3000\n200\n10\n1");
                System.out.println("Actual representation: ");
                System.out.println("x^3 + 10x^2 + 200x + 3000\n");
            } else if (choice == 4) {
                break;
            }
        }
    }
}
