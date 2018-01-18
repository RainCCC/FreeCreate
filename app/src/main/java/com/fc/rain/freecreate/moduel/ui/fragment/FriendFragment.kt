package com.fc.rain.freecreate.moduel.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseFragment
import com.fc.rain.freecreate.moduel.contract.FriendContract
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.moduel.presenter.FriendPresenter
import com.fc.rain.freecreate.moduel.ui.activity.ChatActivity
import com.fc.rain.freecreate.moduel.ui.adapter.FriendAdapter
import com.qiongliao.qiongliaomerchant.base.BaseRainRVAdapter
import kotlinx.android.synthetic.main.fragment_friend.*

/**
 *
 * Describe # 好友
 *
 * Created by Rain on 2017/11/29.
 */
class FriendFragment : BaseFragment(), FriendContract.View {

    var list: MutableList<MyUser>? = arrayListOf()
    var adapter: FriendAdapter? = null
    var rv: RecyclerView? = null

    override fun refreshFriendListSuccess(friendList: MutableList<MyUser>?) {
        list?.clear()
        friendList?.let { list?.addAll(it) }
        adapter?.upData(list)
    }

    var mPresenter: FriendContract.Presenter? = null

    override fun setPresenter(presenter: FriendContract.Presenter) {
        mPresenter = presenter
    }

    companion object {
        fun newInstance(bundle: Bundle?): FriendFragment {
            var friendFragment = FriendFragment()
            bundle?.let { friendFragment.arguments = it }
            return friendFragment
        }
    }

    override fun initView() {
        rv = findViewById<RecyclerView>(R.id.rv_friend) as RecyclerView?
        adapter = FriendAdapter(mContext, list)
        rv?.layoutManager = LinearLayoutManager(mContext)
        rv?.adapter = adapter
    }

    override fun initContract() {
        context?.let { FriendPresenter(it, this) }
    }

    override fun initData() {
        mPresenter?.start()
    }

    override fun initListener() {
        refresh.setOnRefreshListener { refreshlayout -> refreshlayout.finishRefresh(2000) }
        refresh.setOnLoadmoreListener { refreshlayout -> refreshlayout.finishLoadmore(2000) }
        adapter?.setIOnClickListener(object : BaseRainRVAdapter.IOnClickListener {
            override fun onClick(view: View, position: Int) {
                mContext?.let { list?.get(position)?.username?.let { it1 -> ChatActivity.startActivity(it, it1) } }
            }

        })
    }

    override fun lazyLoadData() {
    }

    override val layoutResID: Int
        get() = R.layout.fragment_friend
}