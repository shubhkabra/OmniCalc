package edu.cs125.shubhjason.omnicalc;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.mathcollection.Calculus;

public class AlgebraStuff {
    public static double[] solve(String[] sides) {
        if (sides == null || sides.length < 2) {
            return null;
        }
        double[] solns = new double[5];
        Expression expr = new Expression(sides[0] + " - (" + sides[1] + ")");
        Argument x = new Argument("x");
        solns[0] = Calculus.solveBrent(expr, x, -10, 10, .0001, 100);
        return solns;
    }
}
