package com.pantaubersama.app.ui.menguji.home

import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
        animateOverlay(false)
        fab_create.setImageResource(R.drawable.ic_create)

        return runAnimationSequence(fabAndLabelPairs, false)
    }

    fun collapse(): Disposable {
        animateOverlay(true)
        fab_create.setImageResource(R.drawable.ic_add_menguji)

        return runAnimationSequence(fabAndLabelPairs.reversed(), true)
    }

    private fun runAnimationSequence(list: List<Pair<FloatingActionButton?, TextView>>, isCollapsing: Boolean): Disposable {
        return Observable.fromIterable(list)
            .zipWith(Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)) { pair, _ -> pair }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (fab, label) ->
                if (isCollapsing) {
                    // fade out the label first then hide the fab
                    animateLabel(label, true, fab)
                } else {
                    // show the fab first then fade in the label
                    val listener = object : FloatingActionButton.OnVisibilityChangedListener() {
                        override fun onShown(fab: FloatingActionButton) {
                            animateLabel(label, false)
                        }
                    }
                    fab?.show(listener) ?: animateLabel(label, false)
                }
            }
    }

    private fun animateOverlay(isCollapsing: Boolean) {
        overlay.alpha = if (isCollapsing) 1f else 0f
        overlay.animate()
            .alpha(if (isCollapsing) 0f else 1f)
            .setDuration(DIMMING_DURATION)
            .apply {
                if (isCollapsing) withEndAction { overlay.visibility = View.GONE }
                else withStartAction { overlay.visibility = View.VISIBLE }
            }
            .start()
    }

    private fun animateLabel(view: TextView, isCollapsing: Boolean, fab: FloatingActionButton? = null) {
        val xDelta = view.width.toFloat() / 2

        view.alpha = if (isCollapsing) 1f else 0f
        view.translationX = if (isCollapsing) 0f else xDelta
        view.animate()
            .alpha(if (isCollapsing) 0f else 1f)
            .translationX(if (isCollapsing) xDelta else 0f)
            .setDuration(LABEL_FADE_DURATION)
            .apply {
                if (isCollapsing) withEndAction {
                    view.visibility = View.GONE
                    fab?.hide()
                } else withStartAction {
                    view.visibility = View.VISIBLE
                }
            }
            .start()
    }

    companion object {
        private const val INTERVAL = 35L
        private const val LABEL_FADE_DURATION = 100L
        private const val DIMMING_DURATION = 300L
    }
}