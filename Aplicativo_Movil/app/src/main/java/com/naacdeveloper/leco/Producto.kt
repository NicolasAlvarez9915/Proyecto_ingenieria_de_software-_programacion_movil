package com.naacdeveloper.leco

class Producto (codigo: String, categoria: String, nombre: String, cantidad: Int, cantidadMinima: Int, descripcion: String, valor: Int){

    var codigo: String = "";
    var categoria: String = "";
    var nombre: String = "";
    var cantidad: Int = 0;
    var cantidadMinima: Int = 0;
    var descripcion: String = "";
    var valor: Int = 0;

    init{
      this.cantidad = cantidad;
      this.cantidadMinima = cantidadMinima;
        this.categoria = categoria;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.valor = valor;
    }
}