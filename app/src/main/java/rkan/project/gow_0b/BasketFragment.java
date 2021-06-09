package rkan.project.gow_0b;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static androidx.core.content.FileProvider.getUriForFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BasketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasketFragment extends Fragment {

    private static final String ARG_PARAM1 = "Param for parent activity";

    private RecyclerView mBasketRecycler;
    private BasketViewAdapter mBasketViewAdapter;
    private MainActivity mParentActivity;

    public BasketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BasketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasketFragment newInstance(MainActivity parentActivity) {
        BasketFragment fragment = new BasketFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, parentActivity);
        fragment.setArguments(args);
        return fragment;
    }
    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO eliminate the cast
        mParentActivity = (MainActivity)getArguments().getSerializable(ARG_PARAM1);
        BrochureBasketModel bbModel = new ViewModelProvider(mParentActivity).get(BrochureBasketModel.class);
        BasketExporter basketFileReader = new BasketExporter(new Basket(),
                requireActivity().getFilesDir());
        if (basketFileReader.readBasketObject()) {
            bbModel.setBasketLiveData(basketFileReader.getBasket());
            Log.d("BasketFragment", "Basket Read from File\n" +bbModel.getBasketLiveData().getValue());
        }
    }

    @Override
    public void onPause() {
        Basket basketItems = new ViewModelProvider(mParentActivity)
                .get(BrochureBasketModel.class)
                .getBasketLiveData()
                .getValue();
        BasketExporter basketSaver = new BasketExporter(
                basketItems,
                requireActivity().getFilesDir());
        basketSaver.saveBasketObject();
        Log.d("BasketFragment", "Basket saved to file\n" + basketItems );
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBasketRecycler = view.findViewById(R.id.basketRecyclerView);
        mBasketViewAdapter = new BasketViewAdapter(getLayoutInflater(), requireActivity());
        mBasketRecycler.setAdapter(mBasketViewAdapter);
        mBasketRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        BrochureBasketModel brochureBasketModel =
                new ViewModelProvider(mParentActivity).get(BrochureBasketModel.class);
        brochureBasketModel.getBasketLiveData().observe(getViewLifecycleOwner(), new Observer<Basket>() {
            @Override
            public void onChanged(Basket basketItems) {
                mBasketViewAdapter.notifyDataSetChanged();
                displayPrice(basketItems.getRoundedTotalPrice(2));
            }
        });

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN
                , ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView
                    , @NonNull @NotNull RecyclerView.ViewHolder viewHolder
                    , @NonNull @NotNull RecyclerView.ViewHolder target) {
                int to = viewHolder.getAdapterPosition();
                int from = target.getAdapterPosition();
                brochureBasketModel.swapBasketItemsAt(to, from);
                return true;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder
                    , int direction) {
                brochureBasketModel.removeFromBasket(viewHolder.getAdapterPosition());
            }
        });

        touchHelper.attachToRecyclerView(mBasketRecycler);
        displayPrice(0);

        Button exportButton = view.findViewById(R.id.basketExportButton);
        exportButton.setOnClickListener(v -> {
            BasketExporter exporter = new BasketExporter(
                    brochureBasketModel.getBasketLiveData().getValue(),
                    requireContext().getCacheDir());
            Log.d("BasketFragment", "Exporting Basket\n" + brochureBasketModel.getBasketLiveData().getValue());

            exporter.export();
            Toast.makeText(requireContext(), "Exported basket", Toast.LENGTH_SHORT).show();
            shareFileToOutside(exporter.getExportedFile());
            /*
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            //sharingIntent.setType(URLConnection.guessContentTypeFromName(exporter.getExportedFile().getName()));
            // TODO: exporter.getExportedFile could cause a null pointer error
            //sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://rkan.project.gow_0a/cache/Basket_export_file.txt"));
            //sharingIntent.putExtra(Intent.EXTRA_STREAM,
                    //Uri.parse("content://" + exporter.getExportedFile().getAbsolutePath()));
            //sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, brochureBasketModel.getBasketLiveData().getValue().toString());
            sharingIntent.setType("text/plain");
            Log.d("BasketFragment", exporter.getExportedFile().getAbsolutePath());
            startActivity(Intent.createChooser(sharingIntent, "share file/text with"));
             */
        });

        exportButton = view.findViewById(R.id.exportTextButton);
        exportButton.setOnClickListener(v -> {
            shareTextToOutside(brochureBasketModel.getBasketLiveData().getValue().toString());
        });
    }

    private void shareFileToOutside(File file) {
        Uri contentUri = getUriForFile(requireContext(), "rkan.project.gow_0b.fileprovider", file);
        Log.d("BasketFragment", contentUri.toString());
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        sharingIntent.setType("text/plain");
        startActivity(Intent.createChooser(sharingIntent, "share file/text with"));
    }

    private void shareTextToOutside(String text) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        sharingIntent.setType("text/plain");
        startActivity(Intent.createChooser(sharingIntent, "share file/text with"));
    }

    private void displayPrice(double price) {
        ((TextView)getView().findViewById(R.id.priceView)).setText("Full Price no bargaining = " + price + "\u20B9");
    }
}
