package com.aimmore.airbnv_ca3

import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

class PropDetails : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var name: TextView
    private lateinit var location: TextView
    private lateinit var price: TextView
    private lateinit var description: TextView
    private lateinit var contact: TextView
    private lateinit var favButton: ImageButton
    private lateinit var delButton: ImageButton
    private lateinit var propertyImageView: ImageView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    private var propertyId: Long = -1L
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prop_details)

        name = findViewById(R.id.propertyName)
        location = findViewById(R.id.propertyLocation)
        price = findViewById(R.id.propertyPrice)
        description = findViewById(R.id.propertyDesc)
        contact = findViewById(R.id.propertyContact)
        favButton = findViewById(R.id.favoriteButton)
        delButton = findViewById(R.id.deleteButton)
        propertyImageView = findViewById(R.id.imageView)
        toolbar = findViewById(R.id.toolbar)
        mapView = findViewById(R.id.propertyMapView)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        propertyId = intent.getLongExtra("PROPERTY_ID", -1L)
        if (propertyId == -1L) {
            Toast.makeText(this, "Invalid property ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch and Display Property Details
        fetchAndDisplayProperty()

        // Handle Favorite Button Click
        favButton.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteStatus(isFavorite)
        }

        // Handle Delete Button Click
        delButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun fetchAndDisplayProperty() {
        val db = PropertyDB(this)
        val property = db.GetPropertyBYId(propertyId)

        if (property != null) {
            name.text = property.name
            location.text = property.location
            price.text = "Price: $${property.price}"
            description.text = property.description
            contact.text = "Contact: ${property.contact}"

            property.image?.let { imageByteArray ->
                val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
                propertyImageView.setImageBitmap(bitmap)
            }

            isFavorite = property.isFavorite
            updateFavoriteButtonUI()

            // Update the map with the property's location
            updateMapWithLocation(property.location)
        } else {
            Toast.makeText(this, "Property not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateMapWithLocation(locationString: String) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addressList = geocoder.getFromLocationName(locationString, 1)
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                val propertyLatLng = LatLng(address.latitude, address.longitude)

                googleMap.addMarker(
                    MarkerOptions()
                        .position(propertyLatLng)
                        .title("Property Location")
                )

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLatLng, 15f))
            } else {
                Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error fetching location: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateFavoriteStatus(isFav: Boolean) {
        val db = PropertyDB(this)
        db.UpdateFavProperty(propertyId, isFav)

        val message = if (isFav) {
            "Added to Favorites"
        } else {
            "Removed from Favorites"
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        updateFavoriteButtonUI()
    }

    private fun updateFavoriteButtonUI() {
        val favoriteIcon = if (isFavorite) {
            R.drawable.ic_favorite
        } else {
            R.drawable.ic_favorite_border
        }
        favButton.setImageResource(favoriteIcon)
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Property")
            .setMessage("Are you sure you want to delete this property?")
            .setPositiveButton("Yes") { _, _ -> deleteProperty() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteProperty() {
        val db = PropertyDB(this)
        db.DeleteProperty(propertyId)
        Toast.makeText(this, "Property deleted successfully", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
