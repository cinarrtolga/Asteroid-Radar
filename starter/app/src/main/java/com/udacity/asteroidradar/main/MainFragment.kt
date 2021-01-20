package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var adapter: AsteroidAdapter
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        viewModelFactory = MainViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        val layoutManager = GridLayoutManager(requireContext(), 1)
        binding.asteroidRecycler.layoutManager = layoutManager

        viewModel.imageOfDay.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Picasso.with(requireContext()).load(it.url).into(binding.activityMainImageOfTheDay)
                binding.activityMainImageOfTheDay.visibility = View.VISIBLE
            }
        })

        viewModel.asteroidList.observe(viewLifecycleOwner, Observer { it ->
            if (it != null) {
                adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener { asteroid ->
                    viewModel.currentAsteroid.value = asteroid
                })

                binding.asteroidRecycler.adapter = adapter
                adapter.submitList(viewModel.filterAsteroids(AsteroidFilter.ALL))
            }
        })

        viewModel.currentAsteroid.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(viewModel.currentAsteroid.value!!))
                viewModel.navigationCompleted()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view_today_asteroids -> adapter.submitList(viewModel.filterAsteroids(AsteroidFilter.DAILY))
            R.id.view_week_asteroids -> adapter.submitList(viewModel.filterAsteroids(AsteroidFilter.WEEKLY))
            else -> adapter.submitList(viewModel.filterAsteroids(AsteroidFilter.ALL))
        }

        return true
    }
}
