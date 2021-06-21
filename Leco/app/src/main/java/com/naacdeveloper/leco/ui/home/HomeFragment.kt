package com.naacdeveloper.leco.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.naacdeveloper.leco.Actualizar
import com.naacdeveloper.leco.Autorizacion
import com.naacdeveloper.leco.R
import com.naacdeveloper.leco.modelos.Inmueble
import com.naacdeveloper.leco.modelos.Inmuebles
import com.naacdeveloper.leco.servicios.AbrirActualizar
import com.naacdeveloper.leco.servicios.AdaptadorPersonalizado
import com.naacdeveloper.leco.servicios.InmuebleService
import com.naacdeveloper.leco.servicios.Mensajes

class HomeFragment : Fragment(), AbrirActualizar {

    var rvInmuebles: RecyclerView? = null;
    var layoutManager: RecyclerView.LayoutManager? = null;
    var root: View? = null;

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        if(InmuebleService.hayRed(root?.context as Activity)){
                rvInmuebles = root?.findViewById(R.id.rvInmuebles);
                rvInmuebles?.setHasFixedSize(true);
                layoutManager = LinearLayoutManager(root?.context)
                rvInmuebles?.layoutManager = layoutManager;
                val cola = Volley.newRequestQueue(root?.context);

                InmuebleService.obtenerInmuebles(cola,root?.context!!, rvInmuebles!!, this, null)
        }else{
            Mensajes.alerta("No hay conexion a internet.", root?.context!!);
        }

        val Refres = root?.findViewById<SwipeRefreshLayout>(R.id.Refres);
        Refres?.setOnRefreshListener {
            if(InmuebleService.hayRed(root?.context as Activity)){
                rvInmuebles = root?.findViewById(R.id.rvInmuebles);
                rvInmuebles?.setHasFixedSize(true);
                layoutManager = LinearLayoutManager(root?.context)
                rvInmuebles?.layoutManager = layoutManager;
                val cola = Volley.newRequestQueue(root?.context);

                InmuebleService.obtenerInmuebles(cola,root?.context!!, rvInmuebles!!, this,Refres)
            }else{
                Mensajes.alerta("No hay conexion a internet.", root?.context!!);
            }

        }
        return root
    }

    override fun abrirActualizar(inmueble: Inmueble) {

        val intent = Intent (root?.context, Actualizar::class.java);
        intent.putExtra("codigo", inmueble.codigo);
        intent.putExtra("nombre", inmueble.nombre);
        intent.putExtra("descripcion", inmueble.descripcion);
        intent.putExtra("direccion", inmueble.direccion);
        intent.putExtra("estado", inmueble.estado);

        val settings = root?.context?.getSharedPreferences("Imagen",0);
        val editor = settings?.edit();
        editor?.putString("Imagen", inmueble.fotos!![0].imagen);
        editor?.commit();

        startActivity(intent);
    }
}