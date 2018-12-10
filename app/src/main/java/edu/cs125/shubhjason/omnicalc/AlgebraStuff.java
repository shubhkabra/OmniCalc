package edu.cs125.shubhjason.omnicalc;

import net.objecthunter.exp4j.*;

import java.util.ArrayList;
import java.util.Collections;

public class AlgebraStuff {


    public static ArrayList<Double> solve(String[] sides) {
        if (sides == null || sides.length < 2) {
            return null;
        }
        double[] initVals = new double[101];
        int index = 0;
        for (double i = -5.0; i <= 5.0; i += 0.2) {
            initVals[index++] = Math.pow(i, 5);
        }

        ArrayList<Double> solutions = new ArrayList<Double>();

        Expression leftExpr = new ExpressionBuilder(sides[0].trim())
                .variables("x")
                .build();
        Expression rightExpr = new ExpressionBuilder(sides[1].trim())
                .variables("x")
                .build();

        int numSolns = 0;
        for (double initCond: initVals) {
            double x0 = initCond;
            double fx, fxLeft, fxRight, derivFx;
            double xThis = x0, xNext = x0;

            do {
                xThis = xNext;
                leftExpr.setVariable("x", xThis);
                rightExpr.setVariable("x", xThis);
                fx = leftExpr.evaluate() - rightExpr.evaluate();

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
            } while (Math.abs(xNext - xThis) > .0001);
            if (numSolns == 0) {
                xNext = Math.round(xNext * 100000d) / 100000d;
                //System.out.print("x = " + xNext);
                solutions.add(xNext);
                numSolns++;
            } else if (!Double.isNaN(xNext)){
                xNext = Math.round(xNext * 100000d) / 100000d;
                if (!(solutions.contains(xNext))) {
                    //System.out.println(", " + xNext);
                    solutions.add(xNext);
                    numSolns++;
                }
            }
        }
        Collections.sort(solutions);
        return solutions;
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
