package com.aimmore.airbnv_ca3

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import java.io.ByteArrayOutputStream

class AddProperty : AppCompatActivity() {

    private lateinit var propName: EditText
    private lateinit var propLocation: EditText
    private lateinit var propPrice: EditText
    private lateinit var propDesc: EditText
    private lateinit var propContact: EditText
    private lateinit var saveBtn: Button
    private lateinit var backBtn: ImageButton
    private lateinit var pickImage: ImageButton
    private var imageUri: Uri? = null

    // Register the image picker activity result launcher
    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            pickImage.setImageURI(uri)
            Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Image not available", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_property)

        propName = findViewById(R.id.nameEditText)
        propLocation = findViewById(R.id.locationEditText)
        propPrice = findViewById(R.id.priceEditText)
        propDesc = findViewById(R.id.descriptionEditText)
        propContact = findViewById(R.id.contactEditText)
        saveBtn = findViewById(R.id.addPropertyBtn)
        backBtn = findViewById(R.id.backBtn)
        pickImage = findViewById(R.id.imageButton)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        pickImage.setOnClickListener {
            getImage.launch("image/*")
        }

        // Save button functionality
        saveBtn.setOnClickListener {
            val name = propName.text.toString().trim()
            val location = propLocation.text.toString().trim()
            val priceInput = propPrice.text.toString().trim()
            val description = propDesc.text.toString().trim()
            val contact = propContact.text.toString().trim()

            // Validate fields
            if (name.isEmpty() || location.isEmpty() || priceInput.isEmpty() || description.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Parse and validate price
            val price: Double
            try {
                price = priceInput.toDouble()
                if (price <= 0.0) {
                    Toast.makeText(this, "Price must be greater than zero", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid price value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert URI to Bitmap if available (for saving to database)
            val propertyImageByteArray = imageUri?.let {
                val inputStream = contentResolver.openInputStream(it)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                bitmap?.let { bmp ->
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    byteArrayOutputStream.toByteArray()
                }
            }

            val property = PropsModel(
                id = 0,
                name = name,
                location = location,
                price = price,
                description = description,
                contact = contact,
                image = propertyImageByteArray // Pass the ByteArray to the PropsModel
            )

            // Insert property into the database using PropertyDB
            val db = PropertyDB(this) // Assuming PropertyDB handles database operations
            val id = db.InsertProperty(property)

            if (id > 0) {
                Toast.makeText(this, "Property added successfully!", Toast.LENGTH_SHORT).show()
                // Navigate back to main activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to add property. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handle permissions result if necessary
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the image picking
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
