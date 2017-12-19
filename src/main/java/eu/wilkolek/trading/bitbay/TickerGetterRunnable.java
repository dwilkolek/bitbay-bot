package eu.wilkolek.trading.bitbay;


import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TickerGetterRunnable implements Runnable {

    Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    MarketDataService marketDataService;
    CurrencyPair currencyPair;
    TickDataProvider dataProvider;

    public TickerGetterRunnable(MarketDataService marketDataService, CurrencyPair currencyPair, TickDataProvider dataProvider) {
        this.marketDataService = marketDataService;
        this.currencyPair = currencyPair;
        this.dataProvider = dataProvider;
    }

    @Override
    public void run() {
            Ticker ticker = null;
            while (true) {
                try {
                    ticker = marketDataService.getTicker(currencyPair);
                    log.info(ticker.toString());
                    dataProvider.addTicker(ticker);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }
}
