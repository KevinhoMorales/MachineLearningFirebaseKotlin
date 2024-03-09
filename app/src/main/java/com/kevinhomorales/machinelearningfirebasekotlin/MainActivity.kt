package com.kevinhomorales.machinelearningfirebasekotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.kevinhomorales.machinelearningfirebasekotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpView()
    }

    private fun setUpView() {
        FirebaseApp.initializeApp(this)
        setUpActions()
    }

    private fun setUpActions() {
        binding.readTextButtonId.setOnClickListener {
            selectImage()
        }
    }

    private fun setUpVisionML(){
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
        // Obtener la imagen como FirebaseVisionImage
        val image = FirebaseVisionImage.fromFilePath(this, imageUri)
        // Procesar la imagen para reconocimiento de texto
        textRecognizer.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                // Obtener resultados de texto
                val resultText = firebaseVisionText.text
                // Realizar acciones con el texto reconocido
                binding.resultText.setText(resultText)
            }
            .addOnFailureListener { e ->
                // Manejar errores
                print(e.message)
            }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && data != null && data.data != null) {
            imageUri = data.data!!
            binding.imageViewId.setImageURI(imageUri)
            setUpVisionML()
        }
    }

}