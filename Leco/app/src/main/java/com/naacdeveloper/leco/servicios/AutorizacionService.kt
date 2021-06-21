package com.naacdeveloper.leco.servicios

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.naacdeveloper.leco.modelos.AutorizacionPeticion
import com.naacdeveloper.leco.modelos.AutorizacionRespuesta
import com.naacdeveloper.leco.modelos.Inmueble

class AutorizacionService {
    companion object{
        val baseUrl:String = "http://192.168.0.65:8081/";
        fun ObtenerToken(context: Context, AutorizacionPeticion: AutorizacionPeticion){
            val cola = Volley.newRequestQueue(context);
            var dialog = ProgressDialog.show(
                context,
                "Validando frase secreta.",
                "Espere un momento...",
                true
            );

            var gson = Gson();
            val requestBody = gson.toJson(AutorizacionPeticion);

            val solicitud = object : StringRequest(
                Request.Method.POST,
                baseUrl + "api/Autorizacion",
                Response.Listener {response ->
                    dialog.dismiss();
                    val gson = Gson();
                    var AutorizacionRespuesta = gson.fromJson(
                        response,
                        AutorizacionRespuesta::class.java
                    )
                    Mensajes.alerta(AutorizacionRespuesta.token, context);
                    guardarToken(context as Activity,AutorizacionRespuesta.token);
                }, Response.ErrorListener { response ->
                    Mensajes.alerta("Frase incorecta", context);
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

        fun guardarToken(activity: Activity, token: String){
            val settings = activity.getSharedPreferences("Token",0);
            val editor = settings.edit();
            editor.putString("Token", token);
            editor.commit();
        }

        fun ObtenerToken(activity: Activity):String{
            val settings = activity.getSharedPreferences("Token",0);
            val token = settings.getString("Token", "");
            return token!!;
        }

    }
}