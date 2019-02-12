package com.pantaubersama.app.ui.menguji.home

import android.view.View
import com.pantaubersama.app.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.zipWith
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_menguji_fabs.*
import java.util.concurrent.TimeUnit

class FabAnimationDelegate(override val containerView: View, private val overlay: View) : LayoutContainer {

    private val isCollapsed: Boolean
        get() = label_create.visibility == View.GONE

    private val fabAndLabelPairs = listOf(
        null to label_create,
        fab_live to label_live,
        fab_coming to label_coming,
        fab_done to label_done,
        fab_challenge to label_challenge
    )

    fun toggle(): Disposable {
        return if (isCollapsed) expand() else collapse()
    }

    fun expand(): Disposable {
        animateView(overlay, false, FADE_DURATION)
        fab_create.setImageResource(R.drawable.ic_create)

        return Observable.fromIterable(fabAndLabelPairs)
            .zipWith(Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)) { pair, _ -> pair }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (fab, label) ->
                fab?.show()
                animateView(label, false)
            }
    }

    fun collapse(): Disposable {
        animateView(overlay, true, FADE_DURATION)
        fab_create.setImageResource(R.drawable.ic_add_menguji)

        return Observable.fromIterable(fabAndLabelPairs.reversed())
            .zipWith(Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)) { pair, _ -> pair }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (fab, label) ->
                fab?.hide()
                animateView(label, true)
            }
    }

    private fun animateView(view: View, isCollapsing: Boolean, duration: Long? = null) {
        view.alpha = if (isCollapsing) 1f else 0f
        view.animate()
            .alpha(if (isCollapsing) 0f else 1f)
            .apply {
                duration?.let { setDuration(it) }

                if (isCollapsing) withEndAction { view.visibility = View.GONE }
                else withStartAction { view.visibility = View.VISIBLE }
            }
            .start()
    }

    companion object {
        private const val INTERVAL = 35L
        private const val FADE_DURATION = 300L
    }
}