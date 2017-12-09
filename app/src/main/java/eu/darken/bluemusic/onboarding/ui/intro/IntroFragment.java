package eu.darken.bluemusic.onboarding.ui.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import eu.darken.bluemusic.R;
import eu.darken.bluemusic.util.Preconditions;
import eu.darken.ommvplib.base.OMMVPLib;
import eu.darken.ommvplib.injection.InjectedPresenter;
import eu.darken.ommvplib.injection.PresenterInjectionCallback;


public class IntroFragment extends Fragment implements IntroPresenter.View {

    @Inject IntroPresenter presenter;
    Unbinder unbinder;

    public static Fragment newInstance() {
        return new IntroFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OMMVPLib.<IntroPresenter.View, IntroPresenter>builder()
                .presenterCallback(new PresenterInjectionCallback<>(this))
                .presenterSource(new InjectedPresenter<>(this))
                .attach(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_layout_intro, container, false);
        unbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //noinspection ConstantConditions
        final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        Preconditions.checkNotNull(actionBar);
        actionBar.setTitle(R.string.app_name);
    }

    @OnClick(R.id.finish_onboarding)
    public void onFinishOnboardingClicked(View v) {
        presenter.onFinishOnboardingClicked();
    }

    @Override
    public void closeScreen() {
        //noinspection ConstantConditions
        getActivity().finish();
    }
}
