package rkan.project.gow_0b;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class QuantitySelectionDialogFragment extends DialogFragment {
    private GroceryItem mGroceryItem;
    private BrochureBasketModel mBBModel;
    private double mQuantity;

    public QuantitySelectionDialogFragment(){
        //mBBModel = new ViewModelProvider(requireActivity()).get(BrochureBasketModel.class);

    }
    public QuantitySelectionDialogFragment(GroceryItem gi, BrochureBasketModel bbm) {
        mGroceryItem = gi;
        mBBModel = bbm;
        mQuantity = 1;
    }
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mGroceryItem = (GroceryItem)savedInstanceState.getSerializable("GroceryItemPARAM");
            mBBModel = (BrochureBasketModel)savedInstanceState.getSerializable("BBModelPARAM");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        UnquantizedNumberPicker quantityPicker = new UnquantizedNumberPicker(getLayoutInflater(),
                mGroceryItem.getRateUnit(),
                mGroceryItem.itemIsQuantized());
        if (getTag().equals(BasketViewAdapter.DIALOG_TAG)) {
            try {
                BasketItem basketItem = (BasketItem) mGroceryItem;
                quantityPicker.setPickerValue(basketItem.getItemQuantity());
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        builder.setView(quantityPicker.getRootView())
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (quantityPicker.getPickerValue() == 0){
                            mBBModel.removeFromBasket(mBBModel
                                    .getBasketLiveData()
                                    .getValue()
                                    .indexOf(mGroceryItem)
                            );
                        } else {
                            mBBModel.addToBasket(new BasketItem(mGroceryItem, quantityPicker.getPickerValue()));
                        }
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        if (getTag().equals(BasketViewAdapter.DIALOG_TAG)) {
            builder.setTitle("Change " + mGroceryItem.getItemName() + " quantity");
        } else if (getTag().equals(BrochureViewAdapter.DIALOG_TAG)){
            builder.setTitle("Add " + mGroceryItem.getItemName() + " in basket");
        }
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        outState.putSerializable("GroceryItemPARAM", mGroceryItem);
        outState.putSerializable("BBModelPARAM", mBBModel);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("QuantityDialog", "Dialog Dismissed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("QuantityDialog", "Dialog  destroyed");
    }
}
