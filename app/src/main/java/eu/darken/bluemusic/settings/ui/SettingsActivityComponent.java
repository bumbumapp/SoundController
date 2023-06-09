package eu.darken.bluemusic.settings.ui;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.fragment.app.Fragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.multibindings.IntoMap;
import eu.darken.bluemusic.settings.ui.about.AboutComponent;
import eu.darken.bluemusic.settings.ui.about.AboutFragment;
import eu.darken.bluemusic.settings.ui.advanced.AdvancedComponent;
import eu.darken.bluemusic.settings.ui.advanced.AdvancedFragment;
import eu.darken.bluemusic.settings.ui.general.SettingsComponent;
import eu.darken.bluemusic.settings.ui.general.SettingsFragment;
import eu.darken.mvpbakery.injection.PresenterComponent;
import eu.darken.mvpbakery.injection.activity.ActivityComponent;
import eu.darken.mvpbakery.injection.fragment.FragmentKey;

@SettingsActivityComponent.Scope
@Subcomponent(modules = {
        SettingsActivityComponent.FragmentBinderModule.class
})
public interface SettingsActivityComponent extends ActivityComponent<SettingsActivity>, PresenterComponent<SettingsActivityPresenter, SettingsActivityComponent> {

    @Subcomponent.Builder
    abstract class Builder extends ActivityComponent.Builder<SettingsActivity, SettingsActivityComponent> {

    }

    @javax.inject.Scope
    @Retention(RetentionPolicy.RUNTIME)
    @interface Scope {
    }

    @Module(subcomponents = {
            SettingsComponent.class,
            AdvancedComponent.class,
            AboutComponent.class
    })
    abstract class FragmentBinderModule {

        @Binds
        @IntoMap
        @FragmentKey(SettingsFragment.class)
        abstract Factory<? extends Fragment> settings(SettingsComponent.Builder impl);

        @Binds
        @IntoMap
        @FragmentKey(AdvancedFragment.class)
        abstract Factory<? extends Fragment> advanced(AdvancedComponent.Builder impl);

        @Binds
        @IntoMap
        @FragmentKey(AboutFragment.class)
        abstract Factory<? extends Fragment> about(AboutComponent.Builder impl);
    }
}
