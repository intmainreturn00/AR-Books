package com.intmainreturn00.bookar.presentation.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.intmainreturn00.bookar.*
import com.intmainreturn00.bookar.platform.ScopedFragment
import com.intmainreturn00.bookar.presentation.PodkovaFont
import com.intmainreturn00.bookar.presentation.setCustomFont
import com.intmainreturn00.bookar.presentation.viewmodels.BooksViewModel
import com.intmainreturn00.grapi.grapi
import kotlinx.android.synthetic.main.fragment_loading.*
import kotlinx.coroutines.launch

class LoadingFragment : ScopedFragment() {

    private val model by activityViewModels<BooksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading.setCustomFont(PodkovaFont.REGULAR)

        launch {
            if (grapi.isLoggedIn()) {
                prefetch()
            } else if (activity?.intent?.data != null) {
                grapi.loginEnd(activity?.intent!!) { ok ->
                    if (ok) {
                        prefetch()
                    }
                }
            }
        }

        model.hasProfileCache.observe(this, Observer {cached ->
            if (cached)
                findNavController().navigate(R.id.action_loading_to_shelves)
        })

    }

    private fun prefetch() = model.loadProfile()

}
