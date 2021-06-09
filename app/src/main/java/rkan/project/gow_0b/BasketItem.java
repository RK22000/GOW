package rkan.project.gow_0b;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class to represent an item in the basket. It extends Grocery item
 * to include quantity of item in basket and cost of item.
 */
public class BasketItem extends GroceryItem {
    private double itemQuantity, price;
    private int basketItemNumber;

    /**
     * Constructor to initialize the BasketItem using a GroceryItem
     * and quantity
     * @param item GroceryItem
     * @param quantity quantity of item
     */
    public BasketItem(GroceryItem item, double quantity) {
        super(item.getItemName(), item.getItemCategory(), item.getRateUnit(), item.getItemRate());
        itemQuantity = quantity;
        price = getItemRate() * itemQuantity;
//        basketItemNumber = itemNumber;
    }

    public int getBasketItemNumber() {
        return basketItemNumber;
    }

    public void setBasketItemNumber(int basketItemNumber) {
        this.basketItemNumber = basketItemNumber;
    }
    public double getItemQuantity() {
        return itemQuantity;
    }

    public void updateItemQuantity(double itemQuantity) {
        this.itemQuantity = itemQuantity;
        price = getItemRate() * itemQuantity;
    }

    public void incrementQuantity(double extraQuantity) {
        itemQuantity += extraQuantity;
        price += getItemRate();
    }

    public void incrementQuantity() {
        itemQuantity++;
        price += getItemRate();
    }

    public double getPrice() {
        return price;
    }

    public double getRoundedPrice(int digitsForRounding) {
        return new BigDecimal(price).setScale(digitsForRounding, RoundingMode.HALF_UP).doubleValue();
    }

    @NonNull
    @Override
    public @NotNull String toString() {
        StringBuilder itemString = new StringBuilder()
                .append(itemQuantity);
        if (getRateUnit() != null) {
            if (getRateUnit().equals("gaddi")) {
                itemString.append(" ");
            }
            itemString.append(getRateUnit());
        }
        itemString
                .append(" ")
                .append(getItemName())
                .append(" ")
                .append(getRoundedPrice(2))
                .append("\u20B9");
        return itemString.toString();

    }
}
