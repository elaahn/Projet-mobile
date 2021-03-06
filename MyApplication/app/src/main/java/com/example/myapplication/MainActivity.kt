package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.fragments.AddPlantFragment
import com.example.myapplication.fragments.CollectionFragment
import com.example.myapplication.fragments.HomeFragment
import com.example.myapplication.fragments.PlantRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //importer bottom
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)

        loadFragment(HomeFragment(this), R.string.home_page_title )

        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.home_page -> {
                    loadFragment(HomeFragment(this), R.string.home_page_title )
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this), R.string.Collection_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_plant_page -> {
                    loadFragment(AddPlantFragment(this), R.string.add_plant_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, string: Int) {
        //charger notre PlantRepository
        val repo = PlantRepository()

        //actualiser titre
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        //mettre à jour la liste de plantes
        repo.updateData {
            //injecter le fragment dans notre boite ( fragment_container)
            val transaction=supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
