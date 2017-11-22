package com.qiongliao.qiongliaomerchant.base

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList

/**
 * 支持添加HeaderView,FootView,EmptyView,上拉加载更多View,
 *
 *
 * RecyclerView 万能适配器
 *
 *
 * Created by Rain on 2017/9/28.
 */

abstract class BaseRainRVAdapter<T>(var mContext: Context?, list: MutableList<T>?) : RecyclerView.Adapter<BaseRvViewHolder>() {

    //头部的tag
    private val headerTags = ArrayList<Int>()
    //标准头部viewType值
    private val headerInt = 800000
    //底部的tag
    private val footTags = ArrayList<Int>()
    //标准头部viewType值
    private val footInt = 900000

    //没有数据时的View
    private var emptyView: View? = null

    //上拉加载更多的View;
    private var loadMoreView: View? = null

    //是否需要上拉加载更多
    private var endLoadMore = false

    /**
     * 数据源
     */
    private var list: MutableList<T>? = ArrayList()

    /**
     * 头部的View集合
     */
    private val headViews = ArrayList<View>()

    /**
     * 底部的View集合
     */
    private val footViews = ArrayList<View>()

    //当前的位置
    private var mCurrentPosition = -1

    //handler处理延时操作
    private val handler = Handler()
    private var openEmptyView = false

    /**
     * 总View个数
     *
     * @return
     */
    val totalItemCount: Int
        get() = headViews.size + list!!.size + footViews.size + loadMoreViewCount

    /**
     * 上拉加载更多View的个数
     *
     * @return
     */
    private val loadMoreViewCount: Int
        get() = if (hasLoadMore()) 1 else 0

    /**
     * 头部和list数据的个数
     *
     * @return
     */
    val headAndDataCount: Int
        get() = headViews.size + list!!.size

    /**
     * 获取头部试图的个数
     *
     * @return
     */
    val headerCounts: Int
        get() = headViews.size

    //是否为空
    private val isEmpty: Boolean
        get() = if (emptyView != null && openEmptyView) {
            if (list != null) {
                list!!.size == 0
            } else {
                true
            }
        } else {
            false
        }

    /**
     * 需要监听的子View的数组
     */
    private var childId: IntArray? = null

    private var isListenerChildLongClick = false


    private var iOnClickListener: IOnClickListener? = null

    private var iOnLongClickListener: IOnLongClickListener? = null

    private var iOnChildClickListener: IOnChildClickListener? = null

    private var iOnChildLongClickListener: IOnChildLongClickListener? = null

    //监听加载更多的事件
    private var iLoadMoreListener: ILoadMoreListener? = null

    /**
     * 没有数据时显示空view
     *
     * @param openEmptyView 是否开启空view
     */
    fun setOpenEmptyView(openEmptyView: Boolean) {
        this.openEmptyView = openEmptyView
    }

    /**
     * 没有数据时显示空view
     *
     * @param openEmeptyView 是否开启空view
     * @param openNotify     是否更新
     */
    fun setOpenEmptyView(openEmptyView: Boolean, openNotify: Boolean) {
        this.openEmptyView = openEmptyView
        if (openNotify) {
            notifyDataSetChanged()
        }
    }

    init {
        if (list != null) {
            this.list = list
        }
    }

    /**
     * 添加头部的试图
     *
     * @param view
     * @param isNotify 是否通知插入一个条目
     */
    @JvmOverloads
    fun addHeaderView(view: View?, isNotify: Boolean = true) {
        if (view == null) {
            throw NullPointerException("the header view can not be null")
        }
        val position: Int
        if (headerCounts == 0) {
            position = headerCounts
        } else {
            position = headerCounts
        }
        headViews.add(view)
        if (isNotify) {
        }
    }

    /**
     * 删除一个试图
     *
     * @param position
     */
    @JvmOverloads
    fun removeHeaderView(position: Int, isNotify: Boolean = false) {
        headViews.removeAt(position)
        headerTags.removeAt(position)
        if (isNotify) {
            notifyDataSetChanged()
        }
    }

    /**
     * 添加底部的试图
     *
     * @param view
     * @param isNotify 是否通知插入一个条目
     */
    @JvmOverloads
    fun addFootView(view: View?, isNotify: Boolean = false) {
        if (view == null) {
            throw NullPointerException("the header view can not be null")
        }
        footViews.add(view)
        if (isNotify) {
            notifyDataSetChanged()
        }
    }

    /**
     * 删除一个试图
     *
     * @param position
     */
    @JvmOverloads
    fun removeFootView(position: Int, isNotify: Boolean = true) {
        footViews.removeAt(position)
        footTags.removeAt(position)
        if (isNotify) {
            notifyItemRemoved(position)
        }
    }

    /**
     * 设置空View
     *
     * @param view
     */
    fun setEmptyView(view: View?) {
        if (view != null)
            this.emptyView = view
    }

    /**
     * 设置上拉加载View
     *
     * @param view
     */
    fun setLoadMoreView(view: View?) {
        if (view != null)
            this.loadMoreView = view
    }

    /**
     * 当前位置是否是上拉加载
     *
     * @param position
     * @return
     */
    private fun isShowLoadMore(position: Int): Boolean {
        return hasLoadMore() && position >= itemCount - 1
    }

    /**
     * 符合上拉加载的条件
     *
     * @return
     */
    private fun hasLoadMore(): Boolean {
        return loadMoreView != null && endLoadMore
    }

    override fun getItemCount(): Int {
        return if (isEmpty) {
            1
        } else totalItemCount
    }

    override fun getItemViewType(position: Int): Int {
        mCurrentPosition = position
        //如果没有数据
        if (isEmpty) {
            return EMPTYVIEWTYPE
        }
        //如果是头部
        if (position < headViews.size) {
            headerTags.add(position + headerInt)
            return position + headerInt
        }
        //如果是底部
        if (position > headAndDataCount - 1 && position < totalItemCount - loadMoreViewCount) {
            footTags.add(position + footInt)
            return position + footInt
        }
        //如果是上拉加载
        return if (isShowLoadMore(position)) {
            LOADMORETYPE
        } else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRvViewHolder {
        var view: View? = null
        if (viewType == EMPTYVIEWTYPE) {
            view = emptyView
        } else if (headerTags.contains(viewType)) {
            view = headViews[viewType - headerInt]
        } else if (footTags.contains(viewType)) {
            view = footViews[viewType - footInt]
        } else if (viewType == LOADMORETYPE) {
            view = loadMoreView
        } else {
            view = LayoutInflater.from(mContext).inflate(getLayoutViewId(viewType), parent, false)
        }

        val viewHolder = BaseRvViewHolder(view!!)

        //视图被创建的时候调用
        viewCreated(viewHolder, viewType)

        return viewHolder
    }

    /**
     * 视图被创建的时候调用
     *
     * @param viewHolder
     * @param viewType
     */
    fun viewCreated(viewHolder: BaseRvViewHolder, viewType: Int) {

    }

    /**
     * @param viewType 返回值就是根据这个值进行判断返回的对头部不起作用
     * @return
     */
    protected abstract fun getLayoutViewId(viewType: Int): Int

    override fun onBindViewHolder(holder: BaseRvViewHolder, position: Int) {

        if (isEmpty) {
            return
        }

        if (position < headViews.size) {
            //如果是头部处理头部数据
            convertHeadViewData(holder, position)
        } else if (position > headAndDataCount - 1 && position < totalItemCount - loadMoreViewCount) {
            //如果是底部处理底部数据
            convertFootViewData(holder, position)
        } else if (isShowLoadMore(position)) {
            //如果是上拉加载
            if (iLoadMoreListener != null) {
                //防止Cannot call this method while RecyclerView is computing a layout or scrolling
                handler.postDelayed({ iLoadMoreListener!!.onILoadMoreListener() }, 800)
            }
        } else {
            //如果是数据的话
            val dataPositon = position - headViews.size
            holder.itemView.setOnClickListener { v ->
                if (iOnClickListener != null) {
                    iOnClickListener!!.onClick(v, dataPositon)
                }
            }

            holder.itemView.setOnLongClickListener { v ->
                if (iOnLongClickListener != null) {
                    iOnLongClickListener!!.onLongItemClick(v, dataPositon)
                }
                false
            }

            //监听子View
            if (childId != null && childId!!.size > 0) {
                for (i in childId!!.indices) {
                    holder.getView<View>(childId!![i])!!.setOnClickListener { v ->
                        if (iOnChildClickListener != null) {
                            iOnChildClickListener!!.onChildItemClick(v, dataPositon)
                        }
                    }
                    if (isListenerChildLongClick) {
                        holder.getView<View>(childId!![i])!!.setOnLongClickListener { v ->
                            if (iOnChildLongClickListener != null) {
                                iOnChildLongClickListener!!.onChildLongItemClick(v, dataPositon)
                            }
                            false
                        }
                    }
                }
            }

            convertData(holder, list!![dataPositon], position)
        }
    }

    /**
     * 设置动态监听子
     *
     * @param iOnChildClickListener     子View 点击监听
     * @param iOnChildLongClickListener 子View 按钮监听
     * @param listenerId                子iew的Id
     */
    fun setNeedListenerChildId(iOnChildClickListener: IOnChildClickListener, iOnChildLongClickListener: IOnChildLongClickListener, vararg listenerId: Int) {
        isListenerChildLongClick = true
        this.childId = listenerId
        this.iOnChildClickListener = iOnChildClickListener
        this.iOnChildLongClickListener = iOnChildLongClickListener
    }

    fun setNeedListenerChildId(iOnChildClickListener: IOnChildClickListener, vararg listenerId: Int) {
        this.childId = listenerId
        this.iOnChildClickListener = iOnChildClickListener
    }


    /**
     * 实现列表的显示
     *
     * @param h        RecycleView的ViewHolder
     * @param entity   实体对象
     * @param position 当前的下标
     */
    abstract fun convertData(h: BaseRvViewHolder, entity: T, position: Int)

    /**
     * 处理HeadView数据
     *
     * @param h        RecycleView的ViewHolder
     * @param position 当前的下标
     */
    fun convertHeadViewData(h: BaseRvViewHolder, position: Int) {

    }

    /**
     * 处理View数据
     *
     * @param h        RecycleView的ViewHolder
     * @param position 当前的下标
     */
    fun convertFootViewData(h: BaseRvViewHolder, position: Int) {

    }

    //监听itemView的事件接口
    interface IOnClickListener {
        fun onClick(view: View, position: Int)
    }

    fun setIOnClickListener(iOnClickListener: IOnClickListener) {
        this.iOnClickListener = iOnClickListener
    }

    //监听itemView的长按事件接口
    interface IOnLongClickListener {
        fun onLongItemClick(view: View, position: Int)
    }

    fun setiOnLongClickListener(iOnLongClickListener: IOnLongClickListener) {
        this.iOnLongClickListener = iOnLongClickListener
    }

    //监听itemView的子View的事件接口
    interface IOnChildClickListener {
        fun onChildItemClick(view: View, position: Int)
    }

    //监听itemView的子View的长按事件接口
    interface IOnChildLongClickListener {
        fun onChildLongItemClick(view: View, position: Int)
    }

    fun setiLoadMoreListener(iLoadMoreListener: ILoadMoreListener) {
        this.iLoadMoreListener = iLoadMoreListener
    }

    interface ILoadMoreListener {
        fun onILoadMoreListener()
    }

    /**
     * 更新数据
     *
     * @param list
     */
    fun upData(list: MutableList<T>?) {
        if (list != null) {
            this.list = list
            notifyDataSetChanged()
        }
    }

    /**
     * 添加数据
     *
     * @param list
     */
    fun addData(list: List<T>?) {
        if (list != null) {
            this.list!!.addAll(list)
            notifyDataSetChanged()
        }
    }

    /**
     * 移除指定item
     *
     * @param position
     */
    fun removeItem(position: Int) {
        if (this.list != null) {
            this.list!!.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    /**
     * 移除全部item
     *
     * @param
     */
    fun removeAllItem() {
        if (this.list != null) {
            for (i in this.list!!.indices) {
                this.list!!.removeAt(i)
                notifyItemRemoved(i)
            }
        }
        notifyDataSetChanged()
    }

    //关闭上拉加载未更新适配器
    fun endLoadMore() {
        this.endLoadMore = false
    }

    //打开上拉加载未更新适配器
    fun startLoadMore() {
        this.endLoadMore = true
    }

    companion object {

        //数据为空的Type
        val EMPTYVIEWTYPE = -10000

        //头部Type
        val HEADTYPE = -20000

        //底部Type
        val FOOTTYPE = -30000

        //上拉加载Type
        val LOADMORETYPE = -40000
    }

}
/**
 * 添加头部的试图,默认不通知适配器插入了一个条目
 *
 * @param view
 */
/**
 * 删除一个试图,默认不通知删除一个条目
 *
 * @param position
 */
/**
 * 添加底部的试图,默认不通知适配器插入了一个条目
 *
 * @param view
 */
/**
 * 删除一个试图,默认不通知删除一个条目
 *
 * @param position
 */
