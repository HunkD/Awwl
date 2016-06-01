package com.hunk.nobank.contract;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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
        // TODO: apply pattern $####.##
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        if (numberFormat instanceof DecimalFormat) {
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.setMinimumFractionDigits(2);
            decimalFormat.setMaximumFractionDigits(2);
            decimalFormat.setGroupingUsed(false);
            return decimalFormat.format(value);
        }
        return "";
    }
}
