package rkan.project.gow_0b;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private static final String ARG_PARAM1 = "Param for parent activity"
            , LOG_TAG = "BrochureFragment";

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
        mBrochureRecycler.setLayoutManager(new LinearLayoutManager(getContext()));


        BrochureBasketModel brochureBasketModel =
                new ViewModelProvider(mParentActivity).get(BrochureBasketModel.class);


        brochureBasketModel.getBrochureLiveData().observe(
                requireActivity(),
                brochureItems -> {
                    mBrochureViewAdapter.notifyDataSetChanged();
                    Log.d(LOG_TAG, "Brochure Change noted");
                }
        );
        brochureBasketModel.getBasketLiveData().observe(
                requireActivity(),
                basketItems -> {
                    mBrochureViewAdapter.notifyDataSetChanged();
                    Log.d(LOG_TAG, "Basket Change noted");
                }
        );

        CategoryViewAdapter categoryViewAdapter = new CategoryViewAdapter(requireActivity(),
                new FilterCallback() {
                    @Override
                    public void onCallBack(String category) {
                        mBrochureViewAdapter.setCategoryFilter(category);
                        mBrochureRecycler.setAdapter(mBrochureViewAdapter);
                        Log.d("BrochureFragment", "CallBack Received: " + category);
                        String brochureTitle = category.equals("All Categories")?
                                getString(R.string.brochure_title_maratish):
                                category;
                        ((TextView)view.findViewById(R.id.brochureTitleView)).setText(brochureTitle);
                    }
                });
        Button categoryButton = view.findViewById(R.id.category_button);
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBrochureRecycler.setAdapter(categoryViewAdapter);
            }
        });
        mParentActivity.addOnBackPressedCallback(new MainActivity.onBackPressedCallback() {
            @Override
            public boolean onBackPressed() {
                if (mBrochureRecycler.getAdapter().getClass().getName().equals(BrochureViewAdapter.class.getName())) {
                    Log.d("BrochureFragment", "Handling MainActivity Back pressed event");
                    categoryButton.callOnClick();
                    return true;
                }
                return false;
            }
        });
        EditText filterTextView = view.findViewById(R.id.filterText);

        filterTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("BrochureFragment", "Filtering: " + s);
                ((FilterAdapter)mBrochureRecycler.getAdapter()).setFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("BrochureFragment", "AfterTextChanged: " + s );
            }
        });
        filterTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("BrochureFragment", "EditorAction happened");
                //filterTextView.clearFocus();
                hideKeyboard();
                filterTextView.clearFocus();
                return true;
            }
        });
        mBrochureRecycler.setAdapter(categoryViewAdapter);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
    }

}

interface FilterCallback {
    public void onCallBack(String filter);
}