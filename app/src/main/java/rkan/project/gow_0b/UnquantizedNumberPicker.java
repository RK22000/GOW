package rkan.project.gow_0b;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class UnquantizedNumberPicker {
    private LayoutInflater mInflater;
    private boolean inflated = false;
    private View rootView;
    private NumberPicker wholePicker, decimalPicker;
    private final int decimalRange = 100;
    private final int wholeRange = 100;
    UnquantizedNumberPicker(LayoutInflater inflater, String unit, boolean unitQuantized) {
        mInflater = inflater;
        rootView = inflater.inflate(R.layout.dialog_unquantized, null);
        ((TextView)rootView.findViewById(R.id.unit_view)).setText(unit);
        wholePicker = rootView.findViewById(R.id.whole_picker);
        decimalPicker = rootView.findViewById(R.id.decimal_picker);
        wholePicker.setMaxValue(wholeRange-1);
        wholePicker.setMinValue(0);
        decimalPicker.setMaxValue(decimalRange-1);
        decimalPicker.setMinValue(0);
        String[] displayRange = new  String[decimalRange];
        for (int i = 0; i < decimalRange; i++) {
            displayRange[i] = "."
                    + (i<10?"0":"")
                    + i;
        }
        decimalPicker.setDisplayedValues(displayRange);
        if (unitQuantized) {
            decimalPicker.setVisibility(View.GONE);
        }
    }

    public double getPickerValue() {
        rootView.clearFocus();
        return wholePicker.getValue() + decimalPicker.getValue()*0.01;
    }

    public View getRootView () {
        return rootView;
    }


}
