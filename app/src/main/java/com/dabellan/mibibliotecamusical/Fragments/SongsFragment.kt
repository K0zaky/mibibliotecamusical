package com.dabellan.mibibliotecamusical.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dabellan.mibibliotecamusical.Adapters.FindListAdapter
import com.dabellan.mibibliotecamusical.Constants.Constants
import com.dabellan.mibibliotecamusical.Entities.Cancion
import com.dabellan.mibibliotecamusical.Entities.Playlist
import com.dabellan.mibibliotecamusical.OnClickListener
import com.dabellan.mibibliotecamusical.Services.CancionPlaylistService
import com.dabellan.mibibliotecamusical.databinding.FragmentSongsBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SongsFragment : Fragment(), OnClickListener {
    private lateinit var mBinding: FragmentSongsBinding
    private lateinit var mSongsListAdapter: FindListAdapter
    private lateinit var mLinearLayout: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding=FragmentSongsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // llamas a setupRecyclerView()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        // despues del setup llamas a la funcion de corrutina que te carga los datos
        // en recycler

        mSongsListAdapter = FindListAdapter(this@SongsFragment)
        mLinearLayout = LinearLayoutManager(requireContext())

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLinearLayout
            adapter = mSongsListAdapter
        }
        val value = arguments?.getString("idPlaylist")
        getCanciones(value!!.toLong())

    }

    private fun getCanciones(id: Long){
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CancionPlaylistService::class.java)

        lifecycleScope.launch {
            try {
                val result = service.getCancionesPlaylist(id)
                val canciones = result.body()!!
                mSongsListAdapter.submitList(canciones)

            } catch (e: Exception) {

                (e as? HttpException)?.let {
                    when(it!!.code()) {
                        400 -> {
                            //updateUI(getString(R.string.main_error_server))
                            //Toast.makeText(this@FindFragment,"Error", Toast.LENGTH_SHORT)
                        }
                        //else ->
                        //updateUI(getString(R.string.main_error_response))
                        //Toast.makeText(this@FindFragment,"Error", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }




    override fun onClickCancion(cancionEntity: Cancion) {
        TODO("Not yet implemented")
    }

    override fun onClickPlaylist(playlistEntity: Playlist) {
        TODO("Not yet implemented")
        /*val value = arguments?.getString("idPlaylist")
        getCanciones(value!!.toLong())*/
    }


}