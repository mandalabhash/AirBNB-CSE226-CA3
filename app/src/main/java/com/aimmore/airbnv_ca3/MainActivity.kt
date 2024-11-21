package com.aimmore.airbnv_ca3

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var addFAB: FloatingActionButton
    private lateinit var favFAB: FloatingActionButton
    private lateinit var searchView: SearchView
    private lateinit var propertyListView: ListView
    private lateinit var adapter: PropertyAdapter

    private val propertyList = mutableListOf<PropsModel>()
    private val filteredList = mutableListOf<PropsModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFAB = findViewById(R.id.addFabBtn)
        favFAB = findViewById(R.id.favFabBtn)
        searchView = findViewById(R.id.searchView)
        propertyListView = findViewById(R.id.propertiesList)

        val db = PropertyDB(this)
        propertyList.addAll(db.GetAllProperty())
        filteredList.addAll(propertyList)

        adapter = PropertyAdapter(this, filteredList)
        propertyListView.adapter = adapter

        addFAB.setOnClickListener {
            val intent = Intent(this, AddProperty::class.java)
            startActivity(intent)
            finish()
        }

        favFAB.setOnClickListener {
            val intent = Intent(this, FavProperty::class.java)
            startActivity(intent)
            finish()
        }

        // Filter property list based on search query
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList.clear()
                if (newText.isNullOrEmpty()) {
                    filteredList.addAll(propertyList)
                } else {
                    val searchText = newText.lowercase()
                    propertyList.forEach { property ->
                        if (property.location.lowercase().contains(searchText)) {
                            filteredList.add(property)
                        }
                    }
                }
                adapter.updateList(filteredList)
                return true
            }
        })
    }
}
