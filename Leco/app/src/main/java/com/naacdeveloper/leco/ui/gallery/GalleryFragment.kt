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
            alerta(mensaje);
        }else{
             registrarInmueble();
        }
    }

    private fun registrarInmueble() {
        inmueble?.estado = etEstado?.text.toString();
        inmueble?.direccion = etDireccion?.text.toString();
        inmueble?.nombre = etNombre?.text.toString();
        inmueble?.descripcion = etDescripcion?.text.toString();
        if(!InmuebleService.subirInmueble("/api/Inmueble",root?.context!!, inmueble!!)){
            if(!InmuebleService.subirFoto("/Foto",root?.context!!, fotoInmueble!!)){
                MostrarMensaje("¡Inmueble subido exitosamente!")
            }
        }

    }

    private fun inicializarVariables(){
        imgLocal = root?.findViewById(R.id.imgLocal);
        ibAgregarFoto = root?.findViewById(R.id.ibAgregarFoto);
        btnAgregarInmueble = root?.findViewById(R.id.btnAgregarInmueble);

        inmueble = Inmueble("Defecto","Defecto","Defecto","Defecto","Defecto",null);
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
            MostrarMensaje("Permiso denegado, debe permitirlo manualmente.");
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
                MostrarMensaje("Permiso denegado");
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 777){
            imgLocal?.setImageURI(data?.data);
            imgB64 = InmuebleService.parsearIMagenBase64(imgLocal!!);
            fotoInmueble?.imagen = imgB64!!;
            MostrarMensaje("¡Imagen agregada correctamente!");
        }
    }



    fun alerta(Mensaje:String){
        val mAlertDialog = AlertDialog.Builder(root?.context!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
        mAlertDialog.setTitle("Alerta!")
        mAlertDialog.setMessage(Mensaje)
        mAlertDialog.setNegativeButton("Ok") { dialog, id ->
            Toast.makeText(root?.context, "Ok", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.show()
    }
    fun  MostrarMensaje(Mensaje:String){
        val mAlertDialog = AlertDialog.Builder(root?.context!!)
        mAlertDialog.setIcon(R.mipmap.ic_launcher) //Icono de la alerta
        mAlertDialog.setTitle("En hora buena!") //Titulo de la alerta
        mAlertDialog.setMessage(Mensaje) //Mensaje de la alerta
        mAlertDialog.setNegativeButton("Ok") { dialog, id ->
            Toast.makeText(root?.context, "Ok", Toast.LENGTH_SHORT).show()
        }
        mAlertDialog.show()
    }
}