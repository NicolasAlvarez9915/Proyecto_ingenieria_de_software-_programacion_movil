package com.naacdeveloper.leco.ui.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.naacdeveloper.leco.R
import com.naacdeveloper.leco.modelos.Inmueble
import com.naacdeveloper.leco.servicios.InmuebleService

class ActualizarInmuebleFragment : Fragment() {

    var root: View? = null
    var btnActualizar: Button? = null

    var etNombre: EditText? = null
    var etDireccion: EditText? = null
    var etDescripcion: EditText? = null
    var etEstado: EditText? = null

    var inmueble: Inmueble? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_actualizar_inmueble, container, false)

        inicializarVariables()

        btnActualizar?.setOnClickListener {
            actualizarInmueble()
        }

        return root
    }

    private fun actualizarInmueble(){
        inmueble?.estado = etEstado?.text.toString();
        inmueble?.direccion = etDireccion?.text.toString();
        inmueble?.nombre = etNombre?.text.toString();
        inmueble?.descripcion = etDescripcion?.text.toString();
        InmuebleService.actualizarInmueble(root?.context!!,inmueble!!)
    }

    private fun inicializarVariables(){
        etNombre = root?.findViewById(R.id.etNombreAct)
        etDireccion = root?.findViewById(R.id.etDirecionAct)
        etDescripcion = root?.findViewById(R.id.etDescripcionAct)
        etEstado = root?.findViewById(R.id.etEstadoAct)

        inmueble = Inmueble("Defecto","Defecto","Defecto","Defecto","Defecto", ArrayList());
    }
}