package rkan.project.gow_0b;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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

    public BrochureBasketModel(@NonNull @NotNull Application application) {
        super(application);
        filesDir = application.getFilesDir();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        initializeFromFirebaseBucket();
    }

    public boolean initializeBrochure() {
        File brochureFile = new File(filesDir, "BrochureObjectFile");
        BBFileIO<Brochure> brochureFileIO = new BBFileIO<>(new Brochure(), brochureFile);
        Brochure localBrochure = brochureFileIO.getSubject();
        StorageReference brochureReference = storageReference.child("BrochureDemo.csv");
        brochureReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                if (storageMetadata.getUpdatedTimeMillis() != localBrochure.getLastUpdatedOnFirebase()) {
                    Log.d("BrochureBasketModel", "Brochure is out of sync");
                    // sync localBrochure with firebase Brochure
                    syncBrochureWithFirebaseStorage(localBrochure, brochureReference);
                    // set update time in localBrochure
                    localBrochure.setLastUpdatedOnFirebase(storageMetadata.getUpdatedTimeMillis());
                } else {
                    // nothing
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication().getApplicationContext(), "Failed to sync with online brochure", Toast.LENGTH_SHORT).show();
            }
        });
        brochureLiveData.setValue(localBrochure);
        return false;
    }

    public boolean syncBrochureWithFirebaseStorage(Brochure brochure, StorageReference storageBrochure) {
        File            tmpF = null;
        FileReader      fr = null;
        BufferedReader  br = null;
        try {
            tmpF = File.createTempFile("TempBrochureSyncFile", ".csv", getApplication().getCacheDir());
            fr = new FileReader(tmpF);
            br = new BufferedReader(fr);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        BufferedReader finalBr = br;
        storageBrochure.getFile(tmpF).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                String line;
                String[] item; // = new String[10]; // I only need 4
                try {
                    finalBr.readLine();
                    while ((line = finalBr.readLine()) != null) {
                        Log.d("BrochureBasketModel", line);
                        item = line.split(",");
                        brochure.add(new GroceryItem(item[1], item[0], item[3], Double.parseDouble(item[2])));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    public void initialize() {
        brochureLiveData.setValue(brochureLiveData.getValue().setToDemoBrochure());
        Log.d("BrochBasketModel", "Brochure initialized to demo brochure of size = " + brochureLiveData.getValue().size());
        categorizeBrochure();
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

    public void initializeFromFirebaseBucket(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference brochureReference = storageReference.child("BrochureDemo.csv");
        try {
            File localFile = File.createTempFile("Local Brochure", "csv");
            FileReader fileReader = new FileReader(localFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            brochureReference.getFile(localFile).addOnSuccessListener(
                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                String line;
                                String[] item; // = new String[10]; // I only need 4
                                Brochure brochure = new Brochure();
                                bufferedReader.readLine();
                                while ((line=bufferedReader.readLine()) != null)  {
                                    Log.d("BrochureBasketModel", line);
                                    item = line.split(",");
                                    brochure.add(new GroceryItem(item[1], item[0], item[3], Double.parseDouble(item[2])));
                                }
                                brochureLiveData.setValue(brochure);
                                categorizeBrochure();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
