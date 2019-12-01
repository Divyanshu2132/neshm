package com.androhome.neshm.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androhome.neshm.MainActivity
import com.androhome.neshm.R
import com.androhome.neshm.adpters.NearbyProviderListAdapter
import com.androhome.neshm.adpters.genreAdapter
import com.androhome.neshm.interfaces.LoginApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList


class homeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var tutorListLinearLayoutManager: LinearLayoutManager
    private lateinit var genreList: ArrayList<String>
    private var tempState: String = ""
    private lateinit var colors: ArrayList<Int>
    private lateinit var adapter: genreAdapter
    private lateinit var tutorsListRecyclerView: RecyclerView
    private lateinit var tutor_adapter: NearbyProviderListAdapter

    val loginApiInterface by lazy {
        LoginApiInterface.create()
    }
    var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        val sharedpref: SharedPreferences =
            requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        this.tempState = sharedpref.getString("tempState", "")
        recyclerView = view.findViewById(R.id.genre_list)
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        tutorsListRecyclerView = view.findViewById(R.id.tutors_list)
        createData()
        adapter = genreAdapter(requireContext(), genreList)
        recyclerView.adapter = adapter
        tutorListLinearLayoutManager = LinearLayoutManager(requireContext())
        tutorsListRecyclerView.layoutManager = tutorListLinearLayoutManager
    }

    private fun createData() {
        genreList = ArrayList(
            Arrays.asList(
                "Tutor", "Freelancer", "Cook", "Artist", "Babysitter", "Plumber", "Coach",
                "Painter", "Writing", "Dancer", "Music"
            )
        )
        fetchData()
    }

    private fun fetchData() {
        if (MainActivity.nearbyProvidermodel == null || MainActivity.state != tempState) {
            val sharedPref = requireContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE
            ) ?: return
            with(sharedPref.edit()) {
                putString("tempState", MainActivity.state)
                apply()
            }
            disposable = loginApiInterface.nearbyProviders(MainActivity.state as String).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe { result ->
                    if (result != null) {
                        if (result.userInfo!!.size > 0 && result.userInfo!!.get(0).result != "Something Wrong" && result.userInfo!!.get(
                                0
                            ).result != "Search failed"
                        ) {
                            MainActivity.nearbyProvidermodel = result
                            tutor_adapter = NearbyProviderListAdapter(result.userInfo!!, requireContext())
                            tutorsListRecyclerView.adapter = tutor_adapter
                        }
                    }
                }
        } else {
            val result = MainActivity.nearbyProvidermodel
            tutor_adapter = NearbyProviderListAdapter(result?.userInfo!!, requireContext())
            tutorsListRecyclerView.adapter = tutor_adapter
        }
    }


}
