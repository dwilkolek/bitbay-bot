package eu.wilkolek.trading.bitbay;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CCIDroppingDecisionMaker implements DecisionMaker {


    CCIIndicator buyCCiIndicator;
    DroppingIndicator droppingIndicator;
    TickDataProvider dataProvider;
    ProfitCalculator profitCalculator = new ProfitCalculator();
    BigDecimal minimalProfit;

    public CCIDroppingDecisionMaker(BigDecimal minimalProfit,CCIIndicator buyCCiIndicator, DroppingIndicator droppingIndicator, TickDataProvider dataProvider) {
        this.buyCCiIndicator = buyCCiIndicator;
        this.droppingIndicator = droppingIndicator;
        this.dataProvider = dataProvider;
        this.minimalProfit = minimalProfit;
    }

    @Override
    public boolean shouldBuy() {
        BigDecimal cci = buyCCiIndicator.indicate(this.dataProvider);

        if (cci!= null) {
            return cci.compareTo(new BigDecimal(-150)) < 0;
        }

        return false;
    }

    @Override
    public boolean shouldSell(BigDecimal boughAtPrice) {
        return profitCalculator.profit(boughAtPrice, getSellPrice())
                && droppingIndicator.indicate(this.dataProvider)
                && getSellPrice().divide(getBuyPrice(), 2, RoundingMode.HALF_EVEN).compareTo(minimalProfit) > 0;
    }


    @Override
    public BigDecimal getBuyPrice() {
        return dataProvider.getLast().getAsk().divide(new BigDecimal(1), 2, RoundingMode.HALF_EVEN)
                .subtract(new BigDecimal(0.01)).divide(new BigDecimal(1), 2, RoundingMode.HALF_EVEN);
    }

    @Override
    public BigDecimal getSellPrice() {
        return dataProvider.getLast().getBid().divide(new BigDecimal(1), 2, RoundingMode.HALF_EVEN)
                .add(new BigDecimal(0.01)).divide(new BigDecimal(1), 2, RoundingMode.HALF_EVEN);
    }


}
