package com.naacdeveloper.leco.servicios

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.naacdeveloper.leco.modelos.FotoInmueble
import com.naacdeveloper.leco.modelos.Inmueble
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

        fun registrarInmueble(context: Context, inmueble: Inmueble){
            val cola = Volley.newRequestQueue(context);
            var dialog = ProgressDialog.show(context, "Validando direccion duplicada.", "Espere un momento...", true);

            val solicitud = object :StringRequest(
                    Request.Method.GET,
                    baseUrl + "/Buscar/Inmueble/Direccion/${inmueble.direccion}",
                    Response.Listener<String> {
                        dialog.dismiss();
                        Mensajes.alerta("Ya existe un inmueble registrado con esta direccion.", context);
                    },
                    Response.ErrorListener { r ->
                        Mensajes.alerta(r.printStackTrace().toString(), context);
                        dialog.dismiss();
                        subirInmueble("/api/Inmueble", context, inmueble);
                    }){}

            cola.add(solicitud);
        }

        fun subirFoto(url: String, context: Context, fotoInmueble: FotoInmueble){
            val cola = Volley.newRequestQueue(context);
            var dialog = ProgressDialog.show(context, "", "Espere un momento...", true);


            val gson = Gson();
            val requestBody = gson.toJson(fotoInmueble);

            val solicitud: StringRequest = object : StringRequest(
                    Method.POST, baseUrl + url,
                    Response.Listener {
                        dialog.dismiss();
                    }, Response.ErrorListener { response ->
                Mensajes.alerta("¡Error al subir la foto!", context);
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
        }

        fun subirInmueble(url: String, context: Context, inmueble: Inmueble){
            val cola = Volley.newRequestQueue(context);
            var dialog = ProgressDialog.show(context, "Enviando inmueble al servidor", "Espere un momento...", true);

            val gson = Gson();
            val requestBody = gson.toJson(inmueble);

            val solicitud: StringRequest = object : StringRequest(
                    Method.POST, baseUrl + url,
                    Response.Listener {
                        Mensajes.MostrarMensaje("¡Inmueble subido exitosamente!", context);
                        dialog.dismiss();
                    }, Response.ErrorListener { response ->
                Mensajes.alerta("¡Error al subir el inmueble!", context);
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

            
        }
    }


}