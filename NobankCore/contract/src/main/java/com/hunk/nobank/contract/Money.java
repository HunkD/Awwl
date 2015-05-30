package com.hunk.nobank.contract;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 */
public class Money {
    private BigDecimal value;

    public Money(String value) {
        this.value = new BigDecimal(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public String string() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);
        return df.format(value);
    }
}
