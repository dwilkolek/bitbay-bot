package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.dto.marketdata.Ticker;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class DroppingIndicatorTest {

    @Test
    public void testNotDropping() {
        DroppingIndicator indicator = new DroppingIndicator(new BigDecimal(0.97), 3, new ValueTickerExtractor() {
            @Override
            public BigDecimal extract(Ticker ticker) {
                return ticker.getBid();
            }
        });


        DistinctTickDataProvider distinctTickDataProvider = new DistinctTickDataProvider(3);
        distinctTickDataProvider.addTicker(new Ticker.Builder().bid(new BigDecimal(3200)).ask(new BigDecimal(102)).build());

        distinctTickDataProvider.addTicker(new Ticker.Builder().bid(new BigDecimal(3105)).ask(new BigDecimal(102)).build());

        distinctTickDataProvider.addTicker(new Ticker.Builder().bid(new BigDecimal(3115)).ask(new BigDecimal(102)).build());

        Assert.assertFalse(indicator.indicate(distinctTickDataProvider));
    }


    @Test
    public void testDropping() {
        DroppingIndicator indicator = new DroppingIndicator(new BigDecimal(0.97), 3, new ValueTickerExtractor() {
            @Override
            public BigDecimal extract(Ticker ticker) {
                return ticker.getBid();
            }
        });


        DistinctTickDataProvider distinctTickDataProvider = new DistinctTickDataProvider(3);
        distinctTickDataProvider.addTicker(new Ticker.Builder().bid(new BigDecimal(3200)).ask(new BigDecimal(102)).build());

        distinctTickDataProvider.addTicker(new Ticker.Builder().bid(new BigDecimal(3103)).ask(new BigDecimal(102)).build());

        distinctTickDataProvider.addTicker(new Ticker.Builder().bid(new BigDecimal(3115)).ask(new BigDecimal(102)).build());

        Assert.assertTrue(indicator.indicate(distinctTickDataProvider));
    }

}