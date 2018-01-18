package com.fc.rain.freecreate.moduel.ui.fragment

import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.ui.activity.ChatActivity
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.easeui.model.EaseAtMessageHelper
import com.hyphenate.easeui.ui.EaseConversationListFragment
import com.hyphenate.hx.HxHelper
import com.hyphenate.hx.db.InviteMessgeDao
import org.jetbrains.anko.support.v4.toast

/**
 *
 * Describe # 消息列表
 *
 * Created by Rain on 2017/11/29.
 */
class MessageListFragment : EaseConversationListFragment() {

    companion object {
        fun newInstance(bundle: Bundle?): MessageListFragment {
            var chatFragment = MessageListFragment()
            bundle?.let { chatFragment.arguments = it }
            return chatFragment
        }
    }

    override fun setUpView() {
        super.setUpView()
        registerForContextMenu(conversationListView)
        titleBar.visibility = View.GONE
        conversationListView.setOnItemClickListener { parent, view, position, id ->
            if (HxHelper.getInstance().isLoggedIn) {
                val conversation = conversationListView.getItem(position)
                val userId = conversation.conversationId()
                context?.let { ChatActivity.startActivity(it,userId) }
            } else {
                toast(getString(R.string.hx_connection_error))
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        activity!!.menuInflater.inflate(R.menu.em_delete_message, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var deleteMessage = false
        if (item.itemId == R.id.delete_message) {
            deleteMessage = true
        } else if (item.itemId == R.id.delete_conversation) {
            deleteMessage = false
        }
        val tobeDeleteCons = conversationListView.getItem((item.menuInfo as AdapterView.AdapterContextMenuInfo).position) ?: return true
        if (tobeDeleteCons.type == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId())
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage)
            val inviteMessgeDao = InviteMessgeDao(activity)
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        refresh()

        return true
    }

}