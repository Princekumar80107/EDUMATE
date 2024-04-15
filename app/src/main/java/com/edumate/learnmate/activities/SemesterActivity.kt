package com.edumate.learnmate.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edumate.learnmate.R
import com.edumate.learnmate.adapters.SemesterAdapter
import com.edumate.learnmate.models.Semester
import com.google.firebase.firestore.FirebaseFirestore

class SemesterActivity : AppCompatActivity() {

    private lateinit var adapter: SemesterAdapter
    private var semesterList = mutableListOf<Semester>()
    private lateinit var firestore: FirebaseFirestore
    private var department: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_semester)
        firestore = FirebaseFirestore.getInstance()
        department = intent.getStringExtra("TITLE")

        setUpViews()
    }

    private fun setUpViews() {
        setUpToolbar()
        setUpRecyclerView()
        setUpSemesterList()
    }

    private fun setUpToolbar() {
        val semesterTitle = findViewById<com.google.android.material.textview.MaterialTextView>(R.id.semesterTitle)
        val backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        semesterTitle.text = department

        backArrowImg.setOnClickListener {
            finish()
        }
    }

    private fun setUpSemesterList() {
        val department = intent.getStringExtra("TITLE")
        if (department != null) {
            val collectionReference = firestore.collection(department)

            collectionReference.addSnapshotListener { value, error ->
                if (value == null || error != null) {
                    Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                semesterList.clear()
                semesterList.addAll(value.toObjects(Semester::class.java))
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setUpRecyclerView() {
        val semesterRecyclerView = findViewById<RecyclerView>(R.id.semesterRecyclerView)
        adapter = SemesterAdapter(this, semesterList)
        semesterRecyclerView.layoutManager = LinearLayoutManager(this)
        semesterRecyclerView.adapter = adapter
    }
}