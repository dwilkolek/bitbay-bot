package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class RateLimitedExchange implements MarketDataService, TradeService, AccountService {

    MarketDataService marketDataService;
    TradeService tradeService;
    AccountService accountService;


    AtomicLong lastCall = new AtomicLong(0);
    int limitInMs = 1000;

    AtomicBoolean locked = new AtomicBoolean(false);


    public RateLimitedExchange(int limitInMs, Exchange exchange) {
        this.limitInMs = limitInMs;
        marketDataService = exchange.getMarketDataService();
        tradeService = exchange.getTradeService();
        accountService = exchange.getAccountService();
    }


    private synchronized void beforeCall() throws ExchangeException {
        try {
            waitToUnlock();
        } catch (Exception e) {
            new ExchangeException(e);
        }
        long now = System.currentTimeMillis();
        long next = lastCall.get() + limitInMs;
        if (now < next) {

            long waitto = next - now;

            try {
                Thread.sleep(waitto);
            } catch (Exception e) {
                throw new ExchangeException(e);
            }
        }

    }

    @Override
    public AccountInfo getAccountInfo() throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            AccountInfo res = accountService.getAccountInfo();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public String withdrawFunds(Currency currency, BigDecimal bigDecimal, String s) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            String res = accountService.withdrawFunds(currency, bigDecimal, s);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public String withdrawFunds(WithdrawFundsParams withdrawFundsParams) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            String res = accountService.withdrawFunds(withdrawFundsParams);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public String requestDepositAddress(Currency currency, String... strings) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            String res = accountService.requestDepositAddress(currency, strings);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public TradeHistoryParams createFundingHistoryParams() {
        try {
            beforeCall();
            TradeHistoryParams res = accountService.createFundingHistoryParams();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public List<FundingRecord> getFundingHistory(TradeHistoryParams tradeHistoryParams) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            List<FundingRecord> res = accountService.getFundingHistory(tradeHistoryParams);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public Ticker getTicker(CurrencyPair currencyPair, Object... objects) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {

        try {
            beforeCall();
            Ticker res = marketDataService.getTicker(currencyPair, objects);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair, Object... objects) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            OrderBook res = marketDataService.getOrderBook(currencyPair, objects);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public Trades getTrades(CurrencyPair currencyPair, Object... objects) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            Trades res = marketDataService.getTrades(currencyPair, objects);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public OpenOrders getOpenOrders() throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            OpenOrders res = tradeService.getOpenOrders();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public OpenOrders getOpenOrders(OpenOrdersParams openOrdersParams) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            OpenOrders res = tradeService.getOpenOrders(openOrdersParams);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public String placeMarketOrder(MarketOrder marketOrder) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            String res = tradeService.placeMarketOrder(marketOrder);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public String placeLimitOrder(LimitOrder limitOrder) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            String res = tradeService.placeLimitOrder(limitOrder);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public boolean cancelOrder(String s) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            boolean res = tradeService.cancelOrder(s);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public boolean cancelOrder(CancelOrderParams cancelOrderParams) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            boolean res = tradeService.cancelOrder(cancelOrderParams);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public UserTrades getTradeHistory(TradeHistoryParams tradeHistoryParams) throws IOException {
        try {
            beforeCall();
            UserTrades res = tradeService.getTradeHistory(tradeHistoryParams);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public TradeHistoryParams createTradeHistoryParams() {
        try {
            beforeCall();
            TradeHistoryParams res = createTradeHistoryParams();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public OpenOrdersParams createOpenOrdersParams() {
        try {
            beforeCall();
            OpenOrdersParams res = tradeService.createOpenOrdersParams();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    @Override
    public void verifyOrder(LimitOrder limitOrder) {
        try {
            beforeCall();
            tradeService.verifyOrder(limitOrder);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }
        return;
    }

    @Override
    public void verifyOrder(MarketOrder marketOrder) {
        try {
            beforeCall();
            tradeService.verifyOrder(marketOrder);
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }
        return;
    }

    @Override
    public Collection<Order> getOrder(String... strings) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException, IOException {
        try {
            beforeCall();
            Collection<Order> res = tradeService.getOrder(strings);
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            afterCall();
        }

    }

    private synchronized void waitToUnlock() throws InterruptedException {
        while (this.locked.get()) {
            Thread.sleep(200);
        }
        this.locked.set(true);
        return;
    }

    private synchronized void afterCall() {
        this.locked.set(false);
        this.lastCall.set(System.currentTimeMillis());
    }
}
