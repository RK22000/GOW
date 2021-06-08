package rkan.project.gow_0b;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrochureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrochureFragment extends Fragment {
    private static final String ARG_PARAM1 = "Param for parent activity";

    private BrochureViewAdapter mBrochureViewAdapter;
    private MainActivity mParentActivity;

    public static BrochureFragment newInstance() {
        return new BrochureFragment();
    }

    public static BrochureFragment newInstance(MainActivity parentActivity) {
        BrochureFragment fragment = new BrochureFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, parentActivity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentActivity = (MainActivity)getArguments().getSerializable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brochure, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mBrochureRecycler = view.findViewById(R.id.brochureRecyclerView);
        mBrochureViewAdapter = new BrochureViewAdapter(getLayoutInflater(), requireActivity());
        mBrochureRecycler.setAdapter(mBrochureViewAdapter);
        mBrochureRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        BrochureBasketModel brochureBasketModel =
                new ViewModelProvider(mParentActivity).get(BrochureBasketModel.class);


        brochureBasketModel.getBrochureLiveData().observe(requireActivity(), brochureOrBasket -> {
            mBrochureViewAdapter.notifyDataSetChanged();
            Log.d("From BrochFragment", "Brochure Change noted");
        });

        CategoryViewAdapter categoryViewAdapter = new CategoryViewAdapter(requireActivity(),
                new FilterCallback() {
                    @Override
                    public void onCallBack(String filter) {
                        mBrochureViewAdapter.setFilter(filter);
                        mBrochureRecycler.setAdapter(mBrochureViewAdapter);
                        Log.d("BrochureFragment", "CallBack Received: " + filter);
                    }
                });
        Button categoryButton = view.findViewById(R.id.category_button);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBrochureRecycler.setAdapter(categoryViewAdapter);
            }
        });


    }
}

interface FilterCallback {
    public void onCallBack(String filter);
}