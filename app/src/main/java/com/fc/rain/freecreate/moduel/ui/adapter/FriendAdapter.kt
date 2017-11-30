package com.fc.rain.freecreate.moduel.ui.adapter

import android.content.Context
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.moduel.model.bean.FriendBean
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.qiongliao.qiongliaomerchant.base.BaseRainRVAdapter
import com.qiongliao.qiongliaomerchant.base.BaseRvViewHolder

/**
 *
 * Describe #
 *
 * Created by Rain on 2017/11/29.
 */

class FriendAdapter(var self: Context?, list: MutableList<MyUser>?) : BaseRainRVAdapter<MyUser>(self, list) {
    override fun getLayoutViewId(viewType: Int): Int {
        return R.layout.adapter_friend
    }

    override fun convertData(h: BaseRvViewHolder, entity: MyUser, position: Int) {
        h.getTextView(R.id.tv_name)?.text = entity.username
    }

}