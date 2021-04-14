package com.naacdeveloper.leco.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.naacdeveloper.leco.R

class HomeFragment : Fragment() {

    var button: Button? = null;
    var nombre: EditText? = null;
    var root: View? = null;
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_home, container, false)
        button = root?.findViewById(R.id.button);
        nombre = root?.findViewById(R.id.editTextTextPersonName);
        button?.setOnClickListener {
            Toast.makeText(root?.context, nombre?.text, Toast.LENGTH_LONG).show();
        }
        return root
    }

}