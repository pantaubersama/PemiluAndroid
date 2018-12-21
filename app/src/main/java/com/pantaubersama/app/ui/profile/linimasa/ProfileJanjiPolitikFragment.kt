package com.pantaubersama.app.ui.profile.linimasa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pantaubersama.app.R

class ProfileJanjiPolitikFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_janji_politik, container, false)
    }

    companion object {
        fun newInstance(): ProfileJanjiPolitikFragment {
            return ProfileJanjiPolitikFragment()
        }
    }
}