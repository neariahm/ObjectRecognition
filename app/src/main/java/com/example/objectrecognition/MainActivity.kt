package com.example.objectrecognition

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // - Obtain image through Camera -
        //Set an onClickListener on the button to take a picture     (from Camera Guide)
        findViewById<Button>(R.id.button).setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    // Create an intent that launches the camera and takes a picture
    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    // Get the thumbnail to get the image back from the camera application (from Camera Guide)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Grab bitmap from image that was taken
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // Set bitmap as imageView image
            findViewById<ImageView>(R.id.imageView).setImageBitmap(imageBitmap)

            // - Prepare image (bitmap) for ML Kit APIs -
            val imageForMLKit = InputImage.fromBitmap(imageBitmap, 0)

            // - Initialize specific ML Kit API you want to use -
            //  Utilize image labeling API (from ML Kit)                
            // To use default options:
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            // Pass the image to the process() method (from ML Kit)
            labeler.process(imageForMLKit)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    Log.i("Neariah", "Successfully processed image")
                    //Get information about labeled objects
                    for (label in labels) {
                     // What was detected in the image
                      val text = label.text
                      // The confidence score of what was detected  
                      val confidence = label.confidence
                      Log.i("Neariah", "detected: " + text + " with confidence: " + confidence)
                }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Log.i("Neariah", "Error processing image")

                    // Display information in textView
                   // findViewById<TextView>(R.id.textView).text
                }

        }
    }
}