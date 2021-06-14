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

public class BrochureViewAdapter extends RecyclerView.Adapter<BrochureItemCardViewHolder>
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
    public BrochureItemCardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent
            , int viewType) {
        View basketItemView = mInflater.inflate(R.layout.grocery_item_view, parent, false);
        MaterialCardView brochureItemView = BrochureItemCardViewHolder.inflate(
                mInflater,
                parent
        );
        return new BrochureItemCardViewHolder(brochureItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BrochureItemCardViewHolder holder, int position) {
        holder.initialize(mFilteredBrochure.get(position));
        holder.markIfInBasket(mBBModel.getBasketLiveData().getValue().contains(holder.getGroceryItem()));
        holder.getView().setOnClickListener(view -> {
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


class BrochureItemCardViewHolder extends RecyclerView.ViewHolder {
    MaterialCardView mHolderView;
    GroceryItem mGroceryItem;
    public static MaterialCardView inflate(LayoutInflater inflater, ViewGroup parent) {
        return (MaterialCardView) inflater.inflate(
                R.layout.cardview_brochure_item,
                parent,
                false);
    }
    public BrochureItemCardViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mHolderView = (MaterialCardView) itemView;
    }
    public void initialize(GroceryItem item) {
        mGroceryItem = item;

        ((TextView) mHolderView.findViewById(R.id.brochure_item_view))
                .setText(mGroceryItem.getItemName());
        ((TextView) mHolderView.findViewById(R.id.brochure_category_view))
                .setText(mGroceryItem.getItemCategory());
        ((TextView) mHolderView.findViewById(R.id.rate_view))
                .setText(mGroceryItem.CURRENCY.concat(mGroceryItem.getStringItemRate()));
        String rateUnit;
        if (!(rateUnit = mGroceryItem.getRateUnit()).equals("")) {
            rateUnit = "/".concat(rateUnit);
        } else {
            rateUnit = null;
        }
        ((TextView) mHolderView.findViewById(R.id.rate_unit_view))
                .setText(rateUnit);
    }
    public MaterialCardView getView() {
        return mHolderView;
    }
    public GroceryItem getGroceryItem() {
        return mGroceryItem;
    }
    public void markIfInBasket(boolean inBasket) {
        mHolderView.setChecked(inBasket);
    }
}
