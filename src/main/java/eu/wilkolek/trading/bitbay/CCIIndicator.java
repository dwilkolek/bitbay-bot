package eu.wilkolek.trading.bitbay;

import com.google.gson.Gson;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

public class CCIIndicator implements SingleIndicator<BigDecimal> {

    Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    private final BigDecimal f = new BigDecimal(0.015).divide(new BigDecimal(1), 3, RoundingMode.HALF_EVEN);

    private int period;
    private BigDecimal CCI;
    private ValueTickerExtractor extractor;


    public CCIIndicator(int period, ValueTickerExtractor extractor) {
        this.period = period;
        this.extractor = extractor;
    }

    @Override
    public BigDecimal indicate(TickDataProvider data) {
        List tickers = data.getData(period);
        doMath(tickers);
        if (CCI != null){
            log.info("CCI Indicator:" + CCI + " " + data.getLast());
        }
        return CCI;
    }


    //
//    FORMULA
//            CCI = ( M - A ) / ( 0.015 * D )
//
//    Where:
//
//    M = ( H + L + C ) /
//            3
//    H = Highest price for
//    the period
//    L = Lowest price for
//    the period
//    C = Closing price for
//    the period
//    A = n period moving
//    average of M
//            D = mean deviation
//    of the absolute value of the difference between the mean price and
//    the moving average of mean prices, M - A
//
//


    private void doMath(List<Ticker> tickers) {
        if (tickers.size() == 0 || tickers.size() < period) {
            return;
        }
        BigDecimal H = new BigDecimal(0);
        BigDecimal L = new BigDecimal(5000000);
        BigDecimal A = new BigDecimal(0);
        BigDecimal sum = new BigDecimal(0);
        BigDecimal C = new BigDecimal(0);
        BigDecimal D = new BigDecimal(0);
        BigDecimal M = new BigDecimal(0);


        Iterator<Ticker> iterator = tickers.iterator();
        while (iterator.hasNext()) {
            Ticker t = iterator.next();
            BigDecimal value = extractor.extract(t);

            if (value.compareTo(H) > 0) {
                H = value;
            }
            if (value.compareTo(L) < 0) {
                L = value;
            }
            sum = sum.add(value);
            C = value;
        }

        M = H.add(L).add(C).divide(new BigDecimal(3), 2, RoundingMode.HALF_EVEN);

        A = sum.divide(new BigDecimal(tickers.size()), 2, RoundingMode.HALF_EVEN);


        iterator = tickers.iterator();
        BigDecimal dev = new BigDecimal(0);
        while (iterator.hasNext()) {
            Ticker t = iterator.next();
            BigDecimal value = extractor.extract(t);
            dev = dev.add(value.subtract(A).abs());
        }

        D = dev.divide(new BigDecimal(tickers.size()), 2, RoundingMode.HALF_EVEN);
        if (D.compareTo(new BigDecimal(0)) == 0) {
            D = new BigDecimal(0.00000001);
        }
        this.CCI = M.subtract(A).divide(f.multiply(D), 5, RoundingMode.HALF_EVEN);

    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
