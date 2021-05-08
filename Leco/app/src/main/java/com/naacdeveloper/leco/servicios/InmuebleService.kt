package com.naacdeveloper.leco.servicios

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.util.Base64
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.naacdeveloper.leco.modelos.FotoInmueble
import com.naacdeveloper.leco.modelos.Inmueble
import com.naacdeveloper.leco.modelos.Inmuebles
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
            var dialog = ProgressDialog.show(
                context,
                "Validando direccion duplicada.",
                "Espere un momento...",
                true
            );

            val solicitud = object :StringRequest(
                Request.Method.GET,
                baseUrl + "/Buscar/Inmueble/Direccion/${inmueble.direccion}",
                Response.Listener<String> {
                    dialog.dismiss();
                    Mensajes.alerta(
                        "Ya existe un inmueble registrado con esta direccion.",
                        context
                    );
                },
                Response.ErrorListener {
                    dialog.dismiss();
                    subirInmueble("/api/Inmueble", context, inmueble);
                }
            ){}
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
            var dialog = ProgressDialog.show(
                context,
                "Enviando inmueble al servidor",
                "Espere un momento...",
                true
            );

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

        fun obtenerInmuebles(cola: RequestQueue, context: Context, rvInmuebles: RecyclerView){


            val solicitud = object:StringRequest(
                Request.Method.GET,
                "http://192.168.100.214:8081/InmueblesConFotoPrincipal",
                Response.Listener { response ->
                    try {
                        val gson = Gson();
                        var inmuebelsJson = "{ \"inmuebles\":$response}";
                        var inmueblesRespuesta = gson.fromJson(
                            inmuebelsJson,
                            Inmuebles::class.java
                        )

                        var adaptador = AdaptadorPersonalizado(
                            context!!,
                            inmueblesRespuesta.inmuebles
                        );

                        rvInmuebles?.adapter = adaptador;
                    } catch (e: Exception) {

                    }
                },
                Response.ErrorListener { }){}

            cola.add(solicitud);
        }

        fun hayRed(activity: Activity):Boolean{
            val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager;
            val networkInfo = connectivityManager.activeNetworkInfo;
            return  networkInfo != null && networkInfo.isConnected;
        }

        fun buscarInmuebletoActualizar(context: Context, inmueble: Inmueble){
            val gson = Gson()

            val cola = Volley.newRequestQueue(context);
            val solicitud = object :StringRequest(
                Request.Method.GET,
                baseUrl + "/Buscar/Inmueble/Direccion/${inmueble.direccion}",
                Response.Listener<String> { response ->
                    val inmueble = gson.fromJson(response, Inmueble::class.java)
                    actualizarInmueble(context, inmueble)
                    Mensajes.alerta(
                        "Ya existe un inmueble registrado con esta direccion.",
                        context
                    );
                },
                Response.ErrorListener {

                    subirInmueble("/api/Inmueble", context, inmueble);
                }
            ){}
            cola.add(solicitud);
        }

        fun actualizarInmueble(context: Context, inmueble: Inmueble){
            val cola = Volley.newRequestQueue(context);
            val gson = Gson();
            val requestBody = gson.toJson(inmueble);

            val putRequest: StringRequest = object : StringRequest(Method.PUT,
                baseUrl + "/api/Inmueble/${inmueble}",
                Response.Listener {
                    Mensajes.MostrarMensaje("Inmueble Actualizado Correctamente", context)
                },
                Response.ErrorListener {
                    Mensajes.MostrarMensaje(
                        "Se produjo un error, intentelo nuevamente",
                        context
                    )
                }
            ) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8";
                }
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray();
                }
            }

            cola.add(putRequest);
        }
    }


}