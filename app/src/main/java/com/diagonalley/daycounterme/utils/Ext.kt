package com.diagonalley.daycounterme.utils

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.appwidget.AppWidgetManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Environment
import android.os.SystemClock
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.AppWidgetTarget
import com.diagonalley.daycounterme.BuildConfig
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.ui.widget.ACTION_REFRESH
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import org.greenrobot.eventbus.EventBus
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun View.slideUp(
) {
    val animator: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        this,
        PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 250f, 0f),
        PropertyValuesHolder.ofFloat(View.ALPHA, 0.2f, 1f)
    )
    animator.duration = 300
    animator.interpolator = AccelerateInterpolator()
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            this@slideUp.alpha = 0f
        }

        override fun onAnimationEnd(animation: Animator) {

        }

        override fun onAnimationCancel(animation: Animator) {
        }

        override fun onAnimationRepeat(animation: Animator) {
        }
    })
    animator.startDelay = (350).toLong()
    animator.start()
}

fun Context.clearAndStartActivity(activity: Class<*>) {
    val intent = Intent(this, activity).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}

fun Context.updateWidget(widgetClass: Class<*>, appWidgetId: Int) {
    val intent = Intent(this, widgetClass)
    intent.action = ACTION_REFRESH
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    sendBroadcast(intent)
}

fun Context.startActivityWithData(activity: Class<*>) {
    val intent = Intent(this, activity).apply {

    }
    startActivity(intent)
}

fun ImageView.load(url: String) {
    Glide.with(this).load(url).into(this)
}

fun AppWidgetTarget.getBitmap(context: Context, url: String) {
    val options: RequestOptions =
        RequestOptions().override(300, 300).placeholder(R.drawable.bg_app_widget)
            .error(R.drawable.bg_app_widget)
    Glide.with(context).asBitmap().load(url).apply(options).into(this)
}


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
    val mailTo = "mailto:" + to + "?&subject=" + Uri.encode(subject)
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
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun Bitmap.convertToString(): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun String.encodeAsBitmap(size: Int = 400): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(this, BarcodeFormat.QR_CODE, size, size)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val pixels = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            pixels[y * width + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }
    }

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}

fun ByteArray.toHex(): String = joinToString(separator = "") { eachByte ->
    "%02x".format(eachByte)
}

fun RecyclerView.smoothToPosition(adapter: ListAdapter<*, *>) {
    if (adapter.currentList.size > 0) this.smoothScrollToPosition(0)
}

private const val DEFAULT_CLICK_INTERVAL = 500L // in milliseconds

private var lastClickTime: Long = 0

fun View.setOnSingleClickListener(
    clickInterval: Long = DEFAULT_CLICK_INTERVAL,
    onClick: (View) -> Unit,
) {
    setOnClickListener { view ->
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastClickTime > clickInterval) {
            lastClickTime = currentTime
            onClick(view)
        }
    }
}


/**
 * Generates a QR code with an optional logo or icon in the center.
 *
 * @param text The text to encode in the QR code.
 * @param logo The logo to add to the center of the QR code, or null to generate a QR code without a logo.
 * @param width The desired width of the QR code bitmap in pixels.
 * @param height The desired height of the QR code bitmap in pixels.
 * @return A Bitmap object containing the generated QR code, or null if there was an error.
 */
fun generateQRCodeWithLogo(text: String, logo: Bitmap?, width: Int, height: Int): Bitmap? {
    val hints = Hashtable<EncodeHintType, String>()
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
    try {
        val bitMatrix = QRCodeWriter().encode("", BarcodeFormat.QR_CODE, width, height, hints)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.isAntiAlias = true
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (bitMatrix[x, y]) {
                    paint.color = 0xFF000000.toInt()
                } else {
                    paint.color = 0xFFFFFFFF.toInt()
                }
                canvas.drawRect(
                    x.toFloat(), y.toFloat(), (x + 1).toFloat(), (y + 1).toFloat(), paint
                )
            }
        }
        if (logo != null) {
            val logoSize = width.coerceAtMost(height) / 5
            val logoRect = RectF(
                (width - logoSize) / 2f,
                (height - logoSize) / 2f,
                (width + logoSize) / 2f,
                (height + logoSize) / 2f
            )
            canvas.drawBitmap(logo, null, logoRect, null)
        }
        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

