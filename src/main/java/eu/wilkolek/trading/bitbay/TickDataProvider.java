package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.dto.marketdata.Ticker;

import java.util.List;

public interface TickDataProvider {

    public void addTicker(Ticker ticker);

    List<Ticker> getData(int numberOfLastTickers);

    Ticker getLast();



    }
