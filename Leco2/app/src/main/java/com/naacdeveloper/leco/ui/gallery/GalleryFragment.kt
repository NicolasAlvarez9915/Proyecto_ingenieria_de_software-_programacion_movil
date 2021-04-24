package com.naacdeveloper.leco.ui.gallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.naacdeveloper.leco.R
import com.naacdeveloper.leco.servicios.InmuebleService

class GalleryFragment : Fragment() {

    var vista: View? = null;
    var imgLocal: ImageView? = null;
    var imgB64: String? = null;
    var ibAgregarFoto: Button? = null;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        vista = inflater.inflate(R.layout.fragment_slideshow, container, false)
        imgLocal = vista?.findViewById(R.id.imgLocal);
        ibAgregarFoto = vista?.findViewById(R.id.ibAgregarFoto);

        ibAgregarFoto?.setOnClickListener {
            ValidarPermisoGaleria();
        }

        return vista
    }

    private fun ValidarPermisoGaleria(){
        if(ContextCompat.checkSelfPermission(vista?.context as Activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            pedirPerdisoAlmacenamiento();
        }else{
            abrirGaleria();
        }
    }

    private fun abrirGaleria() {
        val intenGaleria = Intent(Intent.ACTION_PICK);
        intenGaleria.type = "image/*";
        startActivityForResult(intenGaleria, 777);
    }

    private fun pedirPerdisoAlmacenamiento() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(vista?.context as Activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            MostrarMensaje("Permiso denegado, debe permitirlo manualmente.");
        }else{
            ActivityCompat.requestPermissions(vista?.context as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 777);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 777){
            if(grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirGaleria();
            }else {
                MostrarMensaje("Permiso denegado");
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 777){
            imgLocal?.setImageURI(data?.data);
            imgB64 = InmuebleService.parsearIMagenBase64(imgLocal!!);
            MostrarMensaje(imgB64!!);
        }
    }


    fun MostrarMensaje(Mensaje: String){
        Snackbar.make(vista!!, Mensaje, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
    }
}