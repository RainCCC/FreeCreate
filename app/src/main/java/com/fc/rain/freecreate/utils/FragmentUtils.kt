package com.fc.rain.freecreate.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Created by Rain on 2017/11/23.
 */
class FragmentUtils {
    companion object {
        fun show(supporMafragment: FragmentManager, fragment: Fragment?) {
            supporMafragment.beginTransaction()
                    .show(fragment)
                    .commit()
        }

        fun hide(supporMafragment: FragmentManager, fragment: Fragment?) {
            supporMafragment.beginTransaction()
                    .hide(fragment)
                    .commit()
        }

        fun hideAll(supporMafragment: FragmentManager, fragments: MutableList<Fragment>?) {
            fragments?.forEach { supporMafragment.beginTransaction().hide(it).commit() }
        }

        fun showHideOher(supporMafragment: FragmentManager, fragments: MutableList<Fragment>?, fragment: Fragment?) {
            hideAll(supporMafragment, fragments)
            supporMafragment.beginTransaction().show(fragment).commit()
        }

        fun add(supporMafragment: FragmentManager, id: Int, fragment: Fragment?) {
            supporMafragment.beginTransaction()
                    .add(id, fragment)
                    .commit()
        }

        fun add(supporMafragment: FragmentManager, id: Int, fragment: Fragment?, tag: String) {
            supporMafragment.beginTransaction()
                    .add(id, fragment, tag)
                    .commit()
        }
        fun addAndHide(supporMafragment: FragmentManager, id: Int, fragment: Fragment?, tag: String) {
            supporMafragment.beginTransaction()
                    .add(id, fragment, tag)
                    .hide(fragment)
                    .commit()
        }

        fun replace(supporMafragment: FragmentManager, id: Int, fragment: Fragment?) {
            supporMafragment.beginTransaction()
                    .replace(id, fragment)
                    .commit()
        }

        fun replace(supporMafragment: FragmentManager, id: Int, fragment: Fragment?, tag: String) {
            supporMafragment.beginTransaction()
                    .replace(id, fragment, tag)
                    .commit()
        }

        fun findFragmentByTag(supporMafragment: FragmentManager, tag: String): Fragment? {
            return supporMafragment.findFragmentByTag(tag)
        }
    }
}