package proj.android.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj.android.imagefilter.R;

/**
 * Created by deepak on 24/5/15.
 */
public class FilterFragment extends Fragment{

    private View view;
    private static final Map<String,FilterFragment> filterFragmentInstances = new HashMap<>();
    @SuppressLint("ValidFragment")
    private FilterFragment(){}
    public static FilterFragment getInstance(String forManager){
        if(filterFragmentInstances.get(forManager)==null)
            filterFragmentInstances.put(forManager,new FilterFragment());
        return filterFragmentInstances.get(forManager);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_filter, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
