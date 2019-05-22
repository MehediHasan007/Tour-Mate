package com.example.tourmate.Fragment;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourmate.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheet extends BottomSheetDialogFragment {

    TextView bottomSheetTripName,bottomSheetTripDescription,bottomSheetTripBudget,bottomSheetTripStartDate,bottomSheetTripEndDate;

    public BottomSheet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        bottomSheetTripName = view.findViewById(R.id.bottomSheetTripName);
        bottomSheetTripDescription = view.findViewById(R.id.bottomSheetTripDescription);
        bottomSheetTripBudget = view.findViewById(R.id.bottomSheetTripBudget);
        bottomSheetTripStartDate = view.findViewById(R.id.bottomSheetTripStartDate);
        bottomSheetTripEndDate = view.findViewById(R.id.bottomSheetTripEndDate);

        if(getArguments()!=null){
            String TripName = getArguments().getString("Trip Name");
            String TripDescription = getArguments().getString("Trip Description");
            String TripBudget = getArguments().getString("Trip Budget");
            String TripStartDate = getArguments().getString("Trip Start Date");
            String TripEndDate = getArguments().getString("Trip End Date");

            bottomSheetTripName.setText(TripName);
            bottomSheetTripDescription.setText(TripDescription);
            bottomSheetTripBudget.setText(TripBudget);
            bottomSheetTripStartDate.setText(TripStartDate);
            bottomSheetTripEndDate.setText(TripEndDate);
        }

        return view;
    }

}
