package com.naacdeveloper.leco

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.naacdeveloper.leco.modelos.AutorizacionPeticion
import com.naacdeveloper.leco.servicios.AutorizacionService

class Autorizacion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autorizacion)

        var btnValidaFrase = findViewById<Button>(R.id.btnValidarFrase);
        var etFrase = findViewById<EditText>(R.id.etFrase);
        btnValidaFrase.setOnClickListener {
            var autorizacionPeticion = AutorizacionPeticion(etFrase.text.toString());
            AutorizacionService.ObtenerToken(this,autorizacionPeticion);
        }

    }
}