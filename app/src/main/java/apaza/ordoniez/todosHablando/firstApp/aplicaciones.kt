import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apaza.ordoniez.todosHablando.R
import java.util.concurrent.TimeUnit

class aplicaciones : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppListAdapter

    private val handler = Handler()
    private val refreshInterval = TimeUnit.MINUTES.toMillis(1) // Actualizar cada 1 minuto

    private val refreshRunnable = object : Runnable {
        override fun run() {
            // Actualizar la lista de aplicaciones
            updateAppList()
            // Programar la siguiente actualización
            handler.postDelayed(this, refreshInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Verificar si el permiso está otorgado
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.PACKAGE_USAGE_STATS)
            != PackageManager.PERMISSION_GRANTED) {
            // Si no está otorgado, solicitarlo al usuario
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.PACKAGE_USAGE_STATS),
                PERMISSION_REQUEST_USAGE_STATS
            )
        } else {
            // Si el permiso ya está otorgado, inicializar el RecyclerView y actualizar la lista de aplicaciones
            initRecyclerView()
            updateAppList()
        }
    }

    override fun onResume() {
        super.onResume()
        // Comenzar la actualización periódica cuando la actividad esté en primer plano
        handler.post(refreshRunnable)
    }

    override fun onPause() {
        super.onPause()
        // Detener la actualización periódica cuando la actividad esté en segundo plano
        handler.removeCallbacks(refreshRunnable)
    }

    private fun initRecyclerView() {
        adapter = AppListAdapter(emptyList()) // Inicializar el adaptador con una lista vacía
        recyclerView.adapter = adapter
    }

    private fun updateAppList() {
        // Obtener todas las aplicaciones instaladas en el dispositivo
        val installedApps = getInstalledApps(this)
        // Actualizar el conjunto de datos del adaptador
        adapter.updateData(installedApps)
    }

    private fun getInstalledApps(context: Context): List<ApplicationInfo> {
        // Obtener el PackageManager
        val packageManager = context.packageManager

        // Verificar la versión de Android y los permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (context.checkSelfPermission(Manifest.permission.QUERY_ALL_PACKAGES) == PackageManager.PERMISSION_GRANTED) {
                // Obtener una lista de todas las aplicaciones instaladas en el dispositivo
                return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            } else {
                // Manejar la falta de permisos apropiadamente
                throw SecurityException("Permission QUERY_ALL_PACKAGES is required.")
            }
        } else {
            // Para versiones anteriores a Android 11
            return packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        }
    }

    private fun requestUsageStatsPermission() {
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_USAGE_STATS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, inicializar el RecyclerView y actualizar la lista de aplicaciones
                initRecyclerView()
                updateAppList()
            } else {
                // Permiso denegado, solicitar nuevamente o mostrar un mensaje al usuario
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_USAGE_STATS = 100
    }
}
