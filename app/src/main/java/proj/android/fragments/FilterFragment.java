package proj.android.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj.android.gl_helper.GlSurfaceView;
import proj.android.imagefilter.R;

/**
 * Created by deepak on 24/5/15.
 */
public class FilterFragment extends Fragment{

    private static final Map<String,FilterFragment> filterFragmentInstances = new HashMap<>();
    @SuppressLint("ValidFragment")
    private FilterFragment(){}
    public static FilterFragment getInstance(String forManager) {
        if (filterFragmentInstances.get(forManager) == null)
            filterFragmentInstances.put(forManager, new FilterFragment());
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
        glView=(RelativeLayout)view.findViewById(R.id.gl_view);
        addGlView();
    }

    private void addGlView(){
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        glSurfaceView=GlSurfaceView.getInstance(view.getContext());
        glView.addView(glSurfaceView, params);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glView.removeAllViews();
    }

    private View view;
    private RelativeLayout glView;
    private GlSurfaceView glSurfaceView;
}
