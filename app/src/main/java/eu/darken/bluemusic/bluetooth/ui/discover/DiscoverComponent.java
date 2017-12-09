package eu.darken.bluemusic.bluetooth.ui.discover;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dagger.Subcomponent;
import eu.darken.ommvplib.injection.PresenterComponent;
import eu.darken.ommvplib.injection.fragment.support.SupportFragmentComponent;


@DiscoverComponent.Scope
@Subcomponent(modules = {})
public interface DiscoverComponent extends PresenterComponent<DiscoverPresenter.View, DiscoverPresenter>, SupportFragmentComponent<DiscoverFragment> {
    @Subcomponent.Builder
    abstract class Builder extends SupportFragmentComponent.Builder<DiscoverFragment, DiscoverComponent> {

    }

    @Documented
    @javax.inject.Scope
    @Retention(RetentionPolicy.RUNTIME)
    @interface Scope {
    }
}