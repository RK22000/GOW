package rkan.project.gow_0b;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class BrochureBasketModel extends AndroidViewModel implements Serializable {
    private final MutableLiveData<Brochure> brochureLiveData =
            new MutableLiveData<>(new Brochure());
    private final MutableLiveData<Basket> basketLiveData =
            new MutableLiveData<>(new Basket());
    private final MutableLiveData<List<String>> categoryLiveData =
            new MutableLiveData<>(new ArrayList<>());

    private final File filesDir;
    private final StorageReference storageReference;
    private final SharedPreferences preferences;
    private final String STORAGE_FILE = "BrochureDemo.csv";
    private final String BROCHURE_FILE = "BrochureDemo.csv";
    private final String BROCHURE_UPDATE_KEY = "Brochure Update Metadata";

    public BrochureBasketModel(@NonNull @NotNull Application application) {
        super(application);
        filesDir = application.getFilesDir();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        preferences = application.getSharedPreferences("rkan.project.gow_0b.PREFERENCE_FILE_KEY",
                Context.MODE_PRIVATE);
        initializeBrochure3();
    }

    public boolean initializeBrochure3() {
        final File localFile = new File(filesDir, BROCHURE_FILE);
        setLiveBrochureFromFile(localFile);


        final long updateMills = preferences.getLong(BROCHURE_UPDATE_KEY, 0);
        StorageReference brochureReference = storageReference.child(STORAGE_FILE);
        Log.d("BrochureBasketModel", "Getting  meta data");
        brochureReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                if (storageMetadata.getUpdatedTimeMillis() != updateMills) {
                    Log.d("BrochureBasketModel", "Downloading brochure");
                    brochureReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            setLiveBrochureFromFile(localFile);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putLong(BROCHURE_UPDATE_KEY,storageMetadata.getUpdatedTimeMillis());
                            editor.apply();
                        }
                    });
                } else {
                    Log.d("BrochureBasketModel", "Not downloading");
                }

            }
        });
        return true;
    }
    private boolean setLiveBrochureFromFile(File file) {
        Brochure brochure = new Brochure();
        if (BBFileIO.readBrochureFile(brochure, file)) {
            brochureLiveData.setValue(brochure);
            categorizeBrochure();
            return true;
        }
        return false;
    }


    // TODO: make this a method of the basket so category list is obtained from the basket
    private void categorizeBrochure() {
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
    public void setBasketLiveData(Basket basket) {
        basketLiveData.setValue(basket);
    }

    public LiveData<List<String>> getCategoryLiveData() {
        return categoryLiveData;
    }
}
