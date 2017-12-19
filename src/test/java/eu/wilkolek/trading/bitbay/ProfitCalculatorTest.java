package eu.wilkolek.trading.bitbay;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

public class ProfitCalculatorTest {


    @Test
    public void test() {
        ProfitCalculator profitCalculator = new ProfitCalculator();
        Assert.assertTrue(profitCalculator.profit(new BigDecimal(100), new BigDecimal(105)));
        Assert.assertTrue(profitCalculator.profit(new BigDecimal(100), new BigDecimal(101)));
        Assert.assertFalse(profitCalculator.profit(new BigDecimal(3200), new BigDecimal(3205)));
        Assert.assertFalse(profitCalculator.profit(new BigDecimal(3200), new BigDecimal(3199)));
    }

}