package apaza.ordoniez.todosHablando.firstApp

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import apaza.ordoniez.todosHablando.R

class AppDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_detail)

        val appName = intent.getStringExtra("APP_NAME")
        val appIconBitmap = intent.getParcelableExtra<Bitmap>("APP_ICON")

        val appNameTextView = findViewById<TextView>(R.id.app_name)
        val appIconImageView = findViewById<ImageView>(R.id.app_icon)

        appNameTextView.text = appName
        appIconBitmap?.let {
            appIconImageView.setImageBitmap(it)
        }
    }
}
