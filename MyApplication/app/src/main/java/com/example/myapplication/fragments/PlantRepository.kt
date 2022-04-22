package com.example.myapplication.fragments

import android.net.Uri
import com.example.myapplication.PlantModel
import com.example.myapplication.fragments.PlantRepository.Singelton.databaseRef
import com.example.myapplication.fragments.PlantRepository.Singelton.downloadUri
import com.example.myapplication.fragments.PlantRepository.Singelton.plantList
import com.example.myapplication.fragments.PlantRepository.Singelton.storageReference
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.*

class PlantRepository {
    object Singelton {
        //donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://naturecollection-c62f6.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)



        // se connecter à la ref plante
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // creer une liste qui contient les plantes
        val plantList = arrayListOf<PlantModel>()

        //contenir le lien de l'image courante
        var downloadUri : Uri? = null
    }
    fun updateData(callback: () -> Unit) {
        //absorber les données dans la database ref -> liste de plant
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                // retirer les ancinnes
                plantList.clear()
                //recolter la liste
                for (ds in p0.children) {
                    //construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)
                    // verifier plant pas nul
                    if(plant != null ) {
                        //ajout plante à autre liste
                        plantList.add(plant)
                    }
                }
                // actionner callback
                callback()
            }

        } )
    }

    //creer une fn pour envoyer des fichiers sur les storage
    fun uploadImage(file : Uri, callback: () -> Unit) {
        //verifier que ce fichier nest pas null
        if (file != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)
            // demarrer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>
            { task ->
                //si il ya eu un probelem lors de l'envoi du fichier
                if(!task.isSuccessful) {
                    task.exception?.let { throw  it }
                }
                return@Continuation ref.downloadUrl
            }
            ).addOnCompleteListener { task ->
                //verifier si tout a bien fonctionné
                if ( task.isSuccessful ) {
                    //recuperer l'image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    //mettre à jour un objet plant en bdd
    fun updatePlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    //inserer une nouvelle plante en bd
    fun insertPlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    //supprimer plante de bd
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()


}