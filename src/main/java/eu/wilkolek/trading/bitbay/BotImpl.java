package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.orders.DefaultOpenOrdersParamCurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BotImpl implements Bot {

    Logger log = LoggerFactory.getLogger(this.getClass().getCanonicalName());

    private final TradeService tradeService;
    private final DecisionMaker decisionMaker;
    private final MarketDataService marketDataService;
    private final TickDataProvider dataProvider;

    private final RateLimitedExchange rateLimitedExchange;
    final private CurrencyPair currencyPair;

    private int stopBuyingTimeout = 10 * 60 * 1000;

    private boolean buyingMode = true;
    private boolean sellingMode = false;
    private BigDecimal amount;
    private BigDecimal price;

    private final BigDecimal initialInvestement;

    private Order buyOrder;
    private Order sellOrder;
    private Order stopOrder;


    public BotImpl(CurrencyPair currencyPair, BigDecimal initialInvestement, Exchange exchange, DecisionMaker decisionMaker, TickDataProvider dataProvider) {
        this.decisionMaker = decisionMaker;
        this.currencyPair = currencyPair;

        rateLimitedExchange = new RateLimitedExchange(1100, exchange);
        marketDataService = rateLimitedExchange;
        tradeService = rateLimitedExchange;
        this.dataProvider = dataProvider;

        this.initialInvestement = initialInvestement;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new TickerGetterRunnable(marketDataService, currencyPair, dataProvider));

    }


    @Override
    public void process() throws Exception {
        log.info("BOT STARTED");
        while (true) {
            if (buyingMode) {
                if (decisionMaker.shouldBuy()) {
                    price = decisionMaker.getBuyPrice();
                    amount = initialInvestement.divide(price, 8, RoundingMode.HALF_DOWN);
                    buyOrder = new LimitOrder(Order.OrderType.BID, amount, currencyPair, "", null, price);
                    String buyId = tradeService.placeLimitOrder((LimitOrder) buyOrder);
                    buyOrder = findLimitOrderById(buyId);
                    Long limitOrderStart = new Date().getTime();
                    log.info("BUY:" + buyOrder);
                    while (!buyOrder.getStatus().equals(Order.OrderStatus.FILLED)) {
                        //System.out.println("buy check loop");
                        buyOrder = findLimitOrderById(buyId);
                        log.info("BUYING: "+ buyOrder);
                        log.warn(buyOrder.toString());
                        if (!decisionMaker.shouldBuy()) {
                            //System.out.println("cancel loop");
                            log.warn("CANCEL:" + buyOrder);
                            tradeService.cancelOrder(buyOrder.getId());
                            buyOrder = findLimitOrderById(buyId);
                            break;
                        }
                        Thread.sleep(10 * 10000);
                    }
                    
                    amount = buyOrder.getCumulativeAmount();
                    log.info("BOUGHT:" + amount);
                    if (amount.compareTo(new BigDecimal(0)) > 0) {
                        log.info("BUY -> SELL");
                        price = ((LimitOrder) buyOrder).getLimitPrice();
                        sellingMode = true;
                        buyingMode = false;
                        log.info("BUY -> SELL @"+price);                        
                    }
                }
            } else if (sellingMode) {
                if (decisionMaker.shouldSell(price)) {
                    //System.out.println("sell loop");
                    sellOrder = new LimitOrder(Order.OrderType.ASK, amount, currencyPair, "", null, decisionMaker.getSellPrice());
                    log.info("SELL: "+sellOrder);
                    String sellId = tradeService.placeLimitOrder((LimitOrder) sellOrder);
                    while (!sellOrder.getStatus().equals(Order.OrderStatus.FILLED)) {
                        log.info("SELLING: "+sellOrder);
                        sellOrder = findLimitOrderById(sellId);
//                        Ticker selloutTicker = marketDataService.getTicker(currencyPair);
//                        decisionMaker.addTicker(selloutTicker);
//                        if (decisionMaker.shouldRescueMoney(price)) {
//                            //System.out.println("rescue loop");
//                            sellOrder = tradeService.getOpenOrders(new DefaultOpenOrdersParamCurrencyPair()).getOpenOrders().get(0);
//                            if (!Order.OrderStatus.FILLED.equals(sellOrder.getStatus())) {
//                                tradeService.cancelOrder(sellOrder.getId());
//                                stopOrder = new MarketOrder(Order.OrderType.EXIT_ASK, sellOrder.getRemainingAmount(), currencyPair);
//                                log.info(stopOrder);
//                                tradeService.placeMarketOrder(stopOrder);
//                            }
//                            break;
//                        }
                    }

                    log.info("SOLD:" + sellOrder);

                    String profit = "Profit: \n";
                    if (buyOrder != null) {
                        profit += buyOrder + "\n";
                    }
                    if (sellOrder != null) {
                        profit += sellOrder + "\n";
                    }
                    if (buyOrder != null) {
                        profit += buyOrder + "\n";
                    }
                    profit += "----------------------------------------\n";
                    log.info(profit);
                    amount = null;
                    price = null;
                    sellingMode = false;
                    buyingMode = true;
                    buyOrder = null;
                    sellOrder = null;
                    stopOrder = null;
                    log.info("SELL -> BUY @"+((LimitOrder) sellOrder).getLimitPrice());
                }

                log.info("HUNTING");
            }

            Thread.sleep(300);
        }


    }

    private Order findLimitOrderById(String id) throws IOException {
        return tradeService.getOpenOrders(new DefaultOpenOrdersParamCurrencyPair()).getOpenOrders().stream().filter(limitOrder -> limitOrder.getId().equals(id)).findFirst().get();
    }
}

