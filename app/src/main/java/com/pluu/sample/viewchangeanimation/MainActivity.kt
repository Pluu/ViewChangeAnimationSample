package com.pluu.sample.viewchangeanimation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.PathInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import com.pluu.sample.viewchangeanimation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var flag = true

    private val translationX by lazy {
        dp2px(60).toFloat()
    }

    private val scale = 0.9f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnChange.setOnClickListener {
            AnimatorSet().apply {
//                interpolator = DecelerateInterpolator()
//                interpolator = PathInterpolator(0.8f, 0f, 0.35f, 1f)
                interpolator = PathInterpolator(0.25f, 0.1f, 0.25f, 1f)
                val left = if (flag) binding.iv1 else binding.iv2
                val right = if (flag) binding.iv2 else binding.iv1
                playTogether(
                    leftToRight(left),
                    rightToLeft(right)
                )
                duration = 250
                doOnStart {
                    flag = !flag
                }
            }.start()
        }
    }

    private fun leftToRight(view: View): Animator {
        view.pivotX = 0f
        val rightReadAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, scale),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, scale),
                ObjectAnimator.ofFloat(
                    view,
                    View.TRANSLATION_X,
                    -translationX
                )
            )
            doOnEnd {
                view.translationZ = 0f
            }
        }

        val moveToLeftAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f),
                ObjectAnimator.ofFloat(
                    view,
                    View.TRANSLATION_X,
                    dp2px(30).toFloat()
                )
            )
        }

        return AnimatorSet().apply {
            playSequentially(rightReadAnimator, moveToLeftAnimator)
        }
    }

    private fun rightToLeft(view: View): Animator {
        view.pivotX = dp2px(100).toFloat()

        val rightReadAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, scale),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, scale),
                ObjectAnimator.ofFloat(
                    view,
                    View.TRANSLATION_X,
                    translationX
                )
            )
            doOnEnd {
                view.translationZ = 100f
            }
        }

        val moveToLeftAnimator = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f),
                ObjectAnimator.ofFloat(
                    view,
                    View.TRANSLATION_X,
                    dp2px(-30).toFloat()
                )
            )
        }

        return AnimatorSet().apply {
            playSequentially(rightReadAnimator, moveToLeftAnimator)
        }
    }
}

fun Context.dp2px(dpValue: Int): Int {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}
