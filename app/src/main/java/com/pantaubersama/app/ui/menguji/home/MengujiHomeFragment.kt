package com.pantaubersama.app.ui.menguji.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.home.HomeActivity
import com.pantaubersama.app.ui.home.HomeFragment
import com.pantaubersama.app.ui.menguji.list.DebatListActivity
import com.pantaubersama.app.ui.wordstadium.challenge.CreateChallengeActivity
import com.pantaubersama.app.utils.PantauConstants.Debat.Title
import com.pantaubersama.app.utils.extensions.color
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_menguji.*
import kotlinx.android.synthetic.main.layout_menguji_fabs.*

class MengujiHomeFragment : HomeFragment() {

    companion object {
        val TAG: String = MengujiHomeFragment::class.java.simpleName

        fun newInstance(): MengujiHomeFragment {
            return MengujiHomeFragment()
        }
    }

    override val viewPager: ViewPager
        get() = view_pager

    override val pagerFragments: List<Pair<Fragment, String>>
        get() = listOf(
            MengujiPagerFragment.newInstance(true) to getString(R.string.publik_label),
            MengujiPagerFragment.newInstance(false) to getString(R.string.personal_label))

    private val isPublik: Boolean
        get() = currentTabPosition == 0

    private lateinit var fabAnimationDelegate: FabAnimationDelegate
    private var animationDisposable: Disposable? = null

    override fun setLayout(): Int = R.layout.fragment_menguji

    override fun initView(view: View, savedInstanceState: Bundle?) {
        super.initView(view, savedInstanceState)
        changeTopBarColor(isHidden)

        setupFab()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        changeTopBarColor(hidden)
    }

    private fun setupFab() {
        fabAnimationDelegate = FabAnimationDelegate(fab_container)

        val mainFabListener = View.OnClickListener {
            if (!fabAnimationDelegate.isCollapsed) {
                startActivity(Intent(requireContext(), CreateChallengeActivity::class.java))
            } else {
                setupFabButtons()
            }
            animationDisposable = fabAnimationDelegate.toggle()
        }

        val onClickListener = View.OnClickListener { view ->
            val (publikTitle, personalTitle) = when (view) {
                fab_challenge, label_challenge -> Title.PUBLIK_CHALLENGE to Title.PERSONAL_CHALLENGE
                fab_done, label_done -> Title.PUBLIK_DONE to Title.PERSONAL_DONE
                fab_coming, label_coming -> Title.PUBLIK_COMING_SOON to Title.PERSONAL_COMING_SOON
                fab_live, label_live -> Title.PUBLIK_LIVE_NOW to Title.PERSONAL_CHALLENGE_IN_PROGRESS
                else -> "" to ""
            }
            DebatListActivity.start(requireContext(), if (isPublik) publikTitle else personalTitle)
            animationDisposable = fabAnimationDelegate.toggle()
        }

        overlay.setOnClickListener { animationDisposable = fabAnimationDelegate.collapse() }

        fab_create.setOnClickListener(mainFabListener)
        fab_challenge.setOnClickListener(onClickListener)
        fab_done.setOnClickListener(onClickListener)
        fab_coming.setOnClickListener(onClickListener)
        fab_live.setOnClickListener(onClickListener)

        label_create.setOnClickListener(mainFabListener)
        label_challenge.setOnClickListener(onClickListener)
        label_done.setOnClickListener(onClickListener)
        label_coming.setOnClickListener(onClickListener)
        label_live.setOnClickListener(onClickListener)
    }

    private fun setupFabButtons() {
        label_challenge.text = if (isPublik) "Challenge" else "My Challenge"
        label_done.text = if (isPublik) "Debat Done" else "My Debat Done"
        label_live.text = if (isPublik) "Live Now" else "Challenge in Progress"
        fab_live.setImageResource(if (isPublik) R.drawable.ic_debat_live else R.drawable.ic_debat_open)
    }

    private fun changeTopBarColor(hidden: Boolean) {
        val (appbarColor, statusBarColor) = if (hidden)
            color(R.color.colorPrimary) to color(R.color.colorPrimaryDark)
        else
            color(R.color.yellow) to color(R.color.yellow_dark)
        (requireActivity() as HomeActivity).changeTopBarColor(appbarColor, statusBarColor)
    }

    override fun onDestroy() {
        animationDisposable?.dispose()
        super.onDestroy()
    }
}