//package eu.wilkolek.trading.bitbay;
//
//import org.knowm.xchange.currency.Currency;
//import org.knowm.xchange.currency.CurrencyPair;
//import org.knowm.xchange.dto.marketdata.Ticker;
//import org.testng.annotations.BeforeMethod;
//
//import java.math.BigDecimal;
//
//public class CCIBuySellIndicatorTest {
//
//    CCIBuySellIndicator indicator;
//
//    @BeforeMethod
//    private void beforeMethod() {
//        indicator = new CCIBuySellIndicator(5);
//    }
//
//    @org.testng.annotations.Test
//    public void test() throws Exception {
//
////                Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3604, ask=3630, high=3821.59, low=3082.2,avg=null, volume=867.08035325, quoteVolume=null, timestamp=null]
//        String lines[] = data.split("\n");
//
//
//        for (String line: lines) {
//            int startBid = line.indexOf("bid=");
//            int endBid = line.indexOf(",", startBid);
//            BigDecimal bid = new BigDecimal(Double.parseDouble(line.substring(startBid+4, endBid)));
//
//            int startAsk = line.indexOf("ask=");
//            int endAsk = line.indexOf(",", startAsk);
//            BigDecimal ask = new BigDecimal(Double.parseDouble(line.substring(startAsk+4, endAsk)));
//
//
//
//            Ticker ticker = new Ticker.Builder().ask(ask).bid(bid).currencyPair(new CurrencyPair(Currency.DASH, Currency.PLN)).build();
//            indicator.addTicker(ticker);
//        }
//
//    }
//
//
//    private final String data = "Ticker [currencyPair=DASH/PLN, open=null, last=3620, bid=3607.61, ask=3630, high=3821.59, low=3082.2,avg=null, volume=867.05280504, quoteVolume=null, timestamp=null]\n" +
//            "Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3607.61, ask=3630, high=3821.59, low=3082.2,avg=null, volume=867.05280504, quoteVolume=null, timestamp=null]\n" +
//            "Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3607.61, ask=3610, high=3821.59, low=3082.2,avg=null, volume=867.05280504, quoteVolume=null, timestamp=null]\n" +
//            "Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3604, ask=3630, high=3821.59, low=3082.2,avg=null, volume=867.05280504, quoteVolume=null, timestamp=null]\n" +
//            "Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3604, ask=3630, high=3821.59, low=3082.2,avg=null, volume=867.08035325, quoteVolume=null, timestamp=null]\n" +
//            "Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3604, ask=3640, high=3821.59, low=3082.2,avg=null, volume=867.08035325, quoteVolume=null, timestamp=null]\n" +
//            "Ticker [currencyPair=DASH/PLN, open=null, last=3630, bid=3604, ask=3610, high=3821.59, low=3082.2,avg=null, volume=867.08035325, quoteVolume=null, timestamp=null]\n" ;
//}