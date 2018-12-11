package edu.cs125.shubhjason.omnicalc;

import android.util.Log;

import net.objecthunter.exp4j.*;

import java.util.ArrayList;
import java.util.Collections;

public class AlgebraStuff {


    public static ArrayList<Double> solve(String[] sides, char variable, boolean trig) {
        if (sides == null || sides.length < 2) {
            return null;
        }
        double[] initVals;
        if (trig) {
            initVals = new double[51];
            int index = 0;
            for (double i = 0; i <= 2 * Math.PI; i += (2 * Math.PI) / 50) {
                initVals[index++] = i;
            }
        } else {
            initVals = new double[51];
            int index = 0;
            for (double i = -5.0; i <= 5.0; i += 0.2) {
                initVals[index++] = Math.pow(i, 5);
            }
        }
        ArrayList<Double> solutions = new ArrayList<Double>();
        Expression leftExpr, rightExpr;
        try {
            leftExpr = new ExpressionBuilder(sides[0].trim())
                    .variables("" + variable)
                    .build();
            rightExpr = new ExpressionBuilder(sides[1].trim())
                    .variables("" + variable)
                    .build();

            int numSolns = 0;
            for (double initCond : initVals) {
                double x0 = initCond;
                double fx, fxLeft, fxRight, derivFx;
                double xThis = x0, xNext = x0;
                int iter = 0;
                do {
                    iter++;
                    xThis = xNext;
                    leftExpr.setVariable("" + variable, xThis);
                    rightExpr.setVariable("" + variable, xThis);
                    fx = leftExpr.evaluate() - rightExpr.evaluate();
                    Log.d("Algebra", "initCond: " + x0 + " fx: " + fx + " iter: " + iter);
                    if (fx == 0) {
                        break;
                    }
                    /** Approximate derivative at "xThis" */
                    leftExpr.setVariable("" + variable, xThis - .005);
                    rightExpr.setVariable("" + variable, xThis - .005);
                    fxLeft = leftExpr.evaluate() - rightExpr.evaluate();

                    leftExpr.setVariable("" + variable, xThis + .005);
                    rightExpr.setVariable("" + variable, xThis + .005);
                    fxRight = leftExpr.evaluate() - rightExpr.evaluate();

                    derivFx = (fxRight - fxLeft) / .01;
                    
                    /** Newton's Method */
                    xNext = xThis - (fx / derivFx);

                    // Until a zero is reached or it takes too long
                } while (Math.abs(xNext - xThis) > .000001 && iter < 50);
                if (iter < 50) {
                    if (numSolns == 0 && !Double.isNaN(xNext)) {
                        if (!trig || (xNext >= 0 && xNext <= 2 * Math.PI)) {
                            xNext = Math.round(xNext * 100000d) / 100000d;
                            Log.d("Algebra: ", variable + " = " + xNext);
                            solutions.add(xNext);
                            numSolns++;
                        }
                    } else if (!Double.isNaN(xNext)) {
                        if (!trig || (xNext >= 0 && xNext <= 2 * Math.PI)) {
                            xNext = Math.round(xNext * 100000d) / 100000d;
                            if (!(solutions.contains(xNext))) {
                                Log.d("Algebra: ", ", " + xNext);
                                solutions.add(xNext);
                                numSolns++;
                            }
                        }
                    }
                }
            }
            Collections.sort(solutions);
            return solutions;
        } catch (Exception e) {
            throw e;
        }
    }
}
