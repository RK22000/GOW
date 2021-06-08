package rkan.project.gow_0b;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder>{
    private final LayoutInflater mInflater;
    private final BrochureBasketModel mBBModel;
    //private final QuantitySelectionDialogFragment mDialog;
    private final FragmentActivity mParentFragment;
    private final FilterCallback mFilterCallBack;
    public CategoryViewAdapter (FragmentActivity parentFrag, FilterCallback callback) {
        mParentFragment = parentFrag;
        mBBModel = new ViewModelProvider(mParentFragment).get(BrochureBasketModel.class);
        mInflater = mParentFragment.getLayoutInflater();
        mFilterCallBack = callback;
    }
    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View categoryItemView = mInflater.inflate(R.layout.grocery_item_view, parent, false);
        return new CategoryViewHolder(categoryItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {
        holder.initialize(mBBModel.getCategoryLiveData().getValue().get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterCallBack.onCallBack(holder.mCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBBModel.getCategoryLiveData().getValue().size();
    }
}

class CategoryViewHolder extends RecyclerView.ViewHolder {
    String mCategory;
    View mHolderView;
    public CategoryViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        mHolderView = itemView;
    }
    public void initialize(String category) {
        ((TextView)mHolderView.findViewById(R.id.groceryText)).setText(category);
        mCategory = category;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mHolderView.setOnClickListener(listener);
    }
}