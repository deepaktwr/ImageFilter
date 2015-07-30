package proj.android.imagefilter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import proj.android.exceptions.BlockExecution;
import proj.android.exceptions.FragmentException;
import proj.android.fragments.FilterFragment;
import proj.android.fragments.ImageFilterFragment;
import proj.android.gl_helper.GlDecorator;
import proj.android.helper.FragmentHelper;
import proj.android.helper.Fragments;
import proj.android.helper.Utils;


public class FilterMain extends Activity {

    private FragmentHelper fragmentHelper;
    private DisplayMetrics displayMetrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        GlDecorator.setScreenWidth(displayMetrics.widthPixels);
        GlDecorator.setScreenHeight(displayMetrics.heightPixels);

        fragmentHelper = FragmentHelper.getInstance(this);
        try {
            fragmentHelper.setFragmentManagerWithContainer(getFragmentManager(), R.id.container);
        }catch(FragmentException exception){
            Utils.logMessage("could not able to set the fragment manager");
        }
        /*loadFragment(FilterFragment.getInstance(fragmentHelper.getFragmentInstanceTag()),
                null, "FilterFragment", false, false);*/
        loadFragment(ImageFilterFragment.getInstance(fragmentHelper.getFragmentInstanceTag()),
                null, "FilterFragment", false, false);

    }
    public void loadFragment(Fragment fragment, Bundle bundle,String fragmentTag, boolean addToBackStack, boolean hasAnim){
        try {
            if(hasAnim)
                fragmentHelper.applyAnimationForTransition(inAnim,outAnim,popInAnim,popOutAnim);
            fragmentHelper.addFragment(fragment, bundle, fragmentTag, addToBackStack, hasAnim);
        }catch(FragmentException exception){
            Utils.logMessage("fragment could not be able to perform transaction");
        }
    }
    private int inAnim, outAnim, popInAnim, popOutAnim;
    public void setTransition(int inAnim, int outAnim, int popInAnim, int popOutAnim){
        this.inAnim = inAnim;
        this.outAnim = outAnim;
        this.popInAnim = popInAnim;
        this.popOutAnim = popOutAnim;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}
