package com.example.mimapa



import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.example.mimapa.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mimapa.databinding.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERMISO_LOCALIZACION: Int=3
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled=true
        //Los permisos
        enableMyLocation()
        mMap.uiSettings.isZoomControlsEnabled=true//Botones
        val centro = LatLng(42.23656846001073, -8.714151073325072)//latitud y longitud
        mMap.addMarker(MarkerOptions().position(centro).title("Centro de explotacion legal")) //Añado al mapa una marca con la anterior variable
        mMap.moveCamera(CameraUpdateFactory.newLatLng(centro))//Me lleva a la marca
    }

    private fun comprobarPermisos(): Boolean {

        when{
            //Me dice si tengo los permisos
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED->{
                Log.i("Permisos","permiso garantozado")
                mensajeUsuario("Tienes Permisos")
                return true
            }
            //Si no los tengo me dice que los active
            shouldShowRequestPermissionRationale (Manifest.permission.ACCESS_FINE_LOCATION
            )->{
                mensajeUsuario("Da permisos en ajustes")
                return false
            }
            //La primera vez me da la opcion de aceptar los permisos
            else->{
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISO_LOCALIZACION)
                return false
            }
        }
    }
    //Comprueba que los permisos funcionan
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISO_LOCALIZACION->{ //Conpruebo si mi permiso funciona
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mMap.isMyLocationEnabled = true
                }
            }

            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
    //Muestra mensajes al usuario
    fun mensajeUsuario(mensaje:String){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
    }
    //Se inicia el mapa y comprueba los permisos
    @SuppressLint("MissingPermission")
    private fun enableMyLocation(){
        if(!::mMap.isInitialized) return
        if(comprobarPermisos()){
            mMap.isMyLocationEnabled = true
        } else{
            comprobarPermisos()
        }
    }


}