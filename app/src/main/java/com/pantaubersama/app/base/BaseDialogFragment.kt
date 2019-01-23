package com.pantaubersama.app.base

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.pantaubersama.app.di.component.ActivityComponent
import com.pantaubersama.app.di.module.ActivityModule
import com.pantaubersama.app.utils.extensions.snackBar
import timber.log.Timber

/**
 * @author edityomurti on 27/12/2018 01:03
 */
abstract class BaseDialogFragment<P : BasePresenter<*>> : DialogFragment(), BaseView {

    protected abstract var presenter: P

    @LayoutRes
    protected abstract fun setLayout(): Int

    override fun onAttach(context: Context) {
        initInjection(createActivityComponent())
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attach(this)
    }

    protected abstract fun initInjection(activityComponent: ActivityComponent)

    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun showError(throwable: Throwable) {
        activity?.window?.decorView?.findViewById<View>(android.R.id.content)?.snackBar(throwable.message!!)
        Timber.e(throwable)
    }

    private fun createActivityComponent(): ActivityComponent {
        return (requireActivity().application as BaseApp).appComponent
            .withActivityComponent(ActivityModule(requireActivity()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        return dialog
    }

    override fun onResume() {
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 0.95).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }

    protected abstract fun initView(view: View)
}