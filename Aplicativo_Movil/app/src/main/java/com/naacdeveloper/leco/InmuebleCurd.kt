package com.naacdeveloper.leco

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject

class InmuebleCurd {

    var fotoInmueble: FotoInmueble? = null;

    fun obtenerFotoInmueble(url: String, context: Context){
        val cola = Volley.newRequestQueue(context)

        var dialog = ProgressDialog.show(context, "", "Espere un momento...", true);
        val solicitud = StringRequest(
                Request.Method.GET,
                url,
                Response.Listener<String> { response ->
                    try {

                        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        Log.d("SolicitudVolley", response);
                        val gson = Gson();

                        fotoInmueble = null;
                        fotoInmueble = gson.fromJson(response, FotoInmueble::class.java);

                        dialog.dismiss();
                    } catch (e: Exception) {

                    }
                },
                Response.ErrorListener {
                    dialog.dismiss(); })

        cola.add(solicitud);
    }

    fun subirFoto(url: String, context: Context, fotoInmueble: FotoInmueble){
        val cola = Volley.newRequestQueue(context);

        var dialog = ProgressDialog.show(context, "", "Espere un momento...", true);


        val jsonBody = JSONObject()
        jsonBody.put("codigo", fotoInmueble.codigo)
        jsonBody.put("codInmueble", fotoInmueble.codInmueble)
        jsonBody.put("imagen", fotoInmueble.imagen)

        val requestBody = jsonBody.toString()

        val solicitud: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    Log.d("SolicitudPostVolley", response);
                    dialog.dismiss();
                }, Response.ErrorListener {
            response ->
            Toast.makeText(context, "datos incorrectos", Toast.LENGTH_LONG).show();
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