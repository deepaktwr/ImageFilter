package proj.android.helper;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proj.android.exceptions.FragmentException;

/**
 * Created by deepak on 24/5/15.
 */
public class FragmentHelper {

    private Context context;
    private static final Map<Integer,FragmentHelper> FRAGMENT_HELPER_MAP = new HashMap<>();
    private final Map<String,FragmentManager> fragmentManager = new HashMap<>();
    private final Map<String,Integer> containerId = new HashMap<>();
    private final Map<String,List<Integer>> commitIds = new HashMap<>();
    private final Map<String,List<String>> fragmentTag = new HashMap<>();
    private String DEFAULT_KEY = "INITIAL_KEY";
    private static int activitytaskId;

    private int inAnim, popInAnim, outAnim, popOutAnim;

    //methods has been documented yet
    private FragmentHelper(Context context){
        if(this.context == null)
            this.context = context;
    }

    public static FragmentHelper getInstance(Activity activity){
        activitytaskId = activity.getTaskId();
        if(FRAGMENT_HELPER_MAP.get(activitytaskId) == null){
            FragmentHelper fragmentHelperObj =new FragmentHelper(activity.getBaseContext());
            FRAGMENT_HELPER_MAP.put(activitytaskId, fragmentHelperObj);
        }
        return FRAGMENT_HELPER_MAP.get(activitytaskId);
    }

    public String getFragmentInstanceTag(){
        return DEFAULT_KEY+"_"+activitytaskId;
    }
    public String getFragmentInstanceTag(String managerKey){
        return managerKey+"_"+activitytaskId;
    }

    public synchronized void setFragmentManagerWithContainer(FragmentManager fragmentManager, int containerId) throws FragmentException{
        if(this.fragmentManager.get(DEFAULT_KEY) ==null) {
            this.fragmentManager.put(DEFAULT_KEY, fragmentManager);
            this.containerId.put(DEFAULT_KEY, containerId);
            this.commitIds.put(DEFAULT_KEY, new ArrayList<Integer>());
        }
        else
            throw new FragmentException("manager and container has already been specified for this activity.");
    }

    public synchronized void setNestedFragmentManagerWithContainer(FragmentManager fragmentManager, int containerId, String key) throws FragmentException{
        if(this.fragmentManager.get(DEFAULT_KEY) ==null) {
            DEFAULT_KEY = key;
            this.fragmentManager.put(DEFAULT_KEY, fragmentManager);
            this.containerId.put(DEFAULT_KEY, containerId);
            this.commitIds.put(DEFAULT_KEY, new ArrayList<Integer>());
        }
        else if(this.fragmentManager.get(key) == null){
            this.fragmentManager.put(key, fragmentManager);
            this.containerId.put(key, containerId);
            this.commitIds.put(key, new ArrayList<Integer>());
        }else
            throw new FragmentException("manager with key= "+key+" has already been specified.");
    }

    public synchronized void applyAnimationForTransition(int inAnim, int outAnim, int popInAnim, int popOutAnim) throws FragmentException{
        this.inAnim = inAnim;
        this.outAnim = outAnim;
        this.popInAnim = popInAnim;
        this.popOutAnim = popOutAnim;

        if(inAnim ==0 && popInAnim ==0 && outAnim ==0 && popOutAnim ==0)
            throw new FragmentException("at least one animation should not be zero");
    }

    private boolean checkAnim(){
        return (inAnim|outAnim|popInAnim|popOutAnim) != 0x0f;
    }

    public synchronized void replaceFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim) throws FragmentException{
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new FragmentException("manager and container has not been specified yet");
        if(fragment == null)
            throw new FragmentException("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new FragmentException(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(DEFAULT_KEY).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            throw new FragmentException("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions");

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(DEFAULT_KEY).contains(fragmentTag))
                throw new FragmentException("tag of this fragment has already defined");
            this.fragmentTag.get(DEFAULT_KEY).add(fragmentTag);
            transaction.replace(containerId.get(DEFAULT_KEY), fragment, fragmentTag);
        }else {
            String newFragmentTag = getGeneratedFragmentTag(DEFAULT_KEY, fragment.getClass().getName());
            this.fragmentTag.get(DEFAULT_KEY).add(newFragmentTag);
            transaction.replace(containerId.get(DEFAULT_KEY), fragment, newFragmentTag);
        }

        if(addToBackStack) {
            if(this.fragmentTag.get(DEFAULT_KEY).size()>0)
                transaction.addToBackStack(this.fragmentTag.get(DEFAULT_KEY).get(this.fragmentTag.get(DEFAULT_KEY).size()-1));
            else{
                String newFragmentTag = getGeneratedFragmentTag(DEFAULT_KEY, "INITIAL_FRAGMENT");
                this.fragmentTag.get(DEFAULT_KEY).add(newFragmentTag);
                transaction.addToBackStack(newFragmentTag);
            }
        }

        try{
            commitIds.get(DEFAULT_KEY).add(transaction.commit());
        }catch(IllegalStateException exp){
            throw new FragmentException("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    private String getGeneratedFragmentTag(String managerKey, String currentFragmentName){
        int backStacks = fragmentManager.get(managerKey).getBackStackEntryCount();
        int commits = commitIds.get(managerKey).size();

        return managerKey+"_"+backStacks+"_"+commits+"_"+currentFragmentName+"_"+System.currentTimeMillis();
    }

    public synchronized void replaceNestedFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim, String managerKey) throws FragmentException{
        if(TextUtils.isEmpty(managerKey))
            throw new FragmentException("managerKey is the key you have given to the nested " +
                    "fragment manager, specify key or call replaceFragment instead");
        if(fragmentManager.get(managerKey) == null)
            throw new FragmentException("manager and container has not been specified yet");
        if(fragment == null)
            throw new FragmentException("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new FragmentException(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(managerKey).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            throw new FragmentException("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions");

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(managerKey).contains(fragmentTag))
                throw new FragmentException("tag of this fragment has already defined");
            this.fragmentTag.get(managerKey).add(fragmentTag);
            transaction.replace(containerId.get(managerKey), fragment, fragmentTag);
        }else {
            String newFragmentTag = getGeneratedFragmentTag(managerKey, fragment.getClass().getName());
            this.fragmentTag.get(managerKey).add(newFragmentTag);
            transaction.replace(containerId.get(managerKey), fragment, newFragmentTag);
        }

        if(addToBackStack) {
            if(this.fragmentTag.get(managerKey).size()>0)
                transaction.addToBackStack(this.fragmentTag.get(managerKey).get(this.fragmentTag.get(managerKey).size()-1));
            else{
                String newFragmentTag = getGeneratedFragmentTag(managerKey, "INITIAL_FRAGMENT");
                this.fragmentTag.get(managerKey).add(newFragmentTag);
                transaction.addToBackStack(newFragmentTag);
            }
        }

        try{
            commitIds.get(managerKey).add(transaction.commit());
        }catch(IllegalStateException exp){
            throw new FragmentException("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    public synchronized void addFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim) throws FragmentException{
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new FragmentException("manager and container has not been specified yet");
        if(fragment == null)
            throw new FragmentException("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new FragmentException(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(DEFAULT_KEY).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            throw new FragmentException("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions");

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(DEFAULT_KEY).contains(fragmentTag))
                throw new FragmentException("tag of this fragment has already defined");
            this.fragmentTag.get(DEFAULT_KEY).add(fragmentTag);
            transaction.replace(containerId.get(DEFAULT_KEY), fragment, fragmentTag);
        }else {
            String newFragmentTag = getGeneratedFragmentTag(DEFAULT_KEY, fragment.getClass().getName());
            this.fragmentTag.get(DEFAULT_KEY).add(newFragmentTag);
            transaction.replace(containerId.get(DEFAULT_KEY), fragment, newFragmentTag);
        }

        if(addToBackStack) {
            if(this.fragmentTag.get(DEFAULT_KEY).size()>0)
                transaction.addToBackStack(this.fragmentTag.get(DEFAULT_KEY).get(this.fragmentTag.get(DEFAULT_KEY).size()-1));
            else{
                String newFragmentTag = getGeneratedFragmentTag(DEFAULT_KEY, "INITIAL_FRAGMENT");
                this.fragmentTag.get(DEFAULT_KEY).add(newFragmentTag);
                transaction.addToBackStack(newFragmentTag);
            }
        }

        try{
            commitIds.get(DEFAULT_KEY).add(transaction.commit());
        }catch(IllegalStateException exp){
            throw new FragmentException("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    public synchronized void addNestedFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim, String managerKey) throws FragmentException{
        if(TextUtils.isEmpty(managerKey))
            throw new FragmentException("managerKey is the key you have given to the nested " +
                    "fragment manager, specify key or call addFragment instead");
        if(fragmentManager.get(managerKey) == null)
            throw new FragmentException("manager and container has not been specified yet");
        if(fragment == null)
            throw new FragmentException("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new FragmentException(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(managerKey).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            throw new FragmentException("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions");

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(managerKey).contains(fragmentTag))
                throw new FragmentException("tag of this fragment has already defined");
            this.fragmentTag.get(managerKey).add(fragmentTag);
            transaction.replace(containerId.get(managerKey), fragment, fragmentTag);
        }else {
            String newFragmentTag = getGeneratedFragmentTag(managerKey, fragment.getClass().getName());
            this.fragmentTag.get(managerKey).add(newFragmentTag);
            transaction.replace(containerId.get(managerKey), fragment, newFragmentTag);
        }

        if(addToBackStack) {
            if(this.fragmentTag.get(managerKey).size()>0)
                transaction.addToBackStack(this.fragmentTag.get(managerKey).get(this.fragmentTag.get(managerKey).size()-1));
            else{
                String newFragmentTag = getGeneratedFragmentTag(managerKey, "INITIAL_FRAGMENT");
                this.fragmentTag.get(managerKey).add(newFragmentTag);
                transaction.addToBackStack(newFragmentTag);
            }
        }

        try{
            commitIds.get(managerKey).add(transaction.commit());
        }catch(IllegalStateException exp){
            throw new FragmentException("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    public synchronized void removeFragment(Fragment fragment) throws FragmentException{
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new FragmentException("manager and container has not been specified yet");
        boolean isFragNull = fragment == null;
        if(isFragNull)
            throw new FragmentException("fragment is null");
        FragmentTransaction transaction = fragmentManager.get(DEFAULT_KEY).beginTransaction();
        if(!isFragNull)
            transaction = transaction.remove(fragment);
        else
            throw new FragmentException("no fragment has been attached with this manager");

        try{
            transaction.commit();
            commitIds.get(DEFAULT_KEY).remove(commitIds.get(DEFAULT_KEY).size()-1);
            fragmentTag.get(DEFAULT_KEY).remove(fragmentTag.get(DEFAULT_KEY).size()-1);
            //need to remove from back stack if added, need to check that*********************************************

        }catch(IllegalStateException exp){
            throw new FragmentException("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }catch(NullPointerException exp){
            throw new FragmentException("trying to remove fragment which has not been added before");
        }

    }

    public synchronized void removeNestedFragment(Fragment fragment, String managerKey) throws FragmentException{

        if(TextUtils.isEmpty(managerKey))
            throw new FragmentException("managerKey is the key you have given to the nested " +
                    "fragment manager, specify key or call removeFragment instead");
        if(fragmentManager.get(managerKey) == null)
            throw new FragmentException("manager and container has not been specified yet");
        boolean isFragNull = fragment == null;
        if(isFragNull)
            throw new FragmentException("fragment is null");
        FragmentTransaction transaction = fragmentManager.get(managerKey).beginTransaction();
        if(!isFragNull)
            transaction = transaction.remove(fragment);
        else
            throw new FragmentException("no fragment has been attached with this manager");

        try{
            transaction.commit();
            commitIds.get(managerKey).remove(commitIds.get(managerKey).size()-1);
        }catch(IllegalStateException exp){
            throw new FragmentException("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }catch(NullPointerException exp){
            throw new FragmentException("trying to remove fragment which has not been added before");
        }

    }

    //methods below has not been written yet
    public synchronized void popBackStack() throws FragmentException{}

    public synchronized void popBackStackNested(String managerKey) throws FragmentException{}

    public synchronized void popBackStack(String tillTag, int flag) throws FragmentException{}

    public synchronized void popBackStackNested(String tillTag, int flag, String managerKey) throws FragmentException{}

    public synchronized void popBackStack(int tillCommit, int flag) throws FragmentException{}

    public synchronized void popBackStackNested(int tillCommit, int flag, String managerKey) throws FragmentException{}

    public synchronized void popBackStackImmediate() throws FragmentException{}

    public synchronized void popBackStackImmediateNested(String managerKey) throws FragmentException{}

    public synchronized void popBackStackImmediate(String tillTag, int flag) throws FragmentException{}

    public synchronized void popBackStackImmediateNested(String tillTag, int flag, String managerKey) throws FragmentException{}

    public synchronized void popBackStackImmediate(int tillCommit, int flag) throws FragmentException{}

    public synchronized void popBackStackImmediateNested(int tillCommit, int flag, String managerKey) throws FragmentException{}

    public synchronized void putFragment(Bundle bundleOfInstance, Fragment fragment, String tag) throws FragmentException{}

    public synchronized void putFragmentNested(Bundle bundleOfInstance, Fragment fragment, String tag, String managerKey) throws FragmentException{}

    public synchronized Fragment getFragment(Bundle bundleOfInstance, String tag) throws FragmentException{ return null;}

    public synchronized Fragment getFragmentNested(Bundle bundleOfInstance, String tag, String managerKey) throws FragmentException{ return null;}

    public synchronized void setTragetFragment(Fragment fragment) throws FragmentException{}

    public synchronized void setTragetFragmentNested(Fragment fragment, String managerKey) throws FragmentException{}

    public synchronized Fragment getTargetFragment() throws FragmentException{ return null;}

    public synchronized Fragment getTargetFragmentNested(String managerKey) throws FragmentException{ return null;}

}
