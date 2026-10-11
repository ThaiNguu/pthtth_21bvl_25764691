package bai4;

import java.math.BigDecimal;
import java.math.MathContext;

public class CalculatorService {

    public static String calculate(String request) {

        if (request == null || request.trim().isEmpty()) {
            return "ERR INVALID_FORMAT";
        }

        String[] parts = request.trim().split("\\s+");

        if (parts.length != 4
                || !parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        String operator = parts[1];

        if (!operator.equals("+")
                && !operator.equals("-")
                && !operator.equals("*")
                && !operator.equals("/")) {
            return "ERR UNSUPPORTED_OPERATOR";
        }

        BigDecimal a;
        BigDecimal b;

        try {
            a = new BigDecimal(parts[2]);
            b = new BigDecimal(parts[3]);
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }

        if (operator.equals("/") && b.compareTo(BigDecimal.ZERO) == 0) {
            return "ERR DIVIDE_BY_ZERO";
        }

        BigDecimal result;

        switch (operator) {
            case "+":
                result = a.add(b);
                break;

            case "-":
                result = a.subtract(b);
                break;

            case "*":
                result = a.multiply(b);
                break;

            case "/":
                result = a.divide(b, MathContext.DECIMAL64);
                break;

            default:
                return "ERR UNSUPPORTED_OPERATOR";
        }

        return "OK " + result.stripTrailingZeros().toPlainString();
    }
}