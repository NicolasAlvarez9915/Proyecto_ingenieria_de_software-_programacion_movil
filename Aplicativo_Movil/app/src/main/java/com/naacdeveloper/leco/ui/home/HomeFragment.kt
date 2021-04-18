package com.naacdeveloper.leco.ui.home

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.naacdeveloper.leco.*
import org.json.JSONObject

class HomeFragment : Fragment(), CompletadoListener {

    var btnComprobarRed: Button? = null;
    var btnSolicitudHttp : Button? = null;
    var btnSolicitudVolley: Button? = null;
    var btnSolicutudOkHttp: Button? = null;
    var btnPoatVolley: Button? = null;
    var root: View? = null;
    var listener: ComprobarRed? = null;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)

        btnComprobarRed = root?.findViewById(R.id.btnComprobarRed);
        btnComprobarRed?.setOnClickListener {
            if(listener?.HayRed() == true){
                Toast.makeText(root?.context, "Si hay red", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(root?.context, "No hay red", Toast.LENGTH_LONG).show();
            }
        }

        btnSolicitudHttp = root?.findViewById(R.id.btnSolicitdHttp);
        btnSolicitudHttp?.setOnClickListener {
            if(listener?.HayRed() == true){

                PeticionesHtml(this).execute("https://distribuidoraesb.azurewebsites.net/api/Producto");
            }else{
                Toast.makeText(root?.context, "No hay red", Toast.LENGTH_LONG).show();
            }
        }

        btnSolicitudVolley = root?.findViewById(R.id.btnVolley);
        btnSolicitudVolley?.setOnClickListener {
            if(listener?.HayRed() == true){
                solicitudHttpVolley("https://distribuidoraesb.azurewebsites.net/api/Producto");
            }else{
                Toast.makeText(root?.context, "No hay red", Toast.LENGTH_LONG).show();
            }
        }

        btnSolicutudOkHttp = root?.findViewById(R.id.btnSolicitudOkHttp);
        btnSolicutudOkHttp?.setOnClickListener {
            if(listener?.HayRed() == true){
                listener?.solicitud("https://distribuidoraesb.azurewebsites.net/api/Producto");
            }else{
                Toast.makeText(root?.context, "No hay red", Toast.LENGTH_LONG).show();
            }
        }

        btnPoatVolley = root?.findViewById(R.id.btnPostVolley);
        btnPoatVolley?.setOnClickListener {
            if(listener?.HayRed() == true){
                solicitudPostVolley();
            }else{
                Toast.makeText(root?.context, "No hay red", Toast.LENGTH_LONG).show();
            }
        }
        return root
    }

    //Metodo GET volley

    private fun solicitudHttpVolley(url: String){
        val cola = Volley.newRequestQueue(root?.context)

        val solicitud = StringRequest(
            Request.Method.GET,
            url,
            Response.Listener<String> { response ->
                try {
                    var productosJson = "{ \"productos\":$response}"

                    Toast.makeText(root?.context, productosJson, Toast.LENGTH_LONG).show();
                    Log.d("SolicitudVolley", response);

                    val gson = Gson();
                    val res = gson.fromJson(productosJson, Productos::class.java);
                    Toast.makeText(
                        root?.context,
                        res.productos?.count().toString(),
                        Toast.LENGTH_LONG
                    ).show();
                } catch (e: Exception) {

                }
            },
            Response.ErrorListener { })

        cola.add(solicitud);
    }

    //Metodo Post volley

    private fun solicitudPostVolley(){
        val url = "https://distribuidoraesb.azurewebsites.net/api/Usuario";
        val cola = Volley.newRequestQueue(root?.context);

        var dialog = ProgressDialog.show(root?.context, "", "Espere un momento...", true);


        val jsonBody = JSONObject()
        jsonBody.put("correo", "144")
        jsonBody.put("idpersona", "1")
        jsonBody.put("contraseña", "1")
        jsonBody.put("rol", "1")

        val requestBody = jsonBody.toString()

        val solicitud: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(root?.context, response, Toast.LENGTH_LONG).show();
                Log.d("SolicitudPostVolley", response);
                dialog.dismiss();
            }, Response.ErrorListener {
                    response ->
                Toast.makeText(root?.context, "datos incorrectos", Toast.LENGTH_LONG).show();
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


    interface  ComprobarRed{
        fun HayRed(): Boolean{
            return false
        }
        fun solicitud(url: String){}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as ComprobarRed
        }catch (e: ClassCastException){
            throw  ClassCastException(context.toString() + "Implementar la interfaz");
        }
    }

    override fun descargaCompleta(resultado: String) {
        Toast.makeText(root?.context, resultado, Toast.LENGTH_LONG).show();
        Log.d("Solicitud", resultado);
    }

}