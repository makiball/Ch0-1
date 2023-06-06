package kr.co.toplink.ch0_1

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.color.utilities.MaterialDynamicColors.background
import kr.co.toplink.ch0_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityMainBinding


    /* 버튼 선언 */
    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button
    lateinit var animpanel : FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "에니메이션 기초"


        star = binding.star
        rotateButton = binding.rotateButton
        translateButton = binding.translateButton
        scaleButton = binding.scaleButton
        fadeButton = binding.sequentialButton
        colorizeButton = binding.colorizeButton
        showerButton = binding.showerButton
        animpanel = binding.animpanel


        /* 별클릭시 별 크기와 좌표를 구하기 */
        star.setOnClickListener{
            val width = star.width
            val height = star.height
            val posX = star.x
            val posY = star.y

            Toast.makeText(
                applicationContext,
                "Start width: $width, height: $height, posX: $posX, posY: $posY",
                Toast.LENGTH_SHORT
            ).show()
        }

        /* 회전 */
        rotateButton.setOnClickListener {
            rotater()
        }

        /* 이동 */
        translateButton.setOnClickListener {
            translater()
        }

        /* 크기 변환 */
        scaleButton.setOnClickListener {
            scaler()
        }

        /* 페이드 효과 점점 나타내기, 점점 없어지기 */
        fadeButton.setOnClickListener {
            fader()
        }

        /* 색상변경 */
        colorizeButton.setOnClickListener {
            colorizer()
        }

        /* 별떨어지기 */
        showerButton.setOnClickListener {
            shower()
        }
    }


    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(
            star, View.ROTATION,
            -360f, 0f
        )
        animator.duration = 1000
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(
            star, View.TRANSLATION_X,
            500f
        )
        animator.duration = 3000
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(translateButton)
        animator.start()
    }


    private fun scaler() {

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            star, scaleX, scaleY
        )
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()

    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(fadeButton)
        animator.start()
    }

    private fun colorizer() {
        val container = star.parent as ViewGroup
        val animator = ObjectAnimator.ofArgb(
            container,
            "backgroundColor", Color.BLACK, Color.RED
        )
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(colorizeButton)
        animator.start()
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = star.width.toFloat()
        var starH: Float = star.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newStar)

        // Set width and height of new star
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY

        // Set x start position for new star
        newStar.translationX = Math.random().toFloat() *
                containerW - starW / 2

        // Set falling and rotation animation
        val mover = ObjectAnimator.ofFloat(
            newStar, View.TRANSLATION_Y,
            -starH, containerH + starH
        )
        mover.interpolator = AccelerateInterpolator(1f)
        val rotator = ObjectAnimator.ofFloat(
            newStar, View.ROTATION,
            (Math.random() * 1080).toFloat()
        )
        rotator.interpolator = LinearInterpolator()

        // Create animation sets
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()


        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                container.removeView(newStar)
            }
        })
        set.start()
    }
}


/* 에니메이션구동 기간 버튼 활성화 켜기 / 끄기 */
fun ObjectAnimator.disableViewDuringAnimation(view: View) {

    addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            view.isEnabled = false
        }

        override fun onAnimationEnd(animation: Animator) {
            view.isEnabled = true
        }
    })
}