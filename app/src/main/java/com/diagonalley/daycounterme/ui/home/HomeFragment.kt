package com.diagonalley.daycounterme.ui.home

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.diagonalley.daycounterme.R
import com.diagonalley.daycounterme.base.BaseFragment
import com.diagonalley.daycounterme.databinding.FragmentHomeBinding
import com.diagonalley.daycounterme.global.AppConfig
import com.diagonalley.daycounterme.global.WEDDING
import com.diagonalley.daycounterme.manager.MyBiometricManager
import com.diagonalley.daycounterme.ui.adapter.WidgetAdapter
import com.diagonalley.daycounterme.ui.adapter.WidgetView
import com.diagonalley.daycounterme.ui.main.MainActivity
import com.diagonalley.daycounterme.ui.sheet.OnPickImageListener
import com.diagonalley.daycounterme.ui.sheet.PickImageBottomSheet
import com.diagonalley.daycounterme.ui.widget.DayAppWidget
import com.diagonalley.daycounterme.utils.encodeAsBitmap
import com.diagonalley.daycounterme.utils.load
import com.diagonalley.daycounterme.utils.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    @Inject
    lateinit var appConfig: AppConfig

    @Inject
    lateinit var myBiometricManager: MyBiometricManager
    private val viewModel: HomeViewModel by viewModels()

    private val widgetAdapter by lazy {
        WidgetAdapter {
            val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
//            pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetID)
            startActivityForResult(pickIntent, 100)
//            requestToPinWidget()
        }
    }

    private fun requestToPinWidget() {
        val appWidgetManager: AppWidgetManager? =
            getSystemService(requireContext(), AppWidgetManager::class.java)
        val myProvider = ComponentName(requireContext(), DayAppWidget::class.java)
        if (appWidgetManager?.isRequestPinAppWidgetSupported == true) {
            val pinnedWidgetCallbackIntent = Intent(requireContext(), MainActivity::class.java)
            val successCallback = PendingIntent.getBroadcast(
                requireContext(), 0, pinnedWidgetCallbackIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
        }
    }

    override fun getLayoutResId(): Int = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
//            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return if (widgetAdapter.currentList[position].widgetType == WEDDING)
//                        1
//                    else
//                        2
//                }
//            }
            imgAvatar.load("https://i.stack.imgur.com/1nKV7.png?s=64&g=1")
            rcvWidget.apply {
                layoutManager = gridLayoutManager
                adapter = widgetAdapter
            }
            imgAvatar.setOnSingleClickListener {
                val url = "https://i.stack.imgur.com/1nKV7.png?s=64&g=1"
                val bitmap = url.encodeAsBitmap()
            }
            btnAdd.setOnSingleClickListener {
                findNavController().navigate(R.id.calendarFragment)
            }
        }

        val widgetData = arrayListOf<WidgetView>()
        widgetData.add(
            WidgetView(
                widgetType = WEDDING,
                url = "https://3.img-dpreview.com/files/p/E~TS590x0~articles/8692662059/8283897908.jpeg"
            )
        )
        widgetData.add(
            WidgetView(
                widgetType = WEDDING,
                url = "https://3.img-dpreview.com/files/p/E~TS590x0~articles/8692662059/8283897908.jpeg"
            )
        )
//        widgetData.add(
//            WidgetView(
//                widgetType = BIRTHDAY,
//                url = "https://3.img-dpreview.com/files/p/E~TS590x0~articles/8692662059/8283897908.jpeg"
//            )
//        )
        widgetAdapter.submitList(widgetData)
    }

    private fun toggleMenu() {
        val sheet = PickImageBottomSheet.newInstance()
        sheet.listener = object : OnPickImageListener {
            override fun takePhoto() {

            }

            override fun openGallery() {
            }
        }
        sheet.showNow(childFragmentManager, PickImageBottomSheet.TAG)
    }
}

