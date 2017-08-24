package eu.darken.bluemusic.screens.volumes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dagger.Subcomponent;
import eu.darken.ommvplib.injection.PresenterComponent;
import eu.darken.ommvplib.injection.fragment.support.SupportFragmentComponent;


@VolumesComponent.Scope
@Subcomponent(modules = {})
public interface VolumesComponent extends PresenterComponent<VolumesPresenter.View, VolumesPresenter>, SupportFragmentComponent<VolumesFragment> {
    @Subcomponent.Builder
    abstract class Builder extends SupportFragmentComponent.Builder<VolumesFragment, VolumesComponent> {

    }

    @Documented
    @javax.inject.Scope
    @Retention(RetentionPolicy.RUNTIME)
    @interface Scope {
    }
}
