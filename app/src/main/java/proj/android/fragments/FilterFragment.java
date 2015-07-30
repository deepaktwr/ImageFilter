package proj.android.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj.android.gl_helper.GlDecorator;
import proj.android.gl_helper.GlSurfaceView;
import proj.android.helper.Utils;
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

    private int width;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        glView=(RelativeLayout)view.findViewById(R.id.gl_view);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) glView.getLayoutParams();
        params.width = GlDecorator.getScreenWidth()-20;
        params.height = GlDecorator.getScreenWidth()-20;
        Utils.logMessage("width"+(GlDecorator.getScreenWidth()-20));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        glView.setLayoutParams(params);
        glView.requestLayout();
        addGlView();
    }

    private void addGlView(){
        glSurfaceView=GlSurfaceView.getInstance(view.getContext());
        //remove glSurfaceView from previous view if added before
        ViewGroup parent = (ViewGroup)glSurfaceView.getParent();
        if(parent != null)
            parent.removeView(glSurfaceView);

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        glView.addView(glSurfaceView, params);
        Utils.setSurfaceViewObj(glSurfaceView);
    }

    @Override
    public void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    private View view;
    private RelativeLayout glView;
    private GlSurfaceView glSurfaceView;
}
