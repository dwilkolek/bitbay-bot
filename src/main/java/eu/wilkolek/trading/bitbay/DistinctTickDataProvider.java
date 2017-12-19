package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.dto.marketdata.Ticker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class DistinctTickDataProvider implements TickDataProvider {
    LinkedBlockingQueue<Ticker> tickers = new LinkedBlockingQueue();
    Integer limit = 100;

    Ticker lastTick;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    DistinctTickDataProvider(int limit) {
        this.limit = limit;
    }

    @Override
    public void addTicker(Ticker ticker) {
        synchronized (this) {

            if (lastTick != null){
                if (lastTick.getAsk().compareTo(ticker.getAsk()) == 0 && lastTick.getBid().compareTo(ticker.getBid()) == 0) {
                    //System.out.println(sdf.format(new Date()) + ": not distinct");
                    return;
                }
            }


            this.lastTick = ticker;
            if (this.tickers.size() == 0) {
                this.tickers.offer(ticker);
            } else if (this.tickers.size() >= limit) {
                this.tickers.poll();
            }

            try {
                this.tickers.put(ticker);
            } catch (Exception e) {
                this.tickers.offer(ticker);
            }

            //System.out.println(sdf.format(new Date()) + ": " + this.tickers.size() + "of" + limit);
        }
    }


    @Override
    public synchronized List<Ticker> getData(int numberOfLastTickers) {
        Object[] objects = tickers.toArray();
        int offset = objects.length - numberOfLastTickers;


        ArrayList<Ticker> tempTickers = new ArrayList<>();
        int offsetCounter = 0;
        for (Object o : objects) {
            if (offset > 0 && offsetCounter != offset) {
                offsetCounter++;
            } else {
                tempTickers.add((Ticker) o);
            }
        }

        return tempTickers;
    }

    @Override
    public Ticker getLast() {
        return lastTick;
    }
}
