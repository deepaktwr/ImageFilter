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

import proj.android.exceptions.BlockExecution;
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
    private static int ACTIVITY_TASK_ID;
    private  final int activityTaskId;

    private int inAnim, popInAnim, outAnim, popOutAnim;

    //methods has not been documented yet
    private FragmentHelper(Context context){
        this.context = context;
        this.activityTaskId = ACTIVITY_TASK_ID;
    }

    public static FragmentHelper getInstance(Activity activity){
        ACTIVITY_TASK_ID = activity.getTaskId();
        if(FRAGMENT_HELPER_MAP.get(ACTIVITY_TASK_ID) == null){
            FragmentHelper fragmentHelperObj =new FragmentHelper(activity.getBaseContext());
            FRAGMENT_HELPER_MAP.put(ACTIVITY_TASK_ID, fragmentHelperObj);
        }
        return FRAGMENT_HELPER_MAP.get(ACTIVITY_TASK_ID);
    }

    public FragmentManager getFragmentManager() throws FragmentException{
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new FragmentException("manager has not been initialized yet," +
                    " make sure the manager with container has been set before");
        return fragmentManager.get(DEFAULT_KEY);
    }
    public FragmentManager getFragmentManager(String key) throws FragmentException{
        if(fragmentManager.get(key) == null)
            throw new FragmentException(Utils.formatMessage("manager with key = %s has not been initialized yet," +
                    " make sure the manager with container has been set with this key before", key));
        return fragmentManager.get(key);
    }

    public int getContainer() throws FragmentException{
        if(containerId.get(DEFAULT_KEY) == 0)
            throw new FragmentException("container id has not been initialized yet," +
                    " make sure the manager with container has been set before");
        return containerId.get(DEFAULT_KEY);
    }
    public int getContainer(String key) throws FragmentException{
        if(containerId.get(key) == 0)
            throw new FragmentException(Utils.formatMessage("container id with key = %s has not been initialized yet," +
                    " make sure the manager with container has been set with this key before", key));
        return containerId.get(key);
    }

    public String getFragmentInstanceTag(){
        return DEFAULT_KEY+"_"+ activityTaskId;
    }
    public String getFragmentInstanceTag(String managerKey){
        return managerKey+"_"+ activityTaskId;
    }

    public synchronized void setFragmentManagerWithContainer(FragmentManager fragmentManager, int containerId) throws FragmentException{
        if(this.fragmentManager.get(DEFAULT_KEY) ==null) {
            this.fragmentManager.put(DEFAULT_KEY, fragmentManager);
            this.containerId.put(DEFAULT_KEY, containerId);
            this.commitIds.put(DEFAULT_KEY, new ArrayList<Integer>());
            this.fragmentTag.put(DEFAULT_KEY, new ArrayList<String>());
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
            this.fragmentTag.put(DEFAULT_KEY, new ArrayList<String>());
        }
        else if(this.fragmentManager.get(key) == null){
            this.fragmentManager.put(key, fragmentManager);
            this.containerId.put(key, containerId);
            this.commitIds.put(key, new ArrayList<Integer>());
            this.fragmentTag.put(key, new ArrayList<String>());
        }else
            throw new FragmentException(Utils.formatMessage("manager with key= %s has already been specified", key));
    }

    public synchronized void applyAnimationForTransition(int inAnim, int outAnim, int popInAnim, int popOutAnim) throws FragmentException{
        this.inAnim = inAnim;
        this.outAnim = outAnim;
        this.popInAnim = popInAnim;
        this.popOutAnim = popOutAnim;

        if(inAnim ==0 && popInAnim ==0 && outAnim ==0 && popOutAnim ==0)
            throw new FragmentException("at least one animation should not be zero, method don't need to be called if no" +
                    " animation between fragments need to be performed");
    }

    private boolean checkAnim(){
        return (inAnim|outAnim|popInAnim|popOutAnim) != 0x0f;
    }

    public synchronized void replaceFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim){
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new BlockExecution("manager and container has not been specified yet");
        if(fragment == null)
            throw new BlockExecution("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new BlockExecution(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(DEFAULT_KEY).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            Utils.logError(Utils.formatMessage("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions if methods has hasAnim as true " +
                    "otherwise make parameter false for fragment %s", fragment.toString()));

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(DEFAULT_KEY).contains(fragmentTag))
                throw new BlockExecution(Utils.formatMessage("given tag of the fragment %s has already defined with existing manager" +
                        " for this or any other fragment," +
                        " use different tag", fragment.toString()));
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
            throw new BlockExecution("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " of activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    private String getGeneratedFragmentTag(String managerKey, String currentFragmentName){
        int backStacks = fragmentManager.get(managerKey).getBackStackEntryCount();
        int commits = commitIds.get(managerKey).size();

        return managerKey+"_"+backStacks+"_"+commits+"_"+currentFragmentName+"_"+System.currentTimeMillis();
    }

    public synchronized void replaceNestedFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim, String managerKey){
        if(TextUtils.isEmpty(managerKey))
            throw new BlockExecution("managerKey is the key you have given to the nested " +
                    "fragment manager, specify key or call replaceFragment instead");
        if(fragmentManager.get(managerKey) == null)
            throw new BlockExecution("manager and container has not been specified yet");
        if(fragment == null)
            throw new BlockExecution("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new BlockExecution(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(managerKey).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            Utils.logError(Utils.formatMessage("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions if methods has hasAnim as true " +
                    "otherwise make parameter false for fragment %s", fragment.toString()));

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(managerKey).contains(fragmentTag))
                throw new BlockExecution(Utils.formatMessage("given tag of the fragment %s has already defined with existing manager" +
                        " for this or any other fragment," +
                        " use different tag", fragment.toString()));
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
            throw new BlockExecution("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    public synchronized void addFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim){
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new BlockExecution("manager and container has not been specified yet");
        if(fragment == null)
            throw new BlockExecution("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new BlockExecution(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(DEFAULT_KEY).beginTransaction();
        if(hasAnim){
            if(checkAnim())
                transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
            else
                Utils.logError(Utils.formatMessage("no transition animation has been set, call " +
                        "applyAnimationForTransition before this method to apply transitions if methods has hasAnim as true " +
                        "otherwise make parameter false for fragment %s", fragment.toString()));
        }


        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(DEFAULT_KEY).contains(fragmentTag))
                throw new BlockExecution(Utils.formatMessage("given tag of the fragment %s has already defined with existing manager" +
                        " for this or any other fragment," +
                        " use different tag", fragment.toString()));
            this.fragmentTag.get(DEFAULT_KEY).add(fragmentTag);
            transaction.add(containerId.get(DEFAULT_KEY), fragment, fragmentTag);
        }else {
            String newFragmentTag = getGeneratedFragmentTag(DEFAULT_KEY, fragment.getClass().getName());
            this.fragmentTag.get(DEFAULT_KEY).add(newFragmentTag);
            transaction.add(containerId.get(DEFAULT_KEY), fragment, newFragmentTag);
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
            throw new BlockExecution("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    public synchronized void addNestedFragment(Fragment fragment, Bundle bundle, String fragmentTag, boolean addToBackStack, boolean hasAnim, String managerKey) {
        if(TextUtils.isEmpty(managerKey))
            throw new BlockExecution("managerKey is the key you have given to the nested " +
                    "fragment manager, specify key or call addFragment instead");
        if(fragmentManager.get(managerKey) == null)
            throw new BlockExecution("manager and container has not been specified yet");
        if(fragment == null)
            throw new BlockExecution("fragment is null");
        if(bundle != null) {
            try {
                fragment.setArguments(bundle);
            } catch (IllegalStateException e) {
                try {
                    fragment.getArguments().putAll(bundle);
                }catch(Exception exp) {
                    throw new BlockExecution(e.getMessage(), exp);
                }
            }
        }

        FragmentTransaction transaction = fragmentManager.get(managerKey).beginTransaction();
        if(hasAnim && checkAnim())
            transaction.setCustomAnimations(inAnim, outAnim, popInAnim, popOutAnim);
        else
            Utils.logError(Utils.formatMessage("no transition animation has been set, call " +
                    "applyAnimationForTransition before this method to apply transitions if methods has hasAnim as true " +
                    "otherwise make parameter false for fragment %s", fragment.toString()));

        if(!TextUtils.isEmpty(fragmentTag)) {
            if(this.fragmentTag.get(managerKey).contains(fragmentTag))
                throw new BlockExecution(Utils.formatMessage("given tag of the fragment %s has already defined with existing manager" +
                        " for this or any other fragment," +
                        " use different tag", fragment.toString()));
            this.fragmentTag.get(managerKey).add(fragmentTag);
            transaction.add(containerId.get(managerKey), fragment, fragmentTag);
        }else {
            String newFragmentTag = getGeneratedFragmentTag(managerKey, fragment.getClass().getName());
            this.fragmentTag.get(managerKey).add(newFragmentTag);
            transaction.add(containerId.get(managerKey), fragment, newFragmentTag);
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
            throw new BlockExecution("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }
        inAnim =0;outAnim=0;popOutAnim=0;popInAnim=0;
    }

    public synchronized void removeFragment(Fragment fragment) throws FragmentException{
        if(fragmentManager.get(DEFAULT_KEY) == null)
            throw new BlockExecution("manager and container has not been specified yet");
        boolean isFragNull = fragment == null;
        if(isFragNull)
            throw new BlockExecution("fragment is null");
        FragmentTransaction transaction = fragmentManager.get(DEFAULT_KEY).beginTransaction();
        if(!isFragNull && transaction!=null)
            transaction = transaction.remove(fragment);
        else if (transaction == null)
            throw new FragmentException("could not begin transaction with with fragment manager to remove fragment "
                    +fragment.toString());

        try{
            transaction.commit();
            commitIds.get(DEFAULT_KEY).remove(commitIds.get(DEFAULT_KEY).size()-1);
            fragmentTag.get(DEFAULT_KEY).remove(fragmentTag.get(DEFAULT_KEY).size()-1);
            //*******need to remove from back stack if added, need to check that*********************************************

        }catch(IllegalStateException exp){
            throw new BlockExecution(Utils.formatMessage("activity state has been lost, call transaction" +
                    " in onPostResume for the fragment %s instead of any other general or async method" +
                    " from activity", fragment.toString()),exp);
        }catch(NullPointerException exp){
            throw new FragmentException(Utils.formatMessage("trying to remove fragment %s which has not been added before",
                    fragment.toString()), exp);
        }

    }

    public synchronized void removeNestedFragment(Fragment fragment, String managerKey) throws FragmentException{

        if(TextUtils.isEmpty(managerKey))
            throw new BlockExecution("managerKey is the key you have given to the nested " +
                    "fragment manager, specify key or call removeFragment instead");
        if(fragmentManager.get(managerKey) == null)
            throw new BlockExecution("manager and container has not been specified yet");
        boolean isFragNull = fragment == null;
        if(isFragNull)
            throw new BlockExecution("fragment is null");
        FragmentTransaction transaction = fragmentManager.get(managerKey).beginTransaction();
        if(!isFragNull && transaction!=null)
            transaction = transaction.remove(fragment);
        else if (transaction == null)
            throw new FragmentException("could not begin transaction with with fragment manager to remove fragment "
                    +fragment.toString());

        try{
            transaction.commit();
            commitIds.get(managerKey).remove(commitIds.get(managerKey).size()-1);
        }catch(IllegalStateException exp){
            throw new BlockExecution("activity state has been lost, call transaction" +
                    " in onPostResume instead of any other general or async method" +
                    " from activity.",exp);
        }catch(NullPointerException exp){
            throw new FragmentException(Utils.formatMessage("trying to remove fragment %s which has not been added before",
                    fragment.toString()), exp);
        }

    }

    //methods below has not been written yet
    public synchronized void popBackStack() {}

    public synchronized void popBackStackNested(String managerKey) {}

    public synchronized void popBackStack(String tillTag, int flag) {}

    public synchronized void popBackStackNested(String tillTag, int flag, String managerKey) {}

    public synchronized void popBackStack(int tillCommit, int flag) {}

    public synchronized void popBackStackNested(int tillCommit, int flag, String managerKey) {}

    public synchronized void popBackStackImmediate() {}

    public synchronized void popBackStackImmediateNested(String managerKey) {}

    public synchronized void popBackStackImmediate(String tillTag, int flag) {}

    public synchronized void popBackStackImmediateNested(String tillTag, int flag, String managerKey) {}

    public synchronized void popBackStackImmediate(int tillCommit, int flag) {}

    public synchronized void popBackStackImmediateNested(int tillCommit, int flag, String managerKey) {}

    public synchronized void putFragment(Bundle bundleOfInstance, Fragment fragment, String tag) {}

    public synchronized void putFragmentNested(Bundle bundleOfInstance, Fragment fragment, String tag, String managerKey) {}

    public synchronized Fragment getFragment(Bundle bundleOfInstance, String tag) { return null;}

    public synchronized Fragment getFragmentNested(Bundle bundleOfInstance, String tag, String managerKey) { return null;}

    public synchronized void setTragetFragment(Fragment fragment) {}

    public synchronized void setTragetFragmentNested(Fragment fragment, String managerKey) {}

    public synchronized Fragment getTargetFragment() { return null;}

    public synchronized Fragment getTargetFragmentNested(String managerKey) { return null;}


}
