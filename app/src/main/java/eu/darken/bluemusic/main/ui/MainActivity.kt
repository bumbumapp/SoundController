package eu.darken.bluemusic.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import eu.darken.bluemusic.R
import eu.darken.bluemusic.main.ui.managed.ManagedDevicesFragment.Companion.newInstance
import eu.darken.mvpbakery.base.MVPBakery.Companion.builder
import eu.darken.mvpbakery.base.ViewModelRetainer
import eu.darken.mvpbakery.injection.ComponentSource
import eu.darken.mvpbakery.injection.InjectedPresenter
import eu.darken.mvpbakery.injection.ManualInjector
import eu.darken.mvpbakery.injection.PresenterInjectionCallback
import eu.darken.mvpbakery.injection.fragment.HasManualFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainActivityPresenter.View, HasManualFragmentInjector {
    @Inject lateinit var componentSource: ComponentSource<Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.BaseAppTheme)
        super.onCreate(savedInstanceState)
        builder<MainActivityPresenter.View, MainActivityPresenter>()
                .presenterFactory(InjectedPresenter(this))
                .presenterRetainer(ViewModelRetainer(this))
                .addPresenterCallback(PresenterInjectionCallback(this))
                .attach(this)
        setContentView(R.layout.activity_layout_main)
        requestRuntimePermissions()
    }

    /** Android 12+: Bluetooth devices need BLUETOOTH_CONNECT; Android 13+: the service notification. */
    private fun requestRuntimePermissions() {
        val wanted = mutableListOf<String>()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S &&
            !eu.darken.bluemusic.bluetooth.core.BluetoothPermission.has(this)
        ) wanted += android.Manifest.permission.BLUETOOTH_CONNECT
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU &&
            androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
            != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) wanted += android.Manifest.permission.POST_NOTIFICATIONS
        if (wanted.isNotEmpty()) requestPermissions(wanted.toTypedArray(), REQUEST_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != REQUEST_PERMISSIONS) return
        val btIndex = permissions.indexOf(android.Manifest.permission.BLUETOOTH_CONNECT)
        if (btIndex >= 0 && grantResults.getOrNull(btIndex) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // The Bluetooth source is a process-wide singleton that read the device list without the
            // permission; restart the app once so it is rebuilt.
            packageManager.getLaunchIntentForPackage(packageName)?.let {
                it.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
            }
            Runtime.getRuntime().exit(0)
        }
    }

    companion object {
        private const val REQUEST_PERMISSIONS = 42
    }

    override fun supportFragmentInjector(): ManualInjector<Fragment> = componentSource



    override fun showDevices() {
        var introFragment = supportFragmentManager.findFragmentById(R.id.frame_content)
        if (introFragment == null) introFragment = newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.frame_content, introFragment).commitAllowingStateLoss()
    }

    private var shouldReCheck = false
    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        shouldReCheck = true
        super.onStop()
    }
}