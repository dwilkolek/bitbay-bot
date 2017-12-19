package eu.wilkolek.trading.bitbay;


import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class DroppingIndicator implements SingleIndicator<Boolean> {


    Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    final BigDecimal dropMargin;
    ValueTickerExtractor extractor;
    int period;

    DroppingIndicator(BigDecimal dropMargin, int period, ValueTickerExtractor extractor) {
        this.dropMargin = dropMargin;
        this.extractor = extractor;
        this.period = period;
    }

    @Override
    public Boolean indicate(TickDataProvider data) {
        List<Ticker> tickers = data.getData(period);
        BigDecimal max = null;
        BigDecimal min = null;
        for (Ticker t : tickers) {
            BigDecimal value = extractor.extract(t);
            if (max == null) {
                max = value;
            }
            if (min == null) {
                min = value;
            }

            if (value.compareTo(max) > 0) {
                max = value;
            }
            if (value.compareTo(min) < 0) {
                min = value;
            }
        }
        BigDecimal indicator = max.multiply(dropMargin);
        log.info("Dropping indicator:" + indicator + " > " + min + " " + data.getLast());
        return indicator.compareTo(min) > 0;

    }
}
