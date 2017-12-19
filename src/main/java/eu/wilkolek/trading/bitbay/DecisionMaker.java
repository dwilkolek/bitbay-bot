package eu.wilkolek.trading.bitbay;

import java.math.BigDecimal;

public interface DecisionMaker {

    boolean shouldBuy();
    boolean shouldSell(BigDecimal boughAtPrice);
//    boolean shouldRescueMoney(BigDecimal boughAtPrice);
    BigDecimal getBuyPrice();
    BigDecimal getSellPrice();

}
