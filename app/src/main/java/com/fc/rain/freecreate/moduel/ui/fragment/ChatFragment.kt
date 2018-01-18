package com.fc.rain.freecreate.moduel.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.view.View
import android.widget.BaseAdapter
import com.hyphenate.chat.EMMessage
import com.hyphenate.easeui.ui.EaseChatFragment
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter
import com.hyphenate.util.PathUtil
import java.io.File
import java.io.FileOutputStream

/**
 *
 * Describe #
 *
 * Created by Rain on 2018/1/18.
 */
class ChatFragment:EaseChatFragment(), EaseChatFragment.EaseChatFragmentHelper {

    companion object {
        private val REQUEST_CODE_SELECT_VIDEO = 11
    }

    override fun onSetMessageAttributes(message: EMMessage?) {
    }

    override fun onEnterToChatDetails() {
    }

    override fun onAvatarClick(username: String?) {
    }

    override fun onAvatarLongClick(username: String?) {
    }

    override fun onMessageBubbleClick(message: EMMessage?): Boolean {
        return false
    }

    override fun onMessageBubbleLongClick(message: EMMessage?) {
    }

    override fun onExtendMenuItemClick(itemId: Int, view: View?): Boolean {
        return false
    }

    override fun onSetCustomChatRowProvider(): EaseCustomChatRowProvider {
        return  MyEaseCustomChatRowProvider()
    }

    inner class MyEaseCustomChatRowProvider : EaseCustomChatRowProvider {

        override fun getCustomChatRowTypeCount(): Int {
            return 0
        }

        override fun getCustomChatRowType(message: EMMessage?): Int {
            if (message?.type == EMMessage.Type.LOCATION) {
                if (message?.direct() == EMMessage.Direct.RECEIVE) {
//                    return LOACTION_REC
                } else {
//                    return LOACTION_SEND
                }
            }
            return 0
        }

        override fun getCustomChatRow(message: EMMessage?, position: Int, adapter: BaseAdapter?): EaseChatRowPresenter? {
            return null
        }
    }

    override fun setUpView() {
        setChatFragmentHelper(this)
        super.setUpView()
        titleBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SELECT_VIDEO -> {
                    if (data != null) {
                        val duration = data.getIntExtra("dur", 0)
                        val videoPath = data.getStringExtra("path")
                        val file = File(PathUtil.getInstance().imagePath, "thvideo" + System.currentTimeMillis())
                        try {
                            val fos = FileOutputStream(file)
                            val ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3)
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                            fos.close()
                            sendVideoMessage(videoPath, file.absolutePath, duration)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                }
                else -> {
                }
            }
        }

    }
}