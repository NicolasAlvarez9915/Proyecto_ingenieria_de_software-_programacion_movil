package com.naacdeveloper.leco

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.naacdeveloper.leco.modelos.Inmueble
import com.naacdeveloper.leco.servicios.InmuebleService

class Actualizar : AppCompatActivity() {
    var inmueble: Inmueble = Inmueble("","","","","",null);
    var imagen: String = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)
        recibirInmueble();

        var btnActualizarEditar = findViewById<Button>(R.id.btnActualizarEditar);
        var btnEliminar = findViewById<Button>(R.id.btnEliminar);

        btnActualizarEditar.setOnClickListener {
            actualizarInbmueble();
        }
        btnEliminar.setOnClickListener {
            InmuebleService.eliminarInmueble(this,this.inmueble.codigo);
        }
    }

    fun actualizarInbmueble(){
        val etEstado = findViewById<EditText>(R.id.etEstadoEditar)
        this.inmueble.estado = etEstado.text.toString();

        val etDescipcion = findViewById<EditText>(R.id.edtDescripcionEditar)
        this.inmueble.direccion = etDescipcion.text.toString();

        InmuebleService.actualizarInmueble(this, this.inmueble);
    }

    fun recibirInmueble(){

        val bundle = intent.extras;
        this.inmueble.codigo = bundle?.get("codigo").toString();
        this.inmueble.nombre = bundle?.get("nombre").toString();
        this.inmueble.descripcion = bundle?.get("descripcion").toString();
        this.inmueble.estado = bundle?.get("estado").toString();
        this.inmueble.direccion = bundle?.get("direccion").toString();

        val settings = this.getSharedPreferences("Imagen",0);
        this.imagen  = settings.getString("Imagen", "")!!;
        mapearInmueble();
    }

    fun mapearInmueble(){

        val etNombre = findViewById<TextView>(R.id.tvNombreEdit);
        etNombre.setText(this.inmueble.nombre);

        val etNombres = findViewById<TextView>(R.id.tvDireccionEditar)
        etNombres.setText(this.inmueble.descripcion);

        val etEstado = findViewById<EditText>(R.id.etEstadoEditar)
        etEstado.setText(this.inmueble.estado);

        val etDescipcion = findViewById<EditText>(R.id.edtDescripcionEditar)
        etDescipcion.setText(this.inmueble.direccion);

        val imageBytes = Base64.decode(this.imagen, Base64.DEFAULT);
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
        val iVImagen = findViewById<ImageView>(R.id.iVImagen);
        iVImagen.setImageBitmap(decodedImage);
        iVImagen.getLayoutParams().width = 500; iVImagen.getLayoutParams().height = 500; iVImagen.setAdjustViewBounds(true);
    }
}