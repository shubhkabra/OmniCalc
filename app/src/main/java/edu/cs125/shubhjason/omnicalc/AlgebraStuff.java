package edu.cs125.shubhjason.omnicalc;

import android.util.Log;

import net.objecthunter.exp4j.*;

import java.util.ArrayList;
import java.util.Collections;

public class AlgebraStuff {


    public static ArrayList<Double> solve(String[] sides, boolean trig) {
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
            initVals = new double[101];
            int index = 0;
            for (double i = -5.0; i <= 5.0; i += 0.2) {
                initVals[index++] = Math.pow(i, 5);
            }
        }
        ArrayList<Double> solutions = new ArrayList<Double>();
        Expression leftExpr, rightExpr;
        try {
            leftExpr = new ExpressionBuilder(sides[0].trim())
                    .variables("x")
                    .build();
            rightExpr = new ExpressionBuilder(sides[1].trim())
                    .variables("x")
                    .build();

            int numSolns = 0;
            for (double initCond : initVals) {
                double x0 = initCond;
                double fx, fxLeft, fxRight, derivFx;
                double xThis = x0, xNext = x0;
                int iter = 0;
                boolean soln = true;

                do {
                    iter++;
                    xThis = xNext;
                    leftExpr.setVariable("x", xThis);
                    rightExpr.setVariable("x", xThis);
                    fx = leftExpr.evaluate() - rightExpr.evaluate();
                    Log.d("Algebra", "initCond: " + x0 + " fx: " + fx + " iter: " + iter);
                    if (fx == 0) {
                        break;
                    }

                    leftExpr.setVariable("x", xThis - .005);
                    rightExpr.setVariable("x", xThis - .005);
                    fxLeft = leftExpr.evaluate() - rightExpr.evaluate();

                    leftExpr.setVariable("x", xThis + .005);
                    rightExpr.setVariable("x", xThis + .005);
                    fxRight = leftExpr.evaluate() - rightExpr.evaluate();

                    derivFx = (fxRight - fxLeft) / .01;
                    //System.out.println("deriv: " + derivFx);
                    xNext = xThis - (fx / derivFx);
                    //System.out.println("xThis: " + xThis + " xNext: " + xNext);
                } while (Math.abs(xNext - xThis) > .000001 && iter < 100);
                if (iter < 100) {
                    if (numSolns == 0 && !Double.isNaN(xNext)) {
                        if (!trig || (xNext >= 0 && xNext <= 2 * Math.PI)) {
                            xNext = Math.round(xNext * 100000d) / 100000d;
                            Log.d("Algebra: ", "x = " + xNext);
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
        /*if (solutions.size() == 0) {
            System.out.println("No Solution");
        } else {
            System.out.print("x = " + solutions.get(0));
            for (int i = 1; i < solutions.size(); i++) {
                System.out.print(", " + solutions.get(i));
            }
            System.out.println();
        }*/
    }
}
