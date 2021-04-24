package com.naacdeveloper.leco.modelos

class Inmueble(codigo: String, nombre: String, estado: String, direccion: String, descripcion: String, fotos: ArrayList<FotoInmueble>?) {
    var codigo: String = "";
    var nombre: String = "";
    var estado: String = "";
    var direccion: String = "";
    var descripcion: String = "";
    var fotos: ArrayList<FotoInmueble>? = null;

    init{
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.estado = estado;
        this.fotos = fotos;
    }
}