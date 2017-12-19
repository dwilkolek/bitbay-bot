package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.dto.marketdata.Ticker;

import java.math.BigDecimal;

public interface ValueTickerExtractor {

    BigDecimal extract(Ticker ticker);


}
