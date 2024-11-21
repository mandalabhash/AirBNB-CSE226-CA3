package com.aimmore.airbnv_ca3

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class FavPropAdapter(
    private val context: Context,
    private var favoritePropertyList: List<PropsModel>,
    private val itemClickListener: (Long) -> Unit // Lambda function to handle item click
) : BaseAdapter() {

    override fun getCount(): Int = favoritePropertyList.size

    override fun getItem(position: Int): Any = favoritePropertyList[position]

    override fun getItemId(position: Int): Long = favoritePropertyList[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.property_card_view, parent, false)

        val property = favoritePropertyList[position]

        val propertyName: TextView = view.findViewById(R.id.Name)
        val propertyLocation: TextView = view.findViewById(R.id.location)
        val propertyPrice: TextView = view.findViewById(R.id.price)
        val propertyImage: ImageView = view.findViewById(R.id.Image)

        // Populate the view with property details
        propertyName.text = property.name
        propertyLocation.text = property.location
        propertyPrice.text = "Price: $${property.price}"

        // Check if the property has an image and display it
        if (property.image != null) {
            // Convert ByteArray to Bitmap and set to ImageView
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(property.image, 0, property.image.size)
            propertyImage.setImageBitmap(bitmap)
        } else {
            // Set a default image if no image is available
            propertyImage.setImageResource(R.drawable.sample) // Replace with your default image
        }

        // Set an OnClickListener for each list item
        view.setOnClickListener {
            // Trigger the listener with the property ID
            itemClickListener(property.id)
        }

        return view
    }

    fun updateList(newList: List<PropsModel>) {
        favoritePropertyList = newList
        notifyDataSetChanged()
    }
}
