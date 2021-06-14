package rkan.project.gow_0b;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class BrochureViewAdapter extends RecyclerView.Adapter<BrochureItemViewHolder>
implements FilterAdapter{

    public static final String DIALOG_TAG = "From Brochure";
    private final LayoutInflater        mInflater;
    private final BrochureBasketModel mBBModel;
    private final LiveData<Brochure>    mLiveBrochure;
    //private final QuantitySelectionDialogFragment mDialog;
    private final FragmentActivity      mParentFragment;
    private String categoryFilter, filter;
    private Brochure mFilteredBrochure;

    public BrochureViewAdapter(LayoutInflater inflater, FragmentActivity vMStoreOwner) {
        this.mInflater = inflater;
        mBBModel = new ViewModelProvider(vMStoreOwner).get(BrochureBasketModel.class);
        mLiveBrochure = mBBModel.getBrochureLiveData();
        mParentFragment = vMStoreOwner;
        categoryFilter = null;
        mLiveBrochure.observe(vMStoreOwner, new Observer<Brochure>() {
            @Override
            public void onChanged(Brochure brochure) {
                setmFilteredBrochure();
            }
        });
    }

    public void setCategoryFilter(String newFilter){
        categoryFilter = newFilter;
        if (categoryFilter.equals("All Categories")) {
            categoryFilter = null;
        }
        setmFilteredBrochure();
    }

    @Override
    public void setFilter(String filterString) {
        filter = filterString.toLowerCase();
        if (filter.trim().equals("")) {
            filter = null;
        }
        setmFilteredBrochure();
    }
    private void setmFilteredBrochure() {
        mFilteredBrochure = new Brochure();
        for (GroceryItem g :
                mLiveBrochure.getValue()) {
            if ((categoryFilter == null || g.getItemCategory().equals(categoryFilter))
                    && (filter == null || g.getItemFullName().toLowerCase().contains(filter))
            ) {
                mFilteredBrochure.add(g);
            }
        }
        notifyDataSetChanged();
    }


    @NonNull
    @NotNull
    @Override
    public BrochureItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent
            , int viewType) {
        View basketItemView = mInflater.inflate(R.layout.grocery_item_view, parent, false);
        return new BrochureItemViewHolder(basketItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BrochureItemViewHolder holder, int position) {
        holder.initialize(mFilteredBrochure.get(position));
        holder.markIfInBasket(mBBModel.getBasketLiveData().getValue().contains(holder.getGroceryItem()));
        holder.setOnClickListener(view -> {
            QuantitySelectionDialogFragment dialogFragment = new
                    QuantitySelectionDialogFragment(holder.getGroceryItem(), mBBModel);
            dialogFragment.show(mParentFragment.getSupportFragmentManager(), DIALOG_TAG);
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredBrochure.size();
    }
}

class BrochureItemViewHolder extends RecyclerView.ViewHolder {
    GroceryItem mBrochureItem;
    MaterialCardView mHolderView;
    public BrochureItemViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mHolderView = (MaterialCardView)itemView;
    }
    public void initialize(GroceryItem groceryItem) {
        mBrochureItem = groceryItem;
        ((TextView)mHolderView.findViewById(R.id.groceryText)).setText(mBrochureItem.toString());
        //markIfInBasket();
    }

    private boolean markIfInBasket() {
        TextView display = mHolderView.findViewById(R.id.groceryText);
        if (mBrochureItem.inBasket) {
            display.setTextColor(Color.GREEN);
            return true;
        } else {
            display.setTextColor(Color.GRAY);
            return false;
        }
    }

    public boolean markIfInBasket(boolean inBasket) {
        TextView display = mHolderView.findViewById(R.id.groceryText);
        mHolderView.setChecked(inBasket);
        if (inBasket) {
            display.setTextColor(Color.GREEN);
            return true;
        } else {
            display.setTextColor(Color.GRAY);
            return false;
        }
    }

    public GroceryItem getGroceryItem() {
        return mBrochureItem;
    }
    public void setOnClickListener(View.OnClickListener listener) {
        mHolderView.setOnClickListener(listener);
    }
}
