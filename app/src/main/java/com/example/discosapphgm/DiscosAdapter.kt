package com.example.discosapphgm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define la clase DiscosAdapter, que extiende RecyclerView.Adapter y especifica MyViewHolder como su ViewHolder.
class DiscosAdapter(private val discosList: List<Disco>, private val onEdit: (Disco) -> Unit, private val onDelete: (Disco) -> Unit) : RecyclerView.Adapter<DiscosAdapter.MyViewHolder>() {

    // Define la clase interna MyViewHolder, que extiende RecyclerView.ViewHolder.
    // Esta clase proporciona una referencia a las vistas de cada elemento de datos.
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Encuentra y almacena referencias a los elementos de la interfaz de usuario en el layout del ítem.
        var nombreDisco: TextView = view.findViewById(R.id.tvNombreDisco)
        var anioPublicacion: TextView = view.findViewById(R.id.tvAnioPublicacion)
        var btnEditar: Button = view.findViewById(R.id.btnEditar)
        var btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    // Crea nuevas vistas (invocadas por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Infla el layout del ítem de la lista (disco_item.xml) y lo pasa al constructor de MyViewHolder.
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.disco_item, parent, false)
        return MyViewHolder(itemView)
    }

    // Reemplaza el contenido de una vista (invocada por el layout manager).
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // Obtiene el elemento de la lista de discos en esta posición.
        val disco = discosList[position]
        // Reemplaza el contenido de las vistas con los datos del elemento en cuestión.
        holder.nombreDisco.text = disco.nombre
        holder.anioPublicacion.text = disco.anio.toString()

        // Configura los botones de editar y eliminar
        holder.btnEditar.setOnClickListener { onEdit(disco) }
        holder.btnEliminar.setOnClickListener { onDelete(disco) }
    }

    // Devuelve el tamaño de la lista de datos (invocado por el layout manager).
    override fun getItemCount() = discosList.size
}