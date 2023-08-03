package com.example.testapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.db.entity.WeatherData
import com.example.testapplication.utility.Constants
import com.example.testapplication.utility.NetworkResult
import com.example.testapplication.utility.isInternetAvailable
import com.example.testapplication.viewmodel.WeathersViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val weathersViewModel by viewModels<WeathersViewModel>()
    private val permissionId = 2
    private var currentlocation: Location? = null
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inIt()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun inIt() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchData() {
        fetchResponse()
        weathersViewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { res ->
                        val weatherData = WeatherData()
                        res.mainTemperature?.temperature?.let {
                            binding.tvTemp.text = "Temperature : ${it.toString()}"
                            weatherData.temperature = it.toString()
                        }
                        res.date?.let {
                            weatherData.date = Constants.convertUnixToDate(it)
                            binding.tvDate.text = "Date : ${Constants.convertUnixToDate(it)}"
                        }
                        weathersViewModel.insertDataToDb(weatherData)
                        weathersViewModel.weatherResponse.postValue(null)
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                is NetworkResult.Error -> {
                    binding.pbLoader.visibility = View.GONE
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }

        }
    }

    private fun fetchResponse() {
        currentlocation?.let {
            weathersViewModel.fetchDogResponse(
                it.latitude,
                it.longitude,
                Constants.APP_ID
            )
            binding.pbLoader.visibility = View.VISIBLE
        } ?: kotlin.run {
            Toast.makeText(
                this,
                "location not available please allow location permission",
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    location?.let {
                        currentlocation = it
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                if (isInternetAvailable(this)) {
                    fetchData()
                } else {
                    Toast.makeText(this, "Internet not available", Toast.LENGTH_SHORT)
                        .show()
                    lifecycleScope.launch {
                        val data = weathersViewModel.getWeatherData()
                        populateData(data)
                    }
                }

            }
            else -> {}
        }
        return true
    }

    @SuppressLint("SetTextI18n")
    fun populateData(weatherData: WeatherData?) {
        weatherData?.let {
            it.temperature?.let { temp ->
                binding.tvTemp.text = "Temperature : ${temp.toString()}"

            }
            it.date?.let { date ->
                binding.tvDate.text = "Date : $date"
            }
        }
    }

}