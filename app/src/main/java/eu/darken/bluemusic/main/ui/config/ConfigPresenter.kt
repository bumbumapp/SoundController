package eu.darken.bluemusic.main.ui.config

import android.app.Activity
import android.app.NotificationManager
import eu.darken.bluemusic.main.core.audio.AudioStream
import eu.darken.bluemusic.main.core.audio.StreamHelper
import eu.darken.bluemusic.main.core.database.DeviceManager
import eu.darken.bluemusic.main.core.database.ManagedDevice
import eu.darken.bluemusic.settings.core.Settings
import eu.darken.bluemusic.util.ApiHelper
import eu.darken.bluemusic.util.AppTool
import eu.darken.bluemusic.util.WakelockMan
import eu.darken.mvpbakery.base.Presenter
import eu.darken.mvpbakery.injection.ComponentPresenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@ConfigComponent.Scope
class ConfigPresenter @Inject internal constructor(
        private val deviceManager: DeviceManager,
        private val streamHelper: StreamHelper,
        private val appTool: AppTool,
        private val notificationManager: NotificationManager,
        private val wakelockMan: WakelockMan
) : ComponentPresenter<ConfigPresenter.View, ConfigComponent>() {

    private var upgradeSub: Disposable? = null
    private var updateSub: Disposable? = null
    private var isProVersion = false
    private lateinit var deviceAddress: String
    private var _device: ManagedDevice? = null
    private val device: ManagedDevice
        get() = _device!!

    fun setDevice(address: String) {
        deviceAddress = address
    }

    override fun onBindChange(view: View?) {
        super.onBindChange(view)
        updatePro()
        updateDevice()
    }

    private fun updateDevice() {
        if (view != null) {
            updateSub = deviceManager.devices()
                    .subscribeOn(Schedulers.io())
                    .map { deviceMap: Map<String?, ManagedDevice> ->
                        for (d in deviceMap.values) {
                            if (d.address == deviceAddress) return@map d
                        }
                        throw IllegalStateException()
                    }
                    .doOnNext { d: ManagedDevice? -> Timber.d("Updating device: %s", d) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { device: ManagedDevice ->
                                this@ConfigPresenter._device = device
                                withView { it.updateDevice(device) }
                            },
                            { withView { it.finishScreen() } }
                    )
        } else if (updateSub != null) updateSub!!.dispose()
    }

    private fun updatePro() {
        if (view != null) {
        } else if (upgradeSub != null) upgradeSub!!.dispose()
    }

    fun onPurchaseUpgrade(activity: Activity) {

    }

    fun onToggleMusicVolume() {
        if (device.getVolume(AudioStream.Type.MUSIC) == null) {
            device.setVolume(AudioStream.Type.MUSIC, streamHelper.getVolumePercentage(device.getStreamId(AudioStream.Type.MUSIC)))
        } else device.setVolume(AudioStream.Type.MUSIC, null)
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to toggle music volume.") }
                )
    }

    fun onToggleCallVolume() {
        if (device.getVolume(AudioStream.Type.CALL) == null) {
            device.setVolume(AudioStream.Type.CALL, streamHelper.getVolumePercentage(device.getStreamId(AudioStream.Type.CALL)))
        } else device.setVolume(AudioStream.Type.CALL, null)
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to toggle call volume.") }
                )
    }

    fun onToggleRingVolume(): Boolean {

        if (device.getVolume(AudioStream.Type.RINGTONE) == null) {
            if (ApiHelper.hasMarshmallow() && !notificationManager.isNotificationPolicyAccessGranted) {
                // Missing permissions for this, reset value
                withView { it.showNotificationPermissionView() }
            }
            device.setVolume(AudioStream.Type.RINGTONE, streamHelper.getVolumePercentage(device.getStreamId(AudioStream.Type.RINGTONE)))
        } else {
            device.setVolume(AudioStream.Type.RINGTONE, null)
        }
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to toggle ring volume.") }
                )
        return device.getVolume(AudioStream.Type.RINGTONE) != null
    }

    fun onToggleNotificationVolume(): Boolean {

        if (device.getVolume(AudioStream.Type.NOTIFICATION) == null) {
            if (ApiHelper.hasMarshmallow() && !notificationManager.isNotificationPolicyAccessGranted) {
                // Missing permissions for this, reset value
                withView { it.showNotificationPermissionView() }
            }
            device.setVolume(AudioStream.Type.NOTIFICATION, streamHelper.getVolumePercentage(device.getStreamId(AudioStream.Type.NOTIFICATION)))
        } else {
            device.setVolume(AudioStream.Type.NOTIFICATION, null)
        }
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to toggle notification volume.") }
                )
        return device.getVolume(AudioStream.Type.NOTIFICATION) != null
    }

    fun onToggleAlarmVolume(): Boolean {

        if (device.getVolume(AudioStream.Type.ALARM) == null) {
            device.setVolume(AudioStream.Type.ALARM, streamHelper.getVolumePercentage(device.getStreamId(AudioStream.Type.ALARM)))
        } else {
            device.setVolume(AudioStream.Type.ALARM, null)
        }
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to toggle alarm volume.") }
                )
        return device.getVolume(AudioStream.Type.ALARM) != null
    }

    fun onToggleAutoPlay(): Boolean {

            device.autoPlay = !device.autoPlay
            deviceManager.save(setOf(device))
                    .subscribeOn(Schedulers.computation())
                    .subscribe(
                            {},
                            { e -> Timber.e(e, "Failed to toggle autoplay.") }
                    )

        return device.autoPlay
    }

    fun onToggleVolumeLock(): Boolean {

            device.volumeLock = !device.volumeLock
            deviceManager.save(setOf(device))
                    .subscribeOn(Schedulers.computation())
                    .subscribe(
                            {},
                            { e -> Timber.e(e, "Failed to toggle volume lock.") }
                    )

        return device.volumeLock
    }

    fun onToggleKeepAwake(): Boolean {

            device.keepAwake = !device.keepAwake
            deviceManager.save(setOf(device))
                    .subscribeOn(Schedulers.computation())
                    .subscribe(
                            { devices ->
                                if (devices.values.any { it.isActive && it.keepAwake }) {
                                    wakelockMan.tryAquire()
                                } else if (devices.values.none { it.isActive && it.keepAwake }) {
                                    wakelockMan.tryRelease()
                                }
                            },
                            { e -> Timber.e(e, "Failed to toggle keep alive.") }
                    )

        return device.keepAwake
    }


    fun onToggleNudgeVolume(): Boolean {
        device.nudgeVolume = !device.nudgeVolume
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to toggle nudge volume.") }
                )
        return device.nudgeVolume
    }

    fun onEditReactionDelayClicked() {
        val delay = if (device.actionDelay != null) device.actionDelay!! else Settings.DEFAULT_REACTION_DELAY
        withView { it.showReactionDelayDialog(delay) }
    }

    fun onEditReactionDelay(_delay: Long) {
        var delay = _delay
        if (delay < -1) delay = -1
        device.actionDelay = if (delay == -1L) null else delay
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to edit reaction delay.") }
                )
    }

    fun onEditAdjustmentDelayClicked() {
        val delay = if (device.adjustmentDelay != null) device.adjustmentDelay!! else Settings.DEFAULT_ADJUSTMENT_DELAY
        withView { it.showAdjustmentDelayDialog(delay) }
    }

    fun onEditAdjustmentDelay(_delay: Long) {
        var delay = _delay
        if (delay < -1) delay = -1
        device.adjustmentDelay = if (delay == -1L) null else delay
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to update adjustment delay.") }
                )
    }

    fun onEditMonitoringDurationClicked() {
        val delay = if (device.monitoringDuration != null) device.monitoringDuration!! else Settings.DEFAULT_MONITORING_DURATION
        withView { it.showMonitoringDurationDialog(delay) }
    }

    fun onEditMonitoringDuration(_duration: Long) {
        var duration = _duration
        if (duration < -1) duration = -1
        device.monitoringDuration = if (duration == -1L) null else duration
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.computation())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to edit monitoring duration.") }
                )
    }

    fun onRenameClicked() {
     withView { it.showRenameDialog(device.alias) }

    }

    fun onRenameDevice(newAlias: String?) {
        if (newAlias == null) device.setAlias(device.name!!) else device.setAlias(newAlias)
        deviceManager.updateDevices()
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun onDeleteDevice() {
        deviceManager.removeDevice(device)
                .subscribeOn(Schedulers.io())
                .subscribe()
        withView {
            it.showUndoDeletion {
                deviceManager.addNewDevice(device.sourceDevice)
                        .subscribeOn(Schedulers.io())
                        .subscribe()
            }
        }
    }

    fun onLaunchAppClicked() {

            @Suppress("UNCHECKED_CAST")
            Single.create<List<AppTool.Item>> { it.onSuccess(appTool.apps) }
                    .subscribeOn(Schedulers.io())
                    .map { apps: List<AppTool.Item> ->
                        apps.toMutableList().apply {
                            add(0, AppTool.Item.empty())
                        }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { apps: List<AppTool.Item> -> withView { it.showAppSelectionDialog(apps) } }

    }

    fun onClearLaunchApp() {
        onLaunchAppSelected(AppTool.Item.empty())
    }

    fun onLaunchAppSelected(item: AppTool.Item) {
        device.launchPkg = item.packageName
        deviceManager.save(setOf(device))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {},
                        { e -> Timber.e(e, "Failed to update launch app.") }
                )
    }

    interface View : Presenter.View {
        fun updateProState(isPro: Boolean)
        fun updateDevice(device: ManagedDevice)
        fun showRequiresPro()
        fun showReactionDelayDialog(delay: Long)
        fun showAdjustmentDelayDialog(delay: Long)
        fun showMonitoringDurationDialog(duration: Long)
        fun showAppSelectionDialog(items: List<AppTool.Item>)
        fun showRenameDialog(current: String?)
        fun finishScreen()
        fun showNotificationPermissionView()
        fun showUndoDeletion(undoAction: Runnable)
    }

}