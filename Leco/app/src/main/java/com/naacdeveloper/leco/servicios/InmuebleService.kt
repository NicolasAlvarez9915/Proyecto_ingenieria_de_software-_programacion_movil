package com.naacdeveloper.leco.servicios

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.naacdeveloper.leco.modelos.FotoInmueble
import com.naacdeveloper.leco.modelos.Inmueble
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class InmuebleService {



    companion object{
        val baseUrl:String = "http://192.168.100.214:8081";
        fun parsearIMagenBase64(img: ImageView): String {
            val bitmap = (img.drawable as BitmapDrawable).bitmap
            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
            return "";
        }

        fun parsearBase64Imagen(imgB64: String, img: ImageView): ImageView{
            val imageBytes = Base64.decode(imgB64, Base64.DEFAULT);
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
            img.setImageBitmap(decodedImage);
            return img;
        }

        fun subirFoto(url: String, context: Context, fotoInmueble: FotoInmueble): Boolean{
            val cola = Volley.newRequestQueue(context);
            var error = false;
            var dialog = ProgressDialog.show(context, "", "Espere un momento...", true);


            val jsonBody = JSONObject()
            jsonBody.put("codigo", fotoInmueble.codigo)
            jsonBody.put("codInmueble", fotoInmueble.codInmueble)
            jsonBody.put("imagen", fotoInmueble.imagen)

            val requestBody = jsonBody.toString()

            val solicitud: StringRequest = object : StringRequest(
                Method.POST, baseUrl+url,
                Response.Listener {
                    dialog.dismiss();
                }, Response.ErrorListener {
                        response ->
                    Toast.makeText(context, "Fallo al subir la imagen.", Toast.LENGTH_LONG).show();
                    error = true;
                    dialog.dismiss();
                }){
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8";
                }
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }
            cola.add(solicitud);
            return  error;
        }

        fun subirInmueble(url: String, context: Context, inmueble: Inmueble): Boolean{
            val cola = Volley.newRequestQueue(context);
            var error: Boolean = false;
            var dialog = ProgressDialog.show(context, "", "Espere un momento...", true);


            val jsonBody = JSONObject()
            jsonBody.put("codigo", inmueble.codigo);
            jsonBody.put("nombre", inmueble.nombre);
            jsonBody.put("estado", inmueble.estado);
            jsonBody.put("direccion", inmueble.direccion);
            jsonBody.put("descripcion", inmueble.descripcion);

            val requestBody = jsonBody.toString()

            val solicitud: StringRequest = object : StringRequest(
                Method.POST, baseUrl+url,
                Response.Listener {
                    dialog.dismiss();
                }, Response.ErrorListener {
                        response ->
                    Toast.makeText(context, "Fallo al subir el inmueble.", Toast.LENGTH_LONG).show();
                    error = true;
                    dialog.dismiss();
                }){
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8";
                }
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
            }
            cola.add(solicitud);
            return error;
        }
    }


}