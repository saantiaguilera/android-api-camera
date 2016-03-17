package com.santiago.controllers;

import android.content.Context;
import android.view.View;

/**
 * Basic controller for a particular View
 *
 * Created by santiaguilera@theamalgama.com on 08/01/16.
 */
public abstract class BaseController<T extends View> {

    private Context context;
    private T view;

    public BaseController(Context context) {
        setContext(context);
    }

    public BaseController(Context context, T t) {
        setContext(context);
        attachView(t);
    }

    //------------------Context stuff--------------------//

    protected Context getContext() {
        if(context==null)
            throw new NullPointerException("No context found");

        return context;
    }

    protected void setContext(Context context){
        this.context = context;
    }

    //--------------------View stuff--------------------//

    /**
     * Since you can either inflate a View or create and add it to something (eg ViewPager adapter)
     * You will always have to attach a View to a controller (hold a reference if needed, or not)
     * getView can be useful if you have nested controllers and you have to get a View for an adapter of another controller.
     *
     * EG.
     *  Activity>
     *  textViewController = new TextViewController(Activity.this, findViewById(R.id.text));
     *
     *  TextViewController>
     *  onAttach(TextView textView) {
     *      this.textView = textView;
     *      initializeViewStuffAndListeners();
     *  }
     *
     *  ALSO.
     *  ViewPagerController>
     *  someMethodThatSetsAdapter(){
     *      viewPager.setAdapter(new Adapter(someViewController.getView(), anotherViewController.getView()));
     *  }
     */
    public T getView() {
        return view;
    } // throws ViewNotAttachedException;
    public void attachView(T t) {
        if(t==null) return;
        onViewAttached(view = t);
    }

    protected abstract void onViewAttached(T t);

}
