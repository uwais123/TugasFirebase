package com.masuwes.tugasfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.masuwes.tugasfirebase.databinding.ActivityMainBinding
import com.masuwes.tugasfirebase.model.ModelData
import com.masuwes.tugasfirebase.recyclerview.ItemDataAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterMain: ItemDataAdapter

    private lateinit var databaseUser: DatabaseReference

    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater
        binding = ActivityMainBinding.inflate(inflater)
        setContentView(binding.root)

        adapterMain = ItemDataAdapter()

        binding.extendedFab.setOnClickListener {
            startActivity(Intent(this, AddDataActivity::class.java))
        }

        binding.rvMain.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterMain
            setHasFixedSize(true)
        }


        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val daftarUser = arrayListOf<ModelData>()

                if (snapshot.childrenCount > 0) {
                    for (dataUser in snapshot.children) {
                        val data = dataUser.getValue(ModelData::class.java) as ModelData
                        daftarUser.add(data)
                    }
                    adapterMain.addData(daftarUser)
                } else {
                    adapterMain.addData(daftarUser)
                }
                adapterMain.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }

        databaseUser = FirebaseDatabase.getInstance().reference.child("Users")
        databaseUser.addValueEventListener(valueEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        // maka perlu dihapus dengan cara removeEventListener
        // jika penambahan terjadi di oncreate
        // maka hapusnya itu ada di onDestroy seperti kode di bawah ini
        databaseUser.removeEventListener(valueEventListener)
    }
}























