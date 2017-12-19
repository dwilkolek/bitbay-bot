package eu.wilkolek.trading.bitbay;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.bitbay.BitbayExchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class App {

    public static void main(String[] args) throws IOException {
        FileOutputStream dash = null;
        try {
            ExchangeSpecification exSpec = new BitbayExchange().getDefaultExchangeSpecification();
            exSpec.setUserName("username");
            exSpec.setApiKey("key");
            exSpec.setSecretKey("secret");
            Exchange bitbay = ExchangeFactory.INSTANCE.createExchange(exSpec);

            File dash_pln = new File("dash_pln.log");
            dash = new FileOutputStream(dash_pln);

            CurrencyPair currencyPair = new CurrencyPair(Currency.DASH, Currency.PLN);
            BigDecimal investement = new BigDecimal(4);
            TickDataProvider tickDataProvider = new DefaultTickDataProvider(40*60+1);

            CCIIndicator cci = new CCIIndicator(20*60, new AvarageBidAskValueExtractor());
            DroppingIndicator dropping = new DroppingIndicator(new BigDecimal(0.98),10*60, new AvarageBidAskValueExtractor());


            CCIDroppingDecisionMaker decisionMaker = new CCIDroppingDecisionMaker(
             new BigDecimal(1.03), cci, dropping, tickDataProvider
            );

           new BotImpl(currencyPair, investement, bitbay, decisionMaker, tickDataProvider).process();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dash != null) {
                dash.flush();
                dash.close();
            }
        }


// Get the account information
//        AccountService accountService = bitbay.getAccountService();
//        AccountInfo accountInfo = accountService.getAccountInfo();
//        System.out.println(accountInfo.toString());
    }


}
