package com.naacdeveloper.leco.ui.gallery

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.naacdeveloper.leco.R
import com.naacdeveloper.leco.modelos.FotoInmueble
import com.naacdeveloper.leco.modelos.Inmueble
import com.naacdeveloper.leco.servicios.InmuebleService
import com.naacdeveloper.leco.servicios.Mensajes

class GalleryFragment : Fragment() {

    var root: View? = null;
    var imgLocal: ImageView? = null;
    var imgB64: String? = null;
    var ibAgregarFoto: ImageButton? = null;
    var btnAgregarInmueble: Button? = null;

    var etNombre: EditText? = null;
    var etEstado: EditText? = null;
    var etDireccion: EditText? = null;
    var etDescripcion: EditText? = null;


    var inmueble: Inmueble? = null;
    var fotoInmueble: FotoInmueble? = null;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_gallery, container, false)
        inicializarVariables();

        ibAgregarFoto?.setOnClickListener {
            ValidarPermisoGaleria();
        }

        btnAgregarInmueble?.setOnClickListener {
            ValidarCampos();
        }

        return root
    }

    private fun ValidarCampos() {
        var datosFaltantes: String = "";
        var mensaje: String = "Datos erroneos: \n\n";
        var error: Boolean = false;
        if (etNombre?.text.toString() == ""){
            datosFaltantes += "- Nombre. \n";
            error = true;
        }
        if(etEstado?.text.toString() == ""){
            datosFaltantes+="- Estado. \n";
            error = true;
        }
        if(etDireccion?.text.toString() == ""){
            datosFaltantes+="- Direccion. \n";
            error = true;
        }
        if(etDescripcion?.text.toString() == ""){
            datosFaltantes+="- Descripcion. \n";
            error = true;
        }
        if(fotoInmueble?.imagen == "Defecto"){
            datosFaltantes+="- Imagen. \n";
            error = true;
        }
        if(error){
            mensaje+=datosFaltantes;
            mensaje+="\nPor favor verifique.";
            Mensajes.alerta(mensaje, root?.context!!);
        }else{
             registrarInmueble();
        }
    }

    private fun registrarInmueble() {
        inmueble?.estado = etEstado?.text.toString();
        inmueble?.direccion = etDireccion?.text.toString();
        inmueble?.nombre = etNombre?.text.toString();
        inmueble?.descripcion = etDescripcion?.text.toString();
        InmuebleService.registrarInmueble(root?.context!!,inmueble!!);
    }

    private fun inicializarVariables(){
        imgLocal = root?.findViewById(R.id.imgLocal);
        ibAgregarFoto = root?.findViewById(R.id.ibAgregarFoto);
        btnAgregarInmueble = root?.findViewById(R.id.btnAgregarInmueble);

        inmueble = Inmueble("Defecto","Defecto","Defecto","Defecto","Defecto", ArrayList());
        fotoInmueble = FotoInmueble("Defecto","Defecto","Defecto");

        etNombre = root?.findViewById(R.id.etNombre);
        etEstado = root?.findViewById(R.id.etEstado);
        etDescripcion = root?.findViewById(R.id.etDescripcion);
        etDireccion = root?.findViewById(R.id.etDireccion);
    }

    private fun ValidarPermisoGaleria(){
        if(ContextCompat.checkSelfPermission(root?.context as Activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
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
        if(ActivityCompat.shouldShowRequestPermissionRationale(root?.context as Activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Mensajes.MostrarMensaje("Permiso denegado, debe permitirlo manualmente.",root?.context!!);
        }else{
            ActivityCompat.requestPermissions(root?.context as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 777);
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 777){
            if(grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirGaleria();
            }else {
                Mensajes.MostrarMensaje("Permiso denegado",root?.context!!);
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 777){
            imgLocal?.setImageURI(data?.data);
            imgB64 = InmuebleService.parsearIMagenBase64(imgLocal!!);
            fotoInmueble?.imagen = imgB64!!;
            inmueble?.fotos?.add(fotoInmueble!!);
            Mensajes.MostrarMensaje("¡Imagen agregada correctamente!", root?.context!!);
        }
    }

}