package com.example.discosapphgm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Define la clase MainActivity que hereda de AppCompatActivity, una clase base para actividades que usan las características de la ActionBar de Android.
class MainActivity : AppCompatActivity() {

    // Declara variables para los elementos de la interfaz de usuario y la base de datos.
    // lateinit indica que estas variables se inicializarán más tarde.
    private lateinit var etNombre: EditText
    private lateinit var etAnio: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnVerTodos: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: DatabaseHelper

    // El método onCreate se llama cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño de la interfaz de usuario definido en activity_main.xml.
        setContentView(R.layout.activity_main)

        // Inicializa las variables con los elementos de la interfaz de usuario.
        etNombre = findViewById(R.id.etNombreDisco)
        etAnio = findViewById(R.id.etAnioPublicacion)
        btnAgregar = findViewById(R.id.btnAdd)
        btnVerTodos = findViewById(R.id.btnViewAll)
        recyclerView = findViewById(R.id.rvDiscos)
        // Inicializa el controlador de la base de datos.
        dbHandler = DatabaseHelper(this)

        // Configura los eventos de clic para los botones.
        btnAgregar.setOnClickListener { addDisco() }
        btnVerTodos.setOnClickListener { viewDiscos() }

        // Muestra la lista de discos al iniciar la actividad.
        viewDiscos()
    }

    // Método para agregar un nuevo disco a la base de datos.
    private fun addDisco() {
        // Obtiene el texto de los EditText y lo convierte en String.
        val nombre = etNombre.text.toString()
        val anio = etAnio.text.toString()
        // Verifica que los campos no estén vacíos.
        if (nombre.isNotEmpty() && anio.isNotEmpty()) {
            // Crea un objeto Disco y lo añade a la base de datos.
            val disco = Disco(nombre = nombre, anio = anio.toInt())
            val status = dbHandler.addDisco(disco)
            // Verifica si la inserción fue exitosa.
            if (status > -1) {
                Toast.makeText(applicationContext, "Disco agregado", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de discos.
                clearEditTexts()
                viewDiscos()
            }
        } else {
            // Muestra un mensaje si los campos están vacíos.
            Toast.makeText(applicationContext, "Nombre y Año son requeridos", Toast.LENGTH_LONG).show()
        }
    }

    // Método para mostrar todos los discos en el RecyclerView.
    private fun viewDiscos() {
        // Obtiene la lista de discos de la base de datos.
        val discosList = dbHandler.getAllDiscos()
        // Crea un adaptador para el RecyclerView y lo configura.
        val adapter = DiscosAdapter(discosList,
            onEdit = { disco -> editDisco(disco) },
            onDelete = { disco -> deleteDisco(disco) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    // Método para limpiar los campos de texto.
    private fun clearEditTexts() {
        etNombre.text.clear()
        etAnio.text.clear()
    }

    // Método para editar un disco
    private fun editDisco(disco: Disco) {
        etNombre.setText(disco.nombre)
        etAnio.setText(disco.anio.toString())

        btnAgregar.text = "Actualizar" // Cambia el texto del botón

        btnAgregar.setOnClickListener {
            val nuevoNombre = etNombre.text.toString()
            val nuevoAnio = etAnio.text.toString()

            if (nuevoNombre.isNotEmpty() && nuevoAnio.isNotEmpty()) {
                val discoActualizado = Disco(id = disco.id, nombre = nuevoNombre, anio = nuevoAnio.toInt())
                val status = dbHandler.updateDisco(discoActualizado)
                if (status > -1) {
                    Toast.makeText(this, "Disco actualizado", Toast.LENGTH_SHORT).show()
                    clearEditTexts()
                    viewDiscos()
                    btnAgregar.text = "Agregar" // Vuelve al estado original
                    btnAgregar.setOnClickListener { addDisco() }
                }
            } else {
                Toast.makeText(this, "Nombre y Año son requeridos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para eliminar un disco
    private fun deleteDisco(disco: Disco) {
        val status = dbHandler.deleteDisco(disco)
        if (status > 0) {
            Toast.makeText(this, "Disco eliminado", Toast.LENGTH_SHORT).show()
            viewDiscos()
        } else {
            Toast.makeText(this, "Error al eliminar disco", Toast.LENGTH_SHORT).show()
        }
    }
}