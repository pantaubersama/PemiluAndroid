package com.pantaubersama.app.ui.menguji.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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

    val isCollapsed: Boolean
        get() = label_create.visibility != View.VISIBLE

    private var isAnimationRunning: Boolean = false

    private val fabAndLabelPairs = listOf(
        null to label_create,
        fab_live to label_live,
        fab_coming to label_coming,
        fab_done to label_done,
        fab_challenge to label_challenge
    )

    fun toggle(): Disposable? {
        return if (isCollapsed) expand() else collapse()
    }

    fun expand(): Disposable? {
        if (isAnimationRunning) return null

        fab_create.setImageResource(R.drawable.ic_create)

        return runAnimationSequence(fabAndLabelPairs, false)
    }

    fun collapse(): Disposable? {
        if (isAnimationRunning) return null

        fab_create.setImageResource(R.drawable.ic_add_menguji)

        return runAnimationSequence(fabAndLabelPairs.reversed(), true)
    }

    private fun runAnimationSequence(list: List<Pair<FloatingActionButton?, TextView>>, isCollapsing: Boolean): Disposable {
        return Observable.fromIterable(list)
            .doOnSubscribe {
                isAnimationRunning = true
                animateOverlay(isCollapsing)
            }
            .zipWith(Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)) { pair, _ -> pair }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (fab, label) ->
                if (isCollapsing) {
                    // fade out the label first then hide the FAB
                    showOrHideLabel(label, true, fab)
                } else {
                    if (fab != null) {
                        // show the FAB first then fade in the label
                        showFab(fab, label)
                    } else {
                        // we don't animate the main FAB, just the label
                        showOrHideLabel(label, false, null)
                    }
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

    private fun showFab(fab: FloatingActionButton, label: TextView) {
        fab.addOnShowAnimationListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                showOrHideLabel(label, false, null)
                fab.removeOnHideAnimationListener(this)
            }
        })
        fab.show()
    }

    // isCollapsing means the view is going to hide, otherwise it's going to show
    private fun showOrHideLabel(view: TextView, isCollapsing: Boolean, fab: FloatingActionButton?) {
        val xDelta = view.width.toFloat() * 0.6f

        view.visibility = View.VISIBLE
        view.alpha = if (isCollapsing) 1f else 0f
        view.translationX = if (isCollapsing) 0f else xDelta
        view.animate()
            .alpha(if (isCollapsing) 0f else 1f)
            .translationX(if (isCollapsing) xDelta else 0f)
            .setDuration(LABEL_FADE_DURATION)
            .setStartDelay(if (isCollapsing) 0 else LABEL_FADE_IN_DELAY)
            .apply {
                if (isCollapsing) {
                    // start hiding fab in the middle of animation progress
                    var isHiding = false
                    setUpdateListener {
                        if (!isHiding && it.animatedFraction > 0.6f) {
                            fab?.hide()
                            isHiding = true
                        }
                    }
                }
            }
            .withEndAction {
                if (isCollapsing) view.visibility = View.INVISIBLE
                checkAnimationDone(view, isCollapsing)
            }
            .start()
    }

    private fun checkAnimationDone(view: TextView, isCollapsing: Boolean) {
        val collapseFinished = isCollapsing && view == label_create
        val expandFinished = !isCollapsing && view == label_challenge

        if (collapseFinished || expandFinished) {
            isAnimationRunning = false
        }
    }

    companion object {
        private const val INTERVAL = 60L
        private const val LABEL_FADE_DURATION = 150L
        private const val LABEL_FADE_IN_DELAY = 150L
        private const val DIMMING_DURATION = 300L
    }
}