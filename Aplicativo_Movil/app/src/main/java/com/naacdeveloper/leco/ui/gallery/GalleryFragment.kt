package com.naacdeveloper.leco.ui.gallery

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.naacdeveloper.leco.R
import java.io.ByteArrayOutputStream


class GalleryFragment : Fragment() {

    var btnGaleria: Button? = null;
    var root: View? = null;
    var imgLocal: ImageView? = null;
    var tvimgbas64: TextView? = null;
    var imgNube: ImageView? = null;
    

    var imgB64: String? = null;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        root = inflater.inflate(R.layout.fragment_gallery, container, false)
        imgLocal = root?.findViewById(R.id.imgLocal);
        tvimgbas64 = root?.findViewById(R.id.tvimgbas64);
        imgNube = root?.findViewById(R.id.imgNube);

        btnGaleria = root?.findViewById(R.id.btnGaleria);
        btnGaleria?.setOnClickListener {
            checkPermissions();
        }

        return root
    }

    private fun checkPermissions() {
        if(ContextCompat.checkSelfPermission(root?.context as Activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            pedirPerdisoAlmacenamiento();
        }else{
            abrirGaleria();
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 777){
            if(grantResults.isEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                abrirGaleria( );
            }else{
                Toast.makeText(root?.context, "Permiso denegado", Toast.LENGTH_LONG).show();
            }
            if(grantResults.isEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                abrirGaleria( );
            }else{
                Toast.makeText(root?.context, "Permiso denegado", Toast.LENGTH_LONG).show();
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == 777){
            imgLocal?.setImageURI(data?.data);
            parsearIMagenBase64();
            parsearBase64Imagen();
        }
    }

    private fun parsearIMagenBase64() {
        val bitmap = (imgLocal?.drawable as BitmapDrawable).bitmap
        if (bitmap != null) {
            val byteArrayOutputStream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray();
            imgB64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.d("Img64", imgB64!!);
        }
    }

    private fun parsearBase64Imagen(){
        val imageBytes = Base64.decode(imgB64, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        imgNube?.setImageBitmap(decodedImage)
    }


    private fun pedirPerdisoAlmacenamiento() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(root?.context as Activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            //Ya se han rechazados los permisos.
            Toast.makeText(root?.context, "Permiso denegado", Toast.LENGTH_LONG).show();
        }else{
            //Pedir permiso
            ActivityCompat.requestPermissions(root?.context as Activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA), 777);
        }
    }

    private fun abrirGaleria( ){
        val intenGaleria = Intent(Intent.ACTION_PICK);
        intenGaleria.type = "image/*";
        startActivityForResult(intenGaleria, 777);
    }




}