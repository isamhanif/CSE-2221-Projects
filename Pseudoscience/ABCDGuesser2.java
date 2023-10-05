import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * De Jager Formula.
 *
 * @author Isam Hanif
 *
 */
public final class ABCDGuesser2 {
    /**
     * No argument constructor--private to prevent instantiation.
     */
    private ABCDGuesser2() {
    }

    /**
     *
     * @param in
     *            - the input stream
     * @param out
     *            - the output stream
     * @return - a positive real number entered by the user
     */
    private static double getPositiveDouble(SimpleReader in, SimpleWriter out) {
        String constant;
        double realConstant = 0;
        boolean isPositiveRealNumber = false;
        out.print("Please enter a positive real value: ");
        constant = in.nextLine();

        while (!isPositiveRealNumber) {
            if (FormatChecker.canParseDouble(constant)
                    && Double.parseDouble(constant) > 0) {
                realConstant = Double.parseDouble(constant);
                isPositiveRealNumber = true;
            } else {
                out.print("Please enter a positive real value: ");
                constant = in.nextLine();
            }
        }
        return realConstant;
    }

    /**
     *
     * @param in
     *            - the input stream
     * @param out
     *            - the output stream
     * @return - a positive real number not equal to 1.0 entered by the user
     */
    private static double getPositiveDoubleNotOne(SimpleReader in,
            SimpleWriter out) {
        String w;

        double realW = 0;
        boolean meetsRequirements = false;

        out.print("Please enter a personal number: ");
        w = in.nextLine();

        while (!meetsRequirements) {
            if (FormatChecker.canParseDouble(w) && Double.parseDouble(w) > 0
                    && Double.parseDouble(w) != 1.0) {
                realW = Double.parseDouble(w);
                meetsRequirements = true;
            } else {
                out.print("Please enter a personal number: ");
                w = in.nextLine();
            }
        }
        return realW;
    }

    /**
     *
     * @param closestGuess
     *            - the closest estimate from the for loops
     * @param constant
     *            - the user inputed constant
     * @return - returns the relative error of the estimate vs the constant
     */

    private static double relativeError(double closestGuess, double constant) {
        double realRelativeError;
        final int oneHundred = 100;

        realRelativeError = Math.abs((closestGuess - constant) / constant)
                * oneHundred;

        return realRelativeError;

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        double constant;
        double w;
        double x;
        double y;
        double z;
        double relativeError;

        constant = getPositiveDouble(in, out);
        w = getPositiveDoubleNotOne(in, out);
        x = getPositiveDoubleNotOne(in, out);
        y = getPositiveDoubleNotOne(in, out);
        z = getPositiveDoubleNotOne(in, out);

        double closestGuess = 0;
        double firstGuess = 0;
        double bestW = 0;
        double bestX = 0;
        double bestY = 0;
        double bestZ = 0;

        final double[] exponents = { -5, -4, -3, -2, -1, -1.0 / 2, -1.0 / 3,
                -1.0 / 4, 0, 1.0 / 4, 1.0 / 3, 1.0 / 2, 1, 2, 3, 4, 5 };

        for (int a = 0; a < exponents.length; a++) {
            double numberOne = (Math.pow(w, exponents[a]));
            for (int b = 0; b < exponents.length; b++) {
                double numberTwo = (Math.pow(x, exponents[b]));
                for (int c = 0; c < exponents.length; c++) {
                    double numberThree = (Math.pow(y, exponents[c]));
                    for (int d = 0; d < exponents.length; d++) {
                        double numberFour = (Math.pow(z, exponents[d]));
                        firstGuess = numberOne * numberTwo * numberThree
                                * numberFour;
                        if (Math.abs(constant - firstGuess) < Math
                                .abs(constant - closestGuess)) {
                            closestGuess = firstGuess;
                            bestW = exponents[a];
                            bestX = exponents[b];
                            bestY = exponents[c];
                            bestZ = exponents[d];
                        }
                    }
                }
            }
        }

        out.print("Closest guess: ");
        out.print(closestGuess);
        out.println("");

        out.print("Exponents used: ");
        out.print(bestW);
        out.print(" ");
        out.print(bestX);
        out.print(" ");
        out.print(bestY);
        out.print(" ");
        out.print(bestZ);
        out.println("");

        relativeError = relativeError(closestGuess, constant);

        out.print("Error: ");
        out.print(relativeError);
        out.print("%");
        in.close();
        out.close();
    }
}
