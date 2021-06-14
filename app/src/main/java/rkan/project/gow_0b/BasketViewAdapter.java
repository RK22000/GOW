package rkan.project.gow_0b;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

public class BasketViewAdapter extends RecyclerView.Adapter<BasketItemCardViewHolder> {
    private final LayoutInflater        mInflater;
    private final BrochureBasketModel mBBModel;
    private final LiveData<Basket>      mLiveBasket;
    private final FragmentActivity      mParentFragment;
    public static final String DIALOG_TAG = "From Basket";
    public BasketViewAdapter(LayoutInflater inflater, FragmentActivity vMStoreOwner) {
        this.mInflater = inflater;
        mBBModel = new ViewModelProvider(vMStoreOwner).get(BrochureBasketModel.class);
        mLiveBasket = mBBModel.getBasketLiveData();
        mParentFragment = vMStoreOwner;
    }

    @NonNull
    @NotNull
    @Override
    public BasketItemCardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent
            , int viewType) {
        MaterialCardView basketItemView = BasketItemCardViewHolder.inflate(mInflater, parent);
        return new BasketItemCardViewHolder(basketItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BasketItemCardViewHolder holder, int position) {
        holder.initialize(mLiveBasket.getValue().get(position));
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuantitySelectionDialogFragment dialogFragment = new
                        QuantitySelectionDialogFragment(holder.getBasketItem(), mBBModel);
                dialogFragment.show(mParentFragment.getSupportFragmentManager(), DIALOG_TAG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLiveBasket.getValue().size();
    }
}

class BasketItemCardViewHolder extends RecyclerView.ViewHolder {

    MaterialCardView mHolderView;
    BasketItem mBasketItem;

    public static MaterialCardView inflate(LayoutInflater inflater, ViewGroup parent) {
        return (MaterialCardView) inflater.inflate(R.layout.cardview_basket_item, parent, false);
    }
    public BasketItemCardViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mHolderView = (MaterialCardView) itemView;
    }
    public void initialize(BasketItem item) {
        ((TextView) mHolderView.findViewById(R.id.quantity_view))
                .setText(Double.toString(item.getRoundedQuantity(2)) + item.getRateUnit());
        ((TextView) mHolderView.findViewById(R.id.basket_item_view))
                .setText(item.getItemName());
        ((TextView) mHolderView.findViewById(R.id.basket_category_view))
                .setText(item.getItemCategory());
        ((TextView) mHolderView.findViewById(R.id.item_cost_view))
                .setText("\u20B9" + Double.toString(item.getRoundedPrice(2)));
    }
    public MaterialCardView getView() {
        return mHolderView;
    }

    public BasketItem getBasketItem() {
        return mBasketItem;
    }
}

