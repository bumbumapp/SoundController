package eu.darken.bluemusic.util.mvp;

import android.content.Context;
import android.os.Bundle;


public class PresenterLoader<PresenterT extends BasePresenter<ViewT>, ViewT> extends ObjectRetainLoader<PresenterT> {

    private Bundle savedState;

    public PresenterLoader(Context context, PresenterFactory<PresenterT> factory, Bundle savedState) {
        super(context, factory);
        this.savedState = savedState;
    }

    public PresenterT getPresenter() {
        return getObject();
    }

    @Override
    protected void createObjectToRetain() {
        super.createObjectToRetain();
        getPresenter().onCreate(savedState);
    }

    @Override
    protected void clearDataAfterCreation() {
        super.clearDataAfterCreation();
        savedState = null;
    }
}
