package com.naacdeveloper.leco

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.naacdeveloper.leco.ui.home.HomeFragment
import okhttp3.Call
import okhttp3.OkHttpClient
import org.w3c.dom.Text
import java.io.IOException
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), HomeFragment.ComprobarRed {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }




        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun HayRed(): Boolean {
        super.HayRed()
        return Network.hayRed(this);
    }


    //Metodo get OkHttp3

    override fun solicitud(url: String) {
        val cliente = OkHttpClient();
        val solicitud = okhttp3.Request.Builder().url(url).build();
        cliente.newCall(solicitud).enqueue(object: okhttp3.Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                //implementar error
            }

            override fun onResponse(call: Call?, response: okhttp3.Response) {
                val result = response.body().string();

               this@MainActivity.runOnUiThread {
                   try{
                       Toast.makeText(this@MainActivity, result, Toast.LENGTH_LONG).show();
                       Log.d("SolicitudVolley",result);
                   }catch (e: Exception){

                   }
               }

            }
        });
    }


}