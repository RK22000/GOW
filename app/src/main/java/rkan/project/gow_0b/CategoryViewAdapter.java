package rkan.project.gow_0b;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewAdapter extends RecyclerView.Adapter<CategoryViewHolder>
        implements FilterAdapter{
    public static final String ALL_Categories = "All Items";
    private final LayoutInflater mInflater;
    private final BrochureBasketModel mBBModel;
    private final LiveData<List<String>> mLiveCategories;
    private final FragmentActivity mParentFragment;
    private final FilterCallback mFilterCallBack;
    private ArrayList<String> mFilteredCategories;
    private String filter;

    public CategoryViewAdapter (FragmentActivity parentFrag, FilterCallback callback) {
        mParentFragment = parentFrag;
        mBBModel = new ViewModelProvider(mParentFragment).get(BrochureBasketModel.class);
        mLiveCategories = mBBModel.getCategoryLiveData();
        mLiveCategories.observe(mParentFragment, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                setFilteredCategories();
            }
        });
        mInflater = mParentFragment.getLayoutInflater();
        mFilterCallBack = callback;
    }
    private void setFilteredCategories(){
        mFilteredCategories = new ArrayList<>();
        for (String s :
                mLiveCategories.getValue()) {
            if (filter == null || s.toLowerCase().contains(filter)) {
                mFilteredCategories.add(s);
            }
        }
        notifyDataSetChanged();
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
        holder.initialize(mFilteredCategories.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFilterCallBack.onCallBack(holder.mCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredCategories.size();
    }

    @Override
    public void setFilter(String filterString) {
        filter = filterString.toLowerCase();
        setFilteredCategories();
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