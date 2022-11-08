package com.dicoding.submissionintermediatedicoding

import android.content.Intent
import android.widget.RemoteViewsService
import com.dicoding.submissionintermediatedicoding.ui.stackwidget.StackRemoteViewsFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}