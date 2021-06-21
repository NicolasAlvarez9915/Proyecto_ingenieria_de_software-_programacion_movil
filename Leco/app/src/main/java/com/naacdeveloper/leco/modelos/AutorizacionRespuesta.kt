package com.naacdeveloper.leco.modelos

class AutorizacionRespuesta(fraseSecreta: String, token: String) {
    var fraseSecreta: String = "";
    var token: String = "";
    init{
        this.fraseSecreta = fraseSecreta;
        this.token = token;
    }
}