package com.naacdeveloper.leco.servicios

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naacdeveloper.leco.R
import com.naacdeveloper.leco.modelos.Inmueble

class AdaptadorPersonalizado(context: Context, items: ArrayList<Inmueble>): RecyclerView.Adapter<AdaptadorPersonalizado.ViewHolder>() {
    var items: ArrayList<Inmueble>? = null;
    var context: Context? = null;
    init{
        this.items = items;
        this.context = context;
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorPersonalizado.ViewHolder {
        val vista = LayoutInflater.from(context).inflate(R.layout.template_inmuebles,parent,false);
        val viewHolder = ViewHolder(vista);
        return viewHolder;
    }

    override fun onBindViewHolder(holder: AdaptadorPersonalizado.ViewHolder, position: Int) {
        val item = items?.get(position);
        holder.foto = InmuebleService.parsearBase64Imagen(item?.fotos?.get(0)?.imagen!!, holder.foto!!);
        holder.nombre?.text = item.nombre;
        holder.direccion?.text = item.direccion;
        holder.descripcion?.text = item.descripcion;
    }

    override fun getItemCount(): Int {
        return this.items?.count()!!;
    }

    class ViewHolder(vista: View): RecyclerView.ViewHolder(vista){
        var vista = vista;
        var foto: ImageView? = null;
        var descripcion: TextView? = null;
        var direccion: TextView? = null;
        var nombre: TextView? = null;

        init {
            foto = vista.findViewById(R.id.ivFoto);
            descripcion = vista.findViewById(R.id.tvDescripcion);
            direccion = vista.findViewById(R.id.tvDireccion);
            nombre = vista.findViewById(R.id.tvNombre);
        }
    }

}