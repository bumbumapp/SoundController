package eu.darken.bluemusic.bluetooth.ui;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.multibindings.IntoMap;
import eu.darken.bluemusic.bluetooth.ui.discover.DiscoverComponent;
import eu.darken.bluemusic.bluetooth.ui.discover.DiscoverFragment;
import eu.darken.mvpbakery.injection.PresenterComponent;
import eu.darken.mvpbakery.injection.activity.ActivityComponent;
import eu.darken.mvpbakery.injection.fragment.FragmentKey;

@BluetoothActivityComponent.Scope
@Subcomponent(modules = {
        BluetoothActivityComponent.FragmentBinderModule.class
})
public interface BluetoothActivityComponent extends ActivityComponent<BluetoothActivity>, PresenterComponent<BluetoothActivityPresenter, BluetoothActivityComponent> {

    @Subcomponent.Builder
    abstract class Builder extends ActivityComponent.Builder<BluetoothActivity, BluetoothActivityComponent> {

    }

    @javax.inject.Scope
    @Retention(RetentionPolicy.RUNTIME)
    @interface Scope {
    }

    @Module(subcomponents = {
            DiscoverComponent.class
    })
    abstract class FragmentBinderModule {

        @Binds
        @IntoMap
        @FragmentKey(DiscoverFragment.class)
        abstract Factory<? extends Fragment> discover(DiscoverComponent.Builder impl);

    }
}
