package eu.wilkolek.trading.bitbay;

public interface SingleIndicator<T> {

    T indicate(TickDataProvider data);

}
