package com.fc.rain.freecreate.moduel.ui.activity

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.bmob.v3.BmobUser
import kotlinx.android.synthetic.main.activity_main.*
import com.fc.rain.freecreate.R
import com.fc.rain.freecreate.base.BaseActivity
import com.fc.rain.freecreate.moduel.contract.LoginContract
import com.fc.rain.freecreate.moduel.model.bean.MyUser
import com.fc.rain.freecreate.moduel.presenter.LoginPresenter
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yanzhenjie.permission.PermissionListener

class LoginActivity : BaseActivity(), LoginContract.ILoginView {

    override fun registerSuccess() {
        et_register_name.text = null
        et_register_password.text = null
        et_register_again_password.text = null
        et_register_email.text = null
        tv_title.text = getString(R.string.login_en)
        ll_login.visibility = View.VISIBLE
        ll_register.visibility = View.GONE
        openAnimation()
        //清除本地数据
        BmobUser.logOut()
    }

    companion object {
        //权限申请code
        var REQUEST_CONTACTS = 100
        var REQUEST_CODE_SETTING = 200
    }

    var mPresenter: LoginContract.ILoginPresenter? = null

    override fun setPresenter(presenter: LoginContract.ILoginPresenter) {
        this.mPresenter = presenter
    }

    override fun layoutResID(): Int {
        return R.layout.activity_main
    }

    override fun initContract(savedInstanceState: Bundle?) {
        LoginPresenter(this, this)

    }

    override fun initView() {
        //判断是否登录过了
        var currentUser = BmobUser.getCurrentUser(MyUser::class.java)
        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
//        et_name.setText(SPUtils.get(this, USERNAME, "").toString())
//        et_password.setText(SPUtils.get(this, PASSWORD, "").toString())
    }

    override fun initData() {
        mPresenter?.start()
    }

    override fun initListener() {
        btn_login.setOnClickListener { login() }
        btn_register.setOnClickListener { register() }
        tv_register.setOnClickListener {
            tv_title.text = getString(R.string.register_en)
            ll_login.visibility = View.GONE
            ll_register.visibility = View.VISIBLE
            openAnimation()
        }
        tv_login.setOnClickListener {
            tv_title.text = getString(R.string.login_en)
            ll_login.visibility = View.VISIBLE
            ll_register.visibility = View.GONE
            openAnimation()
        }
    }

    /**
     * 申请权限
     */
    private fun initPermission() {
        // 在Activity：
        AndPermission.with(this)
                .requestCode(REQUEST_CONTACTS)
                .permission(Permission.STORAGE)
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
                        login()
                    }

                    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
                        // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                        if (AndPermission.hasAlwaysDeniedPermission(this@LoginActivity, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(this@LoginActivity, REQUEST_CODE_SETTING)
                                    .setTitle(R.string.permission_title_dialog)
                                    .setMessage(R.string.permission_message_failed)
                                    .setPositiveButton(R.string.permission_ok)
                                    .setNegativeButton(R.string.permission_no, null)
                                    .show()
                            // 更多自定dialog，请看上面。
                        }
                    }

                })
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale { _, rationale ->
                    // 这里的对话框可以自定义，只要调用rationale.resume()就可以继续申请。
                    AndPermission.rationaleDialog(this, rationale).show()
                }
                .start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SETTING -> {
                // 用户从设置回来
                initPermission()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter?.start()
    }

    /**
     * 登录注册切换动画
     */
    private fun openAnimation() {
        var anim = ObjectAnimator.ofFloat(card_view, "rotationY", 0f, 360f)
                .setDuration(1000)
        anim.start()
    }

    override fun openDefaultHXListener(): Int {
        return CUSTOMHXLISTENER
    }

    /**
     * 登录
     */
    private fun login() {
        mPresenter?.login(et_name.text.toString(), et_password.text.toString())
    }

    /**
     * 注册
     */
    private fun register() {
        mPresenter?.register(et_register_name.text.toString(), et_register_password.text.toString(), et_register_again_password.text.toString(), et_register_email.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.destroyView()
    }
}
