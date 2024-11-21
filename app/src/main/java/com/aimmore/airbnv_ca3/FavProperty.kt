package com.aimmore.airbnv_ca3

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class FavProperty : AppCompatActivity() {
    private lateinit var favList: ListView
    private lateinit var favPropAdapter: FavPropAdapter
    private lateinit var backBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_property)

        favList = findViewById(R.id.favPropLV)
        backBtn = findViewById(R.id.backBtn)

        backBtn.setOnClickListener {
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }

        // Fetch favorite properties
        val db = PropertyDB(this)
        val favoriteProperties = db.GetFavouriteProperty()

        // Initialize the adapter with the favorite properties list and item click listener
        favPropAdapter = FavPropAdapter(this, favoriteProperties) { propertyId ->
            // Open the PropDetails activity on click, passing the selected property ID
            val intent = Intent(this, PropDetails::class.java)
            intent.putExtra("PROPERTY_ID", propertyId)
            startActivity(intent)
        }

        // Set the adapter to the ListView
        favList.adapter = favPropAdapter
    }
}
