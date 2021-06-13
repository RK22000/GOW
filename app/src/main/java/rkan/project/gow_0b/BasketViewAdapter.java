package rkan.project.gow_0b;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class BasketViewAdapter extends RecyclerView.Adapter<BasketItemViewHolder> {
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
    public BasketItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent
            , int viewType) {
        View basketItemView = mInflater.inflate(R.layout.grocery_item_view, parent, false);
        return new BasketItemViewHolder(basketItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BasketItemViewHolder holder, int position) {
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


class BasketItemViewHolder extends RecyclerView.ViewHolder {
    BasketItem mBasketItem;
    View mHolderView;
    public BasketItemViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mHolderView = itemView;
    }
    public void initialize(BasketItem basketItem) {
        mBasketItem = basketItem;
        ((TextView)mHolderView.findViewById(R.id.groceryText)).setText(mBasketItem.toString());
    }
    public BasketItem getBasketItem() {
        return mBasketItem;
    }
    public View getView() {
        return mHolderView;
    }
}
