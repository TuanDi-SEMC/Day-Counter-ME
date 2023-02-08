package com.diagonalley.daycounterme.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Base64.encodeToString
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.diagonalley.daycounterme.BuildConfig
import com.diagonalley.daycounterme.R
import org.greenrobot.eventbus.EventBus
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


/**
 * -----------------Eventbus-----------------
 * */
inline fun <reified T> postEvent(t: T) {
    EventBus.getDefault().post(t)
}

inline fun <reified T> postStickyEvent(t: T) {
    EventBus.getDefault().postSticky(t)
}

fun <T> getStickyEvent(clazz: Class<T>): T? {
    return EventBus.getDefault().getStickyEvent(clazz)
}

/**register after supper onCreate
 *override fun onCreate(savedInstanceState: Bundle?) {
 *super.onCreate(savedInstanceState)
 *registerEventBus()
 *}
 * */
fun Any.registerEventBus() {
    if (!EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().register(this)
    }
}

/**
 * unregister before supper onDestroy
override fun onDestroy() {
super.onDestroy()
unregisterEventBus()
}
 */
fun Any.unregisterEventBus() {
    if (EventBus.getDefault().isRegistered(this)) {
        EventBus.getDefault().unregister(this)
    }
}

fun Context.sendMail(to: String, subject: String = "") {
    val mailTo = "mailto:" + to +
            "?&subject=" + Uri.encode(subject)
    val emailIntent = Intent(Intent.ACTION_VIEW)
    emailIntent.data = Uri.parse(mailTo)
    try {
        if (emailIntent.resolveActivity(packageManager) != null) {
            startActivity(emailIntent)
        }
    } catch (e: ActivityNotFoundException) {

    }
}

fun Fragment.shareApp() {
    val sendIntent = Intent()
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(
        Intent.EXTRA_TEXT,
        "Try this funny app: https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    )
    sendIntent.type = "text/plain"
    startActivity(sendIntent)
}

fun Int.toThemeText(): Int {
    return when (this) {
        AppCompatDelegate.MODE_NIGHT_NO -> {
            R.string.light
        }
        AppCompatDelegate.MODE_NIGHT_YES -> {
            R.string.dark
        }
        else -> {
            R.string.auto
        }
    }
}

fun Context.openGoogleStore() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Context.createPhotoTakenUri(): Uri {
    val file = createFile()
    return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
}

fun Context.createFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "IMG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ?: throw IllegalStateException("Dir not found")
    return File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",  /* suffix */
        storageDir /* directory */
    )
}

fun Context.openAppSystemSettings() {
    startActivity(Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    })
}

fun Bitmap.encodeImage(): String? {
    val bitmap = Bitmap.createScaledBitmap(this, 50, 50, false)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
    val b = baos.toByteArray()
    return encodeToString(b, Base64.DEFAULT)
}

fun Bitmap.convertToString(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}
