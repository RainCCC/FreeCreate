package com.fc.rain.freecreate.moduel.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMClient
import com.hyphenate.exceptions.HyphenateException
import com.qiongliao.qiongliaomerchant.base.BaseRainRVAdapter
import com.qiongliao.qiongliaomerchant.base.BaseRvViewHolder
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.support.v4.toast


/**
 *
 * Describe # 消息
 *
 * Created by Rain on 2017/11/23.
 */
class MessageFragment : BaseFragment() {
    var rv: RecyclerView? = null
    var friendList: MutableList<String>? = null
    var messageAdapter: MessageAdapter? = null
    override fun initView() {
        rv = findViewById<RecyclerView>(R.id.rv_message) as RecyclerView?
        rv?.layoutManager = LinearLayoutManager(mContext)
        messageAdapter = MessageAdapter(mContext, friendList)
        rv?.adapter = messageAdapter
    }

    override fun initContract() {
    }

    override fun initData() {
        async {
            friendList?.clear()
            friendList = EMClient.getInstance().contactManager().allContactsFromServer
            mContext.runOnUiThread { messageAdapter?.upData(friendList) }

        }
    }

    override fun initListener() {
        EMClient.getInstance().contactManager().setContactListener(object : EMContactListener {
            override fun onFriendRequestAccepted(username: String?) {
                //好友请求被同意
                mContext.runOnUiThread { toast(username + mContext.getString(R.string.already_agree_your_request)) }
            }

            override fun onFriendRequestDeclined(username: String?) {
                //好友请求被拒绝
                mContext.runOnUiThread { toast(username + mContext.getString(R.string.already_disagree_your_request)) }
            }

            override fun onContactInvited(username: String, reason: String) {
                mContext.runOnUiThread {
                    //收到好友邀请
                    var builder = AlertDialog.Builder(mContext)
                    builder.setTitle(mContext.getString(R.string.request_friend))
                    builder.setMessage(username + mContext.getString(R.string.request_add_friend))
                    builder.setPositiveButton(mContext.getString(R.string.agree), object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            try {
                                //同意添加好友
                                EMClient.getInstance().contactManager().acceptInvitation(username)
                                toast(mContext.getString(R.string.agree_request_friend))
                            } catch (e: HyphenateException) {
                                toast(e.description)
                            }
                            dialog?.dismiss()
                        }

                    })
                    builder.setNegativeButton(mContext.getString(R.string.disagree), object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            try {
                                //拒绝添加好友
                                EMClient.getInstance().contactManager().declineInvitation(username)
                                toast(mContext.getString(R.string.disagree_request_friend))
                            } catch (e: HyphenateException) {
                                toast(e.description)
                            }

                            dialog?.dismiss()
                        }

                    })
                    builder.show()
                }

            }

            override fun onContactDeleted(username: String) {
                //被删除时回调此方法
                mContext.runOnUiThread { toast(username + mContext.getString(R.string.already_delete_friend)) }
                initData()
            }


            override fun onContactAdded(username: String) {
                //增加了联系人时回调此方法
                mContext.runOnUiThread { toast(username + mContext.getString(R.string.already_add_friend)) }
                initData()
            }
        })
        messageAdapter?.setIOnClickListener(object : BaseRainRVAdapter.IOnClickListener {
            override fun onClick(view: View, position: Int) {

            }

        })
        messageAdapter?.setiOnLongClickListener(object : BaseRainRVAdapter.IOnLongClickListener {
            override fun onLongItemClick(view: View, position: Int) {
                AlertDialog.Builder(mContext)
                        .setTitle(mContext.getString(R.string.delete_care))
                        .setMessage(mContext.getString(R.string.delete_care_friend))
                        .setNegativeButton(mContext.getString(R.string.no), object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }

                        })
                        .setPositiveButton(mContext.getString(R.string.yes), object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                //删除好友
                                try {
                                    EMClient.getInstance().contactManager().deleteContact(friendList?.get(position))
                                    toast(mContext.getString(R.string.delete_success))
                                } catch (e: HyphenateException) {
                                    toast(e.description)
                                }
                            }

                        }).show()

            }

        })
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_message
}

class MessageAdapter(var self: Context?, list: MutableList<String>?) : BaseRainRVAdapter<String>(self, list) {
    override fun getLayoutViewId(viewType: Int): Int {
        return R.layout.adapter_friend
    }

    override fun convertData(h: BaseRvViewHolder, entity: String, position: Int) {
        h.getTextView(R.id.tv_friend)?.text = entity
    }

}