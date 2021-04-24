package com.naacdeveloper.leco.servicios

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class InmuebleService {

    companion object{
        fun parsearIMagenBase64(img: ImageView): String {
            val bitmap = (img.drawable as BitmapDrawable).bitmap
            if (bitmap != null) {
                val byteArrayOutputStream = ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                val byteArray: ByteArray = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
            return "";
        }

        fun parsearBase64Imagen(imgB64: String, img: ImageView): ImageView{
            val imageBytes = Base64.decode(imgB64, Base64.DEFAULT);
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size);
            img.setImageBitmap(decodedImage);
            return img;
        }
    }


}