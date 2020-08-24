package com.graytsar.wlnupdates.ui.novel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.graytsar.wlnupdates.databinding.FragmentNovelBinding
import com.graytsar.wlnupdates.rest.RetrofitInterface
import com.graytsar.wlnupdates.rest.request.RequestNovel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentNovel : Fragment() {
    private lateinit var binding:FragmentNovelBinding
    val viewModelNovel by viewModels<ViewModelNovel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNovelBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl( "https://www.wlnupdates.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitInterface::class.java)

        GlobalScope.launch {
            val n = service.getNovel(RequestNovel(3, "get-series-id")).execute().body()
            Log.d("DBG", "end")
        }


        return binding.root
    }

}