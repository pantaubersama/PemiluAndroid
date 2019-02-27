package com.pantaubersama.app.ui.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import com.pantaubersama.app.R
import com.pantaubersama.app.ui.note.presiden.CarouselItemFragment
import com.pantaubersama.app.ui.note.presiden.CarouselPagerAdapter
import kotlinx.android.synthetic.main.activity_catatan_pilihan_revised.*

class CatatanPilihanActivityRevised : AppCompatActivity() {
    lateinit var presidentAdapter: CarouselPagerAdapter

    var FIRST_PAGE = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catatan_pilihan_revised)

        // set page margin between pages for viewpager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val pageMargin = metrics.widthPixels / 3
        myviewpager.pageMargin = -pageMargin

        val imageArray = intArrayOf(R.drawable.ava_calon_1, R.drawable.ava_calon_2, R.drawable.img_dummy_paslon_1)

        presidentAdapter = object : CarouselPagerAdapter(supportFragmentManager, myviewpager, imageArray.size) {
            override fun getItemFragment(position: Int, scale: Float): CarouselItemFragment {
                return CarouselItemFragment.newInstance(imageArray[position], scale)
            }
        }
        myviewpager.adapter = presidentAdapter
        presidentAdapter.notifyDataSetChanged()

        myviewpager.addOnPageChangeListener(presidentAdapter)

        // Set current item to the middle page so we can fling to both
        // directions left and right
        myviewpager.currentItem = FIRST_PAGE
        myviewpager.offscreenPageLimit = 3
    }
}
