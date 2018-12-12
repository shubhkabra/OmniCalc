package edu.cs125.shubhjason.omnicalc;

import android.util.Log;

import net.objecthunter.exp4j.*;

import Jama.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public static String findDerivative(String expr, char variable, int degree) {
        if (expr == null || expr.length() == 0 || variable == ' ' || degree < 1) {
            return null;
        }
        if (degree == 1) {
            return "0";
        }
        double[] initVals = new double[51];
        int index = 0;
        for (double i = -5.0; i <= 5.0; i += 0.2) {
            initVals[index++] = Math.pow(i, 5);
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
        double[][] A = new double[51][degree];
        double[][] B = new double[51][1];
        int ind = 0;
        for (double x : pointDerivs.keySet()) {
            for (int i = 0; i < degree; i++) {
                A[ind][i] = Math.pow(x, i);
            }
            B[ind++][0] = pointDerivs.get(x);
        }
        Matrix matA = new Matrix(A);
        Matrix matB = new Matrix(B);
        Matrix normA = matA.transpose().times(matA);
        Matrix normB = matA.transpose().times(matB);
        double[][] soln = normA.solve(normB).getArray();
        String buildDeriv = "";
        for (int i = degree - 1; i >= 0; i--) {
            if (i > 1) {
                buildDeriv += soln[i][0] + "x^" + degree + " + ";
            } else if (i == 1) {
                buildDeriv += soln[i][0] + "x" + " + ";
            } else {
                buildDeriv += soln[i][0];
            }
        }
        return buildDeriv;
    }
}
