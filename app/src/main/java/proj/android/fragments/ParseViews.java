package proj.android.fragments;

import android.view.View;
import android.widget.TextView;

import proj.android.imagefilter.R;

/**
 * Created by deepak on 28/7/15.
 */
public class ParseViews {
    private View baseView;
    private FilterCallback filterCallback;
    ParseViews(View baseView, FilterCallback filterCallback){
        this.baseView = baseView;
        this.filterCallback= filterCallback;
    }

    void initialize(){
        View antique, antiqueNew, blur, blackAndWhite, brannen, bright, brightNew, contrast, contrastNew, duotone, edgeDetect,
                emboss, flip, hueshift, luminance, negative, saturaton, toon, twirl, wrap, doubleTexture;

        setViewAndClick(baseView.findViewById(R.id.filter1));
        setViewAndClick(baseView.findViewById(R.id.filter2));
        setViewAndClick(baseView.findViewById(R.id.filter3));
        setViewAndClick(baseView.findViewById(R.id.filter4));
        setViewAndClick(baseView.findViewById(R.id.filter5));
        setViewAndClick(baseView.findViewById(R.id.filter6));
        setViewAndClick(baseView.findViewById(R.id.filter7));
        setViewAndClick(baseView.findViewById(R.id.filter8));
        setViewAndClick(baseView.findViewById(R.id.filter9));
        setViewAndClick(baseView.findViewById(R.id.filter10));
        setViewAndClick(baseView.findViewById(R.id.filter11));
        setViewAndClick(baseView.findViewById(R.id.filter12));
        setViewAndClick(baseView.findViewById(R.id.filter13));
        setViewAndClick(baseView.findViewById(R.id.filter14));
        setViewAndClick(baseView.findViewById(R.id.filter15));
        setViewAndClick(baseView.findViewById(R.id.filter16));
        setViewAndClick(baseView.findViewById(R.id.filter17));
        setViewAndClick(baseView.findViewById(R.id.filter18));
        setViewAndClick(baseView.findViewById(R.id.filter19));
        setViewAndClick(baseView.findViewById(R.id.filter20));
        setViewAndClick(baseView.findViewById(R.id.filter21));

    }

    private void setViewAndClick(View view){
        switch(view.getId()){
            case R.id.filter21:
                ((TextView)view.findViewById(R.id.filter_text)).setText("DoubleTexture");
                //setClickEvent(view);
                break;
            case R.id.filter20:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Wrap");
                setClickEvent(R.raw.simple_texture_vertex_shader,R.raw.fragment_shader_wrap,view);
                break;
            case R.id.filter19:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Twirl");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_twirl,view);
                break;
            case R.id.filter18:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Toon");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_toon,view);
                break;
            case R.id.filter17:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Saturation");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_saturation,view);
                break;
            case R.id.filter16:
                ((TextView)view.findViewById(R.id.filter_text)).setText("negative");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_negative,view);
                break;
            case R.id.filter15:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Luminance");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_luminance,view);
                break;
            case R.id.filter14:
                ((TextView)view.findViewById(R.id.filter_text)).setText("HueShift");
                setClickEvent(R.raw.simple_texture_vertex_shader,R.raw.fragment_shader_hue_shift,view);
                break;
            case R.id.filter13:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Flip");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_flip,view);
                break;
            case R.id.filter12:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Emboss");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_emboss,view);
                break;
            case R.id.filter11:
                ((TextView)view.findViewById(R.id.filter_text)).setText("EdgeDetect");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_edge_detect,view);
                break;
            case R.id.filter10:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Duotone");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_duotone,view);
                break;
            case R.id.filter9:
                ((TextView)view.findViewById(R.id.filter_text)).setText("ContrastNew");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_contrast_new,view);
                break;
            case R.id.filter8:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Contrast");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_contrast,view);
                break;
            case R.id.filter1:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Antique");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_antique,view);
                break;
            case R.id.filter2:
                ((TextView)view.findViewById(R.id.filter_text)).setText("AntiqueNew");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_antique_new,view);
                break;
            case R.id.filter3:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Blur");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_blur,view);
                break;
            case R.id.filter4:
                ((TextView)view.findViewById(R.id.filter_text)).setText("BlackAndWhite");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_bnw,view);
                break;
            case R.id.filter5:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Brannen");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_brannen,view);
                break;
            case R.id.filter6:
                ((TextView)view.findViewById(R.id.filter_text)).setText("Bright");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_bright,view);
                break;
            case R.id.filter7:
                ((TextView)view.findViewById(R.id.filter_text)).setText("BrightNew");
                setClickEvent(R.raw.simple_texture_vertex_shader, R.raw.fragment_shader_bright_new,view);
                break;
        }

    }

    private void setClickEvent(final int vertexShaderId, final int fragmentShaderId, View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCallback.redrawTexture(vertexShaderId, fragmentShaderId);
            }
        });

    }
}
