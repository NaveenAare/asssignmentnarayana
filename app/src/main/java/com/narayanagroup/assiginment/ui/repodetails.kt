package com.narayanagroup.assiginment.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.narayanagroup.assiginment.R

class repodetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repodetails)

        val Name=intent.getStringExtra("name")
        val fullName=intent.getStringExtra("fullname")
        val url=intent.getStringExtra("url")
        val des=intent.getStringExtra("des")
        val avatar_url=intent.getStringExtra("avatr_url")

        var nametxt=findViewById(R.id.name) as TextView
        nametxt.setText(fullName)
        var full=findViewById(R.id.publics) as TextView
        full.setText(Name)
        var des1=findViewById(R.id.description) as TextView
        des1.setText("Description:-"+des)
        var open=findViewById(R.id.openinbrowser) as CardView
        open.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }

        var img =findViewById(R.id.myimageview) as ImageView
        Glide.with(this)

            .load(avatar_url)

        .into(img)

    }
}