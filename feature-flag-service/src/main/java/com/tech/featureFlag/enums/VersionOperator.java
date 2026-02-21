package com.tech.featureFlag.enums;


public enum VersionOperator {

    GREATER_THAN(">"),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN("<"),
    LESS_THAN_EQUAL("<="),
    EQUAL("=="),
    NOT_EQUAL("!=");

    private final String symbol;

    VersionOperator(String symbol) {
        this.symbol = symbol;
    }

    public static VersionOperator fromSymbol(String symbol) {
        for (VersionOperator op : values()) {
            if (op.symbol.equals(symbol)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Invalid operator: " + symbol);
    }

    public boolean evaluate(int comparison) {
        return switch (this) {
            case GREATER_THAN -> comparison > 0;
            case GREATER_THAN_EQUAL -> comparison >= 0;
            case LESS_THAN -> comparison < 0;
            case LESS_THAN_EQUAL -> comparison <= 0;
            case EQUAL -> comparison == 0;
            case NOT_EQUAL -> comparison != 0;
        };
    }
}