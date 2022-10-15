package com.jokku.images

import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jokku.functionality.R
import com.squareup.picasso.Transformation


class MainActivity : AppCompatActivity() {

    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        image = findViewById(R.id.image)
        image.load("https://mfiles.alphacoders.com/896/896768.jpg")

    }

    private fun ImageView.load(url: String) {
        /*Picasso.get()
            .load(url)
            .transform(PicassoCircleTransformation())
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(this)*/

        Glide.with(applicationContext)
            .load(url)
            .optionalCircleCrop()
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(image)
    }

    class PicassoCircleTransformation : Transformation {

        override fun transform(source: Bitmap): Bitmap {
            val size = source.width.coerceAtMost(source.height)

            val x = (source.width - size) / 2
            val y = (source.height - size) / 2

            val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
            if (squaredBitmap != source) {
                source.recycle()
            }

            val bitmap = Bitmap.createBitmap(size, size, source.config)

            val canvas = Canvas(bitmap)
            val paint = Paint()
            val shader = BitmapShader(
                squaredBitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP
            )
            paint.shader = shader
            paint.isAntiAlias = true

            val r = size / 2f
            canvas.drawCircle(r, r, r, paint)

            squaredBitmap.recycle()
            return bitmap
        }

        override fun key(): String {
            return "circle"
        }

    }
}