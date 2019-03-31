package business.bitmex;

/**
 * Bitmex position representation.
 */
public class BitmexPosition {

    private String symbol;
    private int quantity;

    /**
     * Creates a Bitmex position.
     * @param symbol symbol of the position, such as: BTCUSD.
     * @param quantity quantity of the position.
     */
    public BitmexPosition(String symbol, int quantity) {
        setSymbol(symbol);
        setQuantity(quantity);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        if (symbol == null || symbol.equals(""))
            throw new RuntimeException("symbol can not be null or empty.");
        this.symbol = symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
