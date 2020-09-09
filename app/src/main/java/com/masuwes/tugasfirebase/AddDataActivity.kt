package com.masuwes.tugasfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.masuwes.tugasfirebase.databinding.ActivityAddDataBinding
import com.masuwes.tugasfirebase.model.ModelData
import com.theartofdev.edmodo.cropper.CropImage

class AddDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDataBinding
    private var userData: ModelData? = null

    private lateinit var databaseRef: DatabaseReference
    private lateinit var firebaseStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = layoutInflater
        binding = ActivityAddDataBinding.inflate(inflater)
        setContentView(binding.root)

        databaseRef = FirebaseDatabase.getInstance().reference

        firebaseStorage = FirebaseStorage.getInstance().reference.child("Profile Photo")

        userData = intent.getParcelableExtra("DATA")

        userData?.let { dataUser ->
            binding.run {
                btnDelete.visibility = View.VISIBLE
                btnChoosePhoto.visibility = View.VISIBLE
                imgProfile.visibility = View.VISIBLE

                edtName.text = SpannableStringBuilder(dataUser.profile_name)
                edtAddress.text = SpannableStringBuilder(dataUser.profile_address)
                edtClass.text = SpannableStringBuilder(dataUser.profile_class)
                Glide.with(this@AddDataActivity)
                    .load(dataUser.profile_image)
                    .placeholder(R.drawable.ic_person)
                    .into(imgProfile)

                // setonclick untuk button delete
                btnDelete.setOnClickListener {
                    deleteData(dataUser)
                }

                // setonclick untuk button pilih gambar
                binding.btnChoosePhoto.setOnClickListener {
                    CropImage.activity().start(this@AddDataActivity)
                }
            }
        }

        // setonclick button save
        binding.btnSave.setOnClickListener {
            binding.run {
                val namaUser = edtName.text.toString()
                val kelasUser = edtClass.text.toString()
                val alamatUser = edtAddress.text.toString()

                val peringatan = when {
                    namaUser.isBlank() -> "Nama User Tidak Boleh Kosong !"
                    kelasUser.isBlank() -> "Kelas User Tidak Boleh Kosong !"
                    alamatUser.isBlank() -> "Alamat User Tidak Boleh Kosong !"
                    else -> ""
                }

                if (peringatan.isBlank()) {
                    val dataUser = ModelData(
                        userData?.profile_image ?: "",
                        namaUser,
                        kelasUser,
                        alamatUser
                    )
                    saveData(dataUser)
                } else {
                    Toast.makeText(this@AddDataActivity, peringatan, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // setonclick button cancel
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveData(userData: ModelData) {
        val userDB = databaseRef.child("Users")
            .child(userData.profile_name)
            .setValue(userData)

        userDB.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "User Telah Diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        userDB.addOnFailureListener { error ->
            Toast.makeText(this, "$error", Toast.LENGTH_SHORT).show()
        }
    }


    private fun deleteData(userData: ModelData) {
        val userDB = databaseRef.child("Users")
            .child(userData.profile_name)
            .removeValue()
        userDB.addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "User Telah Dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val resultUriImage = CropImage.getActivityResult(data).uri
            val fileRef =
                firebaseStorage.child(userData?.profile_name + ".jpg")
            val uploadImage = fileRef.putFile(resultUriImage)
            // https://firebase.google.com/docs/storage/android/upload-files?hl=id#get_a_download_url
            uploadImage.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { error ->
                        Log.e("Gagal Upload", error.toString())
                    }
                }
                fileRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    userData?.let {
                        it.profile_image = downloadUri.toString()
                        Glide.with(this)
                            .load(it.profile_image)
                            .into(binding.imgProfile)
                    }
                } else {
                    Toast.makeText(this, "Gagal Upload Foto Profil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

















