package com.naacdeveloper.leco.servicios

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.naacdeveloper.leco.R

class Mensajes {
    companion object{
        fun alerta(Mensaje:String, context: Context){
            val mAlertDialog = AlertDialog.Builder(context)
            mAlertDialog.setIcon(R.mipmap.ic_launcher_round)
            mAlertDialog.setTitle("Alerta!")
            mAlertDialog.setMessage(Mensaje)
            mAlertDialog.setNegativeButton("Ok") { dialog, id ->
                Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show()
            }
            mAlertDialog.show()
        }

        fun  MostrarMensaje(Mensaje:String, context: Context){
            val mAlertDialog = AlertDialog.Builder(context)
            mAlertDialog.setIcon(R.mipmap.ic_launcher) //Icono de la alerta
            mAlertDialog.setTitle("En hora buena!") //Titulo de la alerta
            mAlertDialog.setMessage(Mensaje) //Mensaje de la alerta
            mAlertDialog.setNegativeButton("Ok") { dialog, id ->
                Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show()
            }
            mAlertDialog.show();
        }
    }
}