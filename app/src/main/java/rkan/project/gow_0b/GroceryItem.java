package rkan.project.gow_0b;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class to represent Grocery item. Instances of this class will be
 * stored in the grocery brochure and the grocery brochure will be
 * a reference for adding items to basket and calculating rate.
 *
 * I'm  thinking that if I put a string category in this then if I
 * make the brochure a tree set then all the same  category items will be
 * next to each other.
 *
 * Implements Serializable so that it can
 */
public class GroceryItem implements Serializable {
    public static final String CURRENCY = "\u20B9";
    private final String itemName,
            itemCategory,
            rateUnit;
    private double itemRate;

    /**
     * Constructor to initialize a GroceryItem
     * @param itemName item name
     * @param itemCategory item category
     * @param rateUnit unit of measuring item quantity
     * @param itemRate price of a unit quantity of item
     */
    public GroceryItem(String itemName, String itemCategory, String rateUnit, double itemRate) {
        this.itemName = itemName;
        this.itemCategory = itemCategory;
        this.rateUnit = rateUnit;
        this.itemRate = itemRate;
    }

    public boolean itemIsQuantized() {
        if (rateUnit.toLowerCase().equals("kg")) {
            return false;
        }
        return true;
    }

    public String getItemName() {
        return itemName;
    }

    public String getRateUnit() {
        return rateUnit;
    }

    public double getItemRate() {
        return itemRate;
    }

    public double getRoundedItemRate(int digitsForRounding) {
        return new BigDecimal(itemRate).setScale(digitsForRounding, RoundingMode.HALF_UP).doubleValue();
    }

    public String getStringItemRate() {
        return Double.toString(getRoundedItemRate(2));
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public String getItemFullName(){
        return itemCategory+":"+itemName;
    }
    public void updateItemRate(double itemRate) {
        this.itemRate = itemRate;
    }

    @Override
    public final boolean equals(Object obj) {
        try {
            GroceryItem otherObj = (GroceryItem) obj;
            return getItemFullName().equals(otherObj.getItemFullName());
        } catch (ClassCastException e){
            return false;
        }
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        StringBuilder itemString = new StringBuilder(itemCategory)
                .append(":")
                .append(itemName)
                .append(" ")
                .append(getRoundedItemRate(2))
                .append("\u20B9");
        if (rateUnit != null && !rateUnit.equals("")) {  // notNull and not""
            itemString.append("/")
                    .append(rateUnit);
        }
        return itemString.toString();
    }
}
