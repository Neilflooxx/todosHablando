
import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apaza.ordoniez.todosHablando.R

class AppListAdapter(private var appList: List<ApplicationInfo>) : RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appInfo = appList[position]
        holder.bind(appInfo)
    }

    override fun getItemCount(): Int {
        return appList.size
    }

    fun updateData(newList: List<ApplicationInfo>) {
        appList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val appNameTextView: TextView = itemView.findViewById(R.id.app_name)

        fun bind(appInfo: ApplicationInfo) {
            val appName = itemView.context.packageManager.getApplicationLabel(appInfo)
            appNameTextView.text = appName
        }
    }
}
