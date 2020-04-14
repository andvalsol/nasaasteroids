package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.OnAsteroidClick
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.Result
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid

class MainFragment : Fragment(), OnAsteroidClick {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)

        val adapter = AsteroidsAdapter(this@MainFragment)

        with(binding) {
            lifecycleOwner = this@MainFragment
            viewModel = this@MainFragment.viewModel
            pictureOfDay = null

            asteroidRecycler.apply {
                this.adapter = adapter
                this.setHasFixedSize(true)
            }
        }

        viewModel.asteroidsLiveData.observe(viewLifecycleOwner, Observer { asteroids ->
            adapter.submitList(asteroids)
        })

        viewModel.pictureOfDayLiveData.observe(viewLifecycleOwner, Observer {
            if (it is Result.Success) binding.pictureOfDay = it.value
            else if (it is Result.Error)
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.error_getting_main_image),
                        Toast.LENGTH_LONG
                    )
                    .show()
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun onAsteroidClick(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }
}
