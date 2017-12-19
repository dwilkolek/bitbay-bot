package eu.wilkolek.trading.bitbay;

import java.math.BigDecimal;

public class ProfitCalculator {

    public boolean profit(BigDecimal buyPrice, BigDecimal sellPrice) {
        BigDecimal tax = new BigDecimal(1-0.0042);
        return buyPrice.compareTo(sellPrice.multiply(tax).multiply(tax))<0;
    }


}
