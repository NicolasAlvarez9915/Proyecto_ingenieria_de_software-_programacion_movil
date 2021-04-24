package com.naacdeveloper.leco.modelos

class FotoInmueble (codigo: String, codInmueble: String, imagen: String ){
    var codigo: String = "";
    var codInmueble: String = "";
    var imagen: String = "";

    init {
        this.codInmueble = codInmueble;
        this.codigo = codigo;
        this.imagen = imagen;
    }
}