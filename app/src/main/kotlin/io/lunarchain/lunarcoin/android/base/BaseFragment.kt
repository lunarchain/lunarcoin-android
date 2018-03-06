package io.lunarchain.lunarcoin.android.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.lunarchain.R
import kotlinx.android.synthetic.main.fragment_base.*

abstract class BaseFragment : Fragment() {

    protected lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater!!.inflate(getRootContentViewResId(), container, false)
        if (getContentViewResId() != 0 && root is LinearLayout) {
            (root as LinearLayout).addView(
                View.inflate(context, getContentViewResId(), null),
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            )
        }
        return root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (toolbar != null) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        init(savedInstanceState)
    }

    protected open fun getRootContentViewResId(): Int = R.layout.fragment_base

    protected abstract fun getContentViewResId(): Int

    protected abstract fun init(savedInstanceState: Bundle?)

}