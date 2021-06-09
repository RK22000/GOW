package rkan.project.gow_0b;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Brochure extends ArrayList<GroceryItem> {

    public Brochure setToDemoBrochure() {
        String[] vegetables = {"Cucumbers", "Tomatoes", "Carrots", "Beetroot"},
                leaves = {"Mint", "Methi", "Lettuces", "Coriander"},
                toiletries = {"Soap", "Toothpaste", "Toothbrush"},
                categorise = {"Vegetables", "Leaves", "Toiletries"},
                rateUnits = {"kg", "gaddi", null};
        String[][] cat = {vegetables, leaves, toiletries};
        for (int i = 0; i < categorise.length; i++) {
            for (int j = 0; j < cat[i].length; j++) {
                add(new GroceryItem(cat[i][j],categorise[i], rateUnits[i], new BigDecimal(Math.random()).setScale(2, RoundingMode.HALF_UP).doubleValue()));
            }
        }
        return this;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        StringBuilder brochureString = new StringBuilder();
        for (GroceryItem g :
                this) {
            brochureString.append(g).append("\n");
        }
        return brochureString.toString();
    }


    /*
    public static void main(String[] args) {
        Brochure testBrochure = new Brochure();
        testBrochure.setToDemoBrochure();
        System.out.println(testBrochure);
    }
     */
}
