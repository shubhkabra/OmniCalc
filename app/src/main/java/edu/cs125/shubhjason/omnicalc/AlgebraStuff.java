package edu.cs125.shubhjason.omnicalc;

import android.util.Log;

import net.objecthunter.exp4j.*;

import Jama.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AlgebraStuff {

    private static final int NUM_PTS_DERIV = 801;
    private static int function = 2;

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

    public static String findDerivative(String expr, char variable, int degree) {
        if (expr == null || expr.length() == 0 || variable == ' ' || degree < 0) {
            return null;
        }
        if (degree == 0) {
            return "0";
        }
        double[] initVals = new double[NUM_PTS_DERIV];
        /** Function 1 - Exponential. */
        if (function == 1) {
            int index = 0;
            for (double i = -5.0; i <= 5.0; i += 0.2) {
                initVals[index++] = Math.pow(i, 5);
            }
        }
        /** Function 2 - Linear */
        if (function == 2) {
            int index = 0;
            for (double i = -10.0; i <= 10.0; i += 0.025) {
                initVals[index++] = i * 1;
            }
        }
        /** Function 3 - Hybrid */
        if (function == 3) {
            int index = 0;
            for (double i = -5.0; i <= 5.0; i += 0.05) {
                if (i >= -1.0 && i <= 1.0) {
                    initVals[index++] = i * 1;
                } else {
                    initVals[index++] = Math.pow(i, 3);
                }
            }
        }

        Map<Double, Double> pointDerivs = new HashMap<>();
        Expression expression;
        try {
            expression = new ExpressionBuilder(expr.trim())
                    .variables("" + variable)
                    .build();

            for (double initCond : initVals) {
                double xThis = initCond;
                double fx, fxLeft, fxRight, derivFx;

                expression.setVariable("" + variable, xThis);
                fx = expression.evaluate();

                /** Approximate derivative at "xThis" */
                expression.setVariable("" + variable, xThis - .005);
                fxLeft = expression.evaluate();

                expression.setVariable("" + variable, xThis + .005);
                fxRight = expression.evaluate();

                derivFx = (fxRight - fxLeft) / .01;
                Log.d("Calculus", "initCond: " + xThis + " dfx: " + derivFx);
                pointDerivs.put(initCond, derivFx);
            }
        } catch (Exception e) {
            throw e;
        }
        double[][] A = new double[NUM_PTS_DERIV][degree];
        double[][] B = new double[NUM_PTS_DERIV][1];
        int ind = 0;
        for (double x : pointDerivs.keySet()) {
            for (int i = 0; i < degree; i++) {
                A[ind][i] = Math.pow(x, i);
            }
            B[ind++][0] = pointDerivs.get(x);
        }
        /** Do cool linear algebra least-squares things.*/
        Matrix matA = new Matrix(A);
        Matrix matB = new Matrix(B);
        Matrix normA = matA.transpose().times(matA);
        Matrix normB = matA.transpose().times(matB);
        double[][] soln = normA.solve(normB).getArray();
        for (int i = 0; i < soln.length; i++) {
            if (Math.abs(soln[i][0]) < .05) {
                soln[i][0] = 0;
            } else {
                soln[i][0] = Math.round(soln[i][0] * 100d) / 100d;
            }
            Log.d("Derivative", i + "-" + soln[i][0]);
        }
        /** Build return expression. */
        String buildDeriv = "";
        int terms = 0;
        for (int i = degree - 1; i >= 0; i--) {
            if (soln[i][0] != 0.0) {
                if (terms >= 1) {
                    if (soln[i][0] > 0) {
                        buildDeriv += " + ";
                    } else {
                        buildDeriv += " - ";
                    }
                } else if (terms == 0 && soln[i][0] < 0) {
                    buildDeriv += "-";
                }
                if (i > 1) {
                    buildDeriv += Math.abs(soln[i][0]) + "" + variable + "^" + i;
                } else if (i == 1) {
                    buildDeriv += Math.abs(soln[i][0]) + "" + variable;
                } else {
                    buildDeriv += Math.abs(soln[i][0]);
                }
                terms++;
            }
        }
        return buildDeriv;
    }
}
