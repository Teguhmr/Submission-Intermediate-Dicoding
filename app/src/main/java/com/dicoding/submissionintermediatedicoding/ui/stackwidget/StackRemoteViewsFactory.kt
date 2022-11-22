package com.dicoding.submissionintermediatedicoding.ui.stackwidget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.dicoding.submissionintermediatedicoding.R
import com.dicoding.submissionintermediatedicoding.data.preferences.UserLoginPreferences


internal class StackRemoteViewsFactory(private var context: Context) :
    RemoteViewsService.RemoteViewsFactory {
    private val widgetItems = ArrayList<Bitmap>()
    private lateinit var usrLoginPref: UserLoginPreferences

    override fun onCreate() {
        usrLoginPref = UserLoginPreferences(context)
    }

    override fun onDataSetChanged() {

        for (i in usrLoginPref.getDataArrayList()){
            val imageBytes = Base64.decode(i, Base64.DEFAULT)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            widgetItems.add(image)
        }

    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.image_view, widgetItems[position])

        val extras = bundleOf(ImagesBannerStackWidget.EXTRA_ITEM to position)
        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.image_view, fillIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}