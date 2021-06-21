package com.naacdeveloper.leco.servicios

import android.R.attr.password
import android.R.attr.targetActivity
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.naacdeveloper.leco.modelos.FotoInmueble
import com.naacdeveloper.leco.modelos.Inmueble
import com.naacdeveloper.leco.modelos.Inmuebles
import java.io.ByteArrayOutputStream


class InmuebleService {

    companion object{
        val baseUrl:String = "http://192.168.0.65:8081/";

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
                baseUrl + "Buscar/Inmueble/Direccion/${inmueble.direccion}",
                Response.Listener<String> {
                    dialog.dismiss();

                    Mensajes.alerta(
                        "Ya existe un inmueble registrado con esta direccion.",
                        context
                    );
                },
                Response.ErrorListener {
                    dialog.dismiss();
                    subirInmueble("api/Inmueble", context, inmueble);
                }
            ){
                override fun getHeaders(): Map<String, String>? {
                    val headers: HashMap<String, String> = HashMap()
                    headers["Content-Type"] = "application/json";
                    headers.put("Authorization", "Bearer " + AutorizacionService.ObtenerToken(context as Activity));
                    return headers
                }
            }
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
                override fun getHeaders(): Map<String, String>? {
                    val headers: HashMap<String, String> = HashMap()
                    headers["Content-Type"] = "application/json";
                    headers.put("Authorization", "Bearer " + AutorizacionService.ObtenerToken(context as Activity));
                    return headers
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
                Method.POST, "http://192.168.0.65:8081/api/Inmueble",
                Response.Listener {
                    Mensajes.MostrarMensaje("¡Inmueble subido exitosamente!", context);
                    dialog.dismiss();
                }, Response.ErrorListener { response ->
                    Mensajes.alerta("¡Error al subir el inmueble!${response.message}", context);
                    dialog.dismiss();
                }){
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8";
                }
                override fun getBody(): ByteArray {
                    return requestBody.toByteArray()
                }
                @Throws(AuthFailureError::class)
                override fun  getHeaders():  Map<String, String> {
                     val params: HashMap<String, String> = HashMap();
                    params["Authorization"] = "Bearer " + AutorizacionService.ObtenerToken(context as Activity);
                    return params;
                };
            }
            cola.add(solicitud);
        }

        fun obtenerInmuebles(cola: RequestQueue, context: Context, rvInmuebles: RecyclerView, abrirActualizar: AbrirActualizar, Refres: SwipeRefreshLayout?){


            val solicitud = object:StringRequest(
                Request.Method.GET,
                baseUrl+"InmueblesConFotoPrincipal",
                Response.Listener { response ->
                    try {
                        val gson = Gson();
                        var inmuebelsJson = "{ \"inmuebles\":$response}";
                        var inmueblesRespuesta = gson.fromJson(
                            inmuebelsJson,
                            Inmuebles::class.java
                        )

                        var adaptador = AdaptadorPersonalizado(object:ClickListener{
                            override fun onClick(view: View, index: Int) {
                                abrirActualizar.abrirActualizar(inmueblesRespuesta.inmuebles[index])
                            }
                        },
                            inmueblesRespuesta.inmuebles
                        );
                        if(Refres != null){
                            Refres.isRefreshing = false;
                        }

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
                baseUrl + "api/Inmueble/${inmueble.codigo}",
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
                @Throws(AuthFailureError::class)
                override fun  getHeaders():  Map<String, String> {
                    val params: HashMap<String, String> = HashMap();
                    params["Authorization"] = "Bearer " + AutorizacionService.ObtenerToken(context as Activity);
                    return params;
                };
            }

            cola.add(putRequest);
        }

        fun eliminarInmueble(context: Context, codigo: String){
            val cola = Volley.newRequestQueue(context);
            var dialog = ProgressDialog.show(
                context,
                "Eliminando inmueble en el servidor",
                "Espere un momento...",
                true
            );


            val solicitud: StringRequest = object : StringRequest(
                Method.DELETE, "http://192.168.0.65:8081/api/Inmueble/$codigo",
                Response.Listener {
                    Mensajes.MostrarMensaje("¡Inmueble eliminado exitosamente!", context);
                    dialog.dismiss();
                }, Response.ErrorListener { response ->
                    Mensajes.alerta("¡Error al eliminar el inmueble!${response.message}", context);
                    dialog.dismiss();
                }){
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8";
                }
                @Throws(AuthFailureError::class)
                override fun  getHeaders():  Map<String, String> {
                    val params: HashMap<String, String> = HashMap();
                    params["Authorization"] = "Bearer " + AutorizacionService.ObtenerToken(context as Activity);
                    return params;
                };
            }
            cola.add(solicitud);
        }
    }


}