package rkan.project.gow_0b;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class BrochureBasketModel extends ViewModel implements Serializable {
    private final MutableLiveData<Brochure> brochureLiveData =
            new MutableLiveData<>(new Brochure());
    private final MutableLiveData<Basket> basketLiveData =
            new MutableLiveData<>(new Basket());
    private final MutableLiveData<List<String>> categoryLiveData =
            new MutableLiveData<>(new ArrayList<>());

    public BrochureBasketModel() {
        // TODO: setToDemoBrochure could produce null pointer error.
        //  Understand this case
        initialize();
    }

    public void initialize() {
        brochureLiveData.setValue(brochureLiveData.getValue().setToDemoBrochure());
        Log.d("BrochBasketModel", "Brochure initialized to demo brochure of size = " + brochureLiveData.getValue().size());
        TreeSet<String> categorySet = new TreeSet<>();
        categorySet.add("All Categories");
        for (GroceryItem g :
                brochureLiveData.getValue()) {
            categorySet.add(g.getItemCategory());
        }
        categoryLiveData.setValue(new ArrayList<>(categorySet));
    }

    public boolean addToBasket(BasketItem basketItem) {
        Basket newBasket = basketLiveData.getValue();
        boolean newItemAddedToBasket = newBasket.add(basketItem);
        basketLiveData.setValue(newBasket);
        Brochure newBrochure = brochureLiveData.getValue();
        newBrochure.get(newBrochure.indexOf(basketItem)).inBasket = true;
        brochureLiveData.setValue(newBrochure);
        return newItemAddedToBasket;
    }

    public void swapBasketItemsAt(int to, int from) {
        Basket newBasket = basketLiveData.getValue();
        Collections.swap(newBasket, to, from);
        basketLiveData.setValue(newBasket);
    }
    //  TODO: Do I need to update LiveData here
    public BasketItem removeFromBasket(int index) {
        Basket newBasket = basketLiveData.getValue();
        BasketItem removedItem = newBasket.remove(index);
        basketLiveData.setValue(newBasket);
        Brochure newBrochure = brochureLiveData.getValue();
        newBrochure.get(newBrochure.indexOf(removedItem)).inBasket = false;
        brochureLiveData.setValue(newBrochure);
        return removedItem;
    }

    public LiveData<Brochure> getBrochureLiveData() {
        return brochureLiveData;
    }

    public LiveData<Basket> getBasketLiveData() {
        return basketLiveData;
    }

    public LiveData<List<String>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
