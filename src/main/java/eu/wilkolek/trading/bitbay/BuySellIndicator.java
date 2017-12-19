package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.dto.marketdata.Ticker;

public interface BuySellIndicator<T>{

    T indicateBuy();
    T indicateSell();
    void addTicker(Ticker ticker);

}
