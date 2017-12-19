package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.dto.marketdata.Ticker;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AvarageBidAskValueExtractor implements ValueTickerExtractor {
    @Override
    public BigDecimal extract(Ticker ticker) {
        return ticker.getBid().add(ticker.getAsk()).divide(new BigDecimal(2), 2, RoundingMode.HALF_EVEN);
    }
}
