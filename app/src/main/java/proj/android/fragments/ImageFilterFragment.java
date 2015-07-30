package proj.android.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.HashMap;
import java.util.Map;
import proj.android.gl_helper.GlSurfaceView;
import proj.android.helper.Utils;
import proj.android.imagefilter.R;

/**
 * Created by deepak on 24/5/15.
 */
public class ImageFilterFragment extends Fragment implements FilterCallback{

    private static final Map<String,ImageFilterFragment> imageFilterFragmentInstances = new HashMap<>();
    @SuppressLint("ValidFragment")
    private ImageFilterFragment(){}
    public static ImageFilterFragment getInstance(String forManager) {
        if (imageFilterFragmentInstances.get(forManager) == null)
            imageFilterFragmentInstances.put(forManager, new ImageFilterFragment());
        return imageFilterFragmentInstances.get(forManager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_image_filter, container, false);
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
        addGlView();
    }

    private void addGlView(){
        glSurfaceView=GlSurfaceView.getInstance(view.getContext());
        //remove glSurfaceView from previous view if added before
        ViewGroup parent = (ViewGroup)glSurfaceView.getParent();
        if(parent != null)
            parent.removeView(glSurfaceView);

        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        glView.addView(glSurfaceView, params);
        Utils.setSurfaceViewObj(glSurfaceView);

        ParseViews parseViews = new ParseViews(view, this);
        parseViews.initialize();
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

    @Override
    public void redrawTexture(int vertexShaderId, int fragmentShaderId) {
        glSurfaceView.drawNewTexture(vertexShaderId, fragmentShaderId);
    }
}
