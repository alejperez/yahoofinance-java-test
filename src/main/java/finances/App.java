package finances;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.fx.FxQuote;
import yahoofinance.quotes.fx.FxSymbols;

public class App {

    public static void main(String[] args) throws IOException {
        readStock("INTC", true);

        readSingleStockInterval("GOOG", -5);

        // If the historical quotes are not yet available, the getHistory() method will automatically send a new request to Yahoo Finance.
        Stock google = YahooFinance.get("GOOG");
        List<HistoricalQuote> googleHistQuotes = google.getHistory();

    }

    private static void readStock(String ticker, boolean refresh) throws IOException {
        Stock stock = YahooFinance.get(ticker);

        BigDecimal price = stock.getQuote(refresh).getPrice();
        BigDecimal change = stock.getQuote().getChangeInPercent();
        BigDecimal peg = stock.getStats().getPeg();
        BigDecimal dividend = stock.getDividend().getAnnualYieldPercent();

        Stock google = YahooFinance.get("GOOG");
        List<HistoricalQuote> googleHistQuotes = google.getHistory();

        stock.print();
    }

    /*
     * MULTIPLE STOCKS AT ONCE
     */
    private static void readMultipleStock(String[] symbols) throws IOException {

        // String[] symbols = new String[] {"INTC", "BABA", "TSLA", "AIR.PA", "YHOO"};
        Map<String, Stock> stocks = YahooFinance.get(symbols); // single request
        Stock intel = stocks.get("INTC");
        Stock airbus = stocks.get("AIR.PA");
    }

    /*
     * FX QUOTE
     */
    private static void readFXData(String[] symbols) throws IOException {

        FxQuote usdeur = YahooFinance.getFx(FxSymbols.USDEUR);
        FxQuote usdgbp = YahooFinance.getFx("USDGBP=X");
        System.out.println(usdeur);
        System.out.println(usdgbp);
    }

    /*
     * MULTIPLE STOCK - INCLUDE HISTORICAL QUOTES
     */
    private static void readHistorical(String[] symbols) throws IOException {

        // Warning!!!
        symbols = new String[] { "INTC", "BABA", "TSLA", "AIR.PA", "YHOO" };
        Map<String, Stock> stocks = YahooFinance.get(symbols, true); // single request
        Stock intel = stocks.get("INTC");
        Stock airbus = stocks.get("AIR.PA");
    }

    /*
     * STOCK - INCLUDE HISTORICAL QUOTES - Include interval -5 means from 5 years ago
     */
    private static void readSingleStockInterval(String symbol, int yearsInAdvance) throws IOException {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, yearsInAdvance); // -5 means from 5 years ago

        Stock stock = YahooFinance.get(symbol, from, to, Interval.WEEKLY);

        stock.print();

    }

    /*
     * ALTERNATIVES FOR HISTORICAL QUOTES
     */
    private static void otherReadingHistoricalData() throws IOException {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1); // from 1 year ago

        Stock google = YahooFinance.get("GOOG");
        List<HistoricalQuote> googleHistQuotes = google.getHistory(from, to, Interval.DAILY);
        // googleHistQuotes is the same as google.getHistory() at this point
        // provide some parameters to the getHistory method to send a new request to Yahoo Finance
    }

}
