package apaza.ordoniez.todosHablando.firstApp
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import apaza.ordoniez.todosHablando.R

class AppListAdapter1(context: Context, apps: List<ApplicationInfo>, private val packageManager: PackageManager)
    : ArrayAdapter<ApplicationInfo>(context, 0, apps) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val app = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_app, parent, false)

        val appIcon = view.findViewById<ImageView>(R.id.app_icon)
        val appName = view.findViewById<TextView>(R.id.app_name)
        val buttonCreate = view.findViewById<Button>(R.id.button_create)

        app?.let {
            val iconDrawable: Drawable = it.loadIcon(packageManager)
            val iconBitmap: Bitmap = iconDrawable.toBitmap()
            appIcon.setImageDrawable(iconDrawable)
            appName.text = packageManager.getApplicationLabel(it)

            buttonCreate.setOnClickListener {
                val intent = Intent(context,  AppDetailActivity::class.java).apply {
                    putExtra("APP_NAME", packageManager.getApplicationLabel(app))
                    putExtra("APP_ICON", iconBitmap)
                }
                context.startActivity(intent)
            }
        }

        return view
    }
}

// Helper function to convert Drawable to Bitmap
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }

    val bitmap = Bitmap.createBitmap(
        intrinsicWidth.takeIf { it > 0 } ?: 1,
        intrinsicHeight.takeIf { it > 0 } ?: 1,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    setBounds(0, 0, canvas.width, canvas.height)
    draw(canvas)
    return bitmap
}
