package rkan.project.gow_0b;

import androidx.fragment.app.DialogFragment;

public class ContinuumSelectionDialogFragment extends DialogFragment {
    private GroceryItem mGroceryItem;
    private BrochureBasketModel mBBModel;
    private double mQuantity;

    public ContinuumSelectionDialogFragment(GroceryItem gi, BrochureBasketModel bbm) {
        mGroceryItem = gi;
        mBBModel = bbm;
        mQuantity = 1;
    }

}
