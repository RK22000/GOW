package rkan.project.gow_0b;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Shopping basket
 */
public class Basket extends ArrayList<BasketItem> implements Serializable {
    private double totalPrice;

    public Basket(){
        super();
        totalPrice = 0;
    }

    public Basket setDemoBasket() {
        Brochure demoBroch = new Brochure().setToDemoBrochure();
        int i = 0;
        while (i < demoBroch.size()) {
            add(new BasketItem(demoBroch.get(i), 1));
        }
        return this;
    }

    @Override
    public boolean add(BasketItem basketItem) {
        boolean superClassAdd = !contains(basketItem);
        if (superClassAdd) {
            super.add(basketItem);
        } else {
            //get(indexOf(basketItem)).incrementQuantity(basketItem.getItemQuantity());
            set(indexOf(basketItem), basketItem);
        }
        calculateTotalPrice();
        return superClassAdd;
    }

    @Override
    public BasketItem remove(int index) {
        BasketItem removedItem = super.remove(index);
        calculateTotalPrice();
        return removedItem;
    }

    private void calculateTotalPrice() {
        totalPrice = 0;
        for (BasketItem b :
                this) {
            totalPrice += b.getPrice();
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getRoundedTotalPrice(int digitsForRounding) {
        return new BigDecimal(totalPrice).setScale(digitsForRounding, RoundingMode.HALF_UP).doubleValue();
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (BasketItem b :
                this) {
            builder.append(b)
                    .append("\n");
        }
        builder.append("The total price is \u20B9")
                .append(getRoundedTotalPrice(2));
        return builder.toString();
    }
}
