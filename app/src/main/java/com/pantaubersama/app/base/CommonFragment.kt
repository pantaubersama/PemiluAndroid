package com.pantaubersama.app.base

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.pantaubersama.app.R
import com.pantaubersama.app.utils.extensions.color

abstract class CommonFragment : Fragment() {

    protected var progressDialog: ProgressDialog? = null

    @LayoutRes
    protected abstract fun setLayout(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProgressDialog()
        initView(view)
    }

    protected abstract fun initView(view: View)

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity).apply {
            setMessage(getString(R.string.txt_mohon_tunggu))
        }
    }

    protected fun setupToolbar(isBackButtonEnable: Boolean, title: String, @ColorRes color: Int, elevation: Float) {
        view?.findViewById<Toolbar>(R.id.toolbar)?.let {
            it.setNavigationOnClickListener { requireActivity().onBackPressed() }
            it.title = title
            it.elevation = elevation
            it.setBackgroundColor(color(color))
            (activity as AppCompatActivity).setSupportActionBar(it)

            (activity as AppCompatActivity).supportActionBar?.run {
                setDisplayHomeAsUpEnabled(isBackButtonEnable)
            }
        }
    }
}