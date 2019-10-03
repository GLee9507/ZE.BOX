package com.gene.zebox.defect.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.gene.zebox.R
import com.gene.zebox.databinding.ActivityNewTaskBinding
import com.gene.zebox.defect.viewmodel.DefectViewModel
import com.gene.zebox.defect.widget.AutoCompleteAdapter
import com.gene.zebox.defect.widget.ListPopupWindow
import com.gene.zebox.defect.widget.MainAdapter
import com.gene.zebox.defect.widget.SuggestAdapter
import kotlinx.android.synthetic.main.activity_new_task.*


class DefectActivity : AppCompatActivity() {
    private lateinit var mainAdapter: MainAdapter

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityNewTaskBinding>(
            this,
            R.layout.activity_new_task
        )
    }
    private val vm by lazy { ViewModelProviders.of(this)[DefectViewModel::class.java] }

    private val popupWindow by lazy {
        ListPopupWindow(binding.edit).apply {
            setOnItemClickListener {
                binding.vm?.add2Selected(it)
                binding.edit.setText("")
                dismiss()
            }
        }
    }

    private val adapter by lazy {
        AutoCompleteAdapter(vm.allLiveData)
    }


    private val pop by lazy {
        androidx.appcompat.widget.ListPopupWindow(this).apply {
            this.setAdapter(this@DefectActivity.adapter)
            this.setOnDismissListener {
                adapter.clear()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.vm = vm
        recycler.layoutManager = LinearLayoutManager(this)
//        recycler.adapter = SuggestAdapter(vm.allLiveData)
//        mainAdapter = recycler.adapter as MainAdapter
        binding.suggest.adapter = SuggestAdapter(vm.allLiveData)
        edit.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null &&
                        event.keyCode == KeyEvent.KEYCODE_ENTER &&
                        event.action == KeyEvent.ACTION_UP)
            ) {
                vm.newItem()
                binding.edit.setText("")
            }
            return@setOnEditorActionListener true
        }

        vm.edit.observe(this) {

            val suggestAdapter = binding.suggest.adapter as SuggestAdapter
            suggestAdapter.submit(it) { hasData ->
                binding.suggestView.visibility = if (hasData) {
                    showAnimator.start()
                    View.VISIBLE
                } else {

                    View.INVISIBLE
                }
            }

        }
        vm.allLiveData.observe(this) {}
    }

    private val showAnimator by lazy {
        val ofFloat = ValueAnimator.ofFloat(0F, 1F)
        ofFloat.duration = 618
        ofFloat.addUpdateListener {
            binding.suggestGroup.visibility = View.VISIBLE
            binding.suggestGroup.alpha = it.animatedValue as Float
        }
        ofFloat.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
                binding.suggestGroup.alpha = 1F
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        ofFloat
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)

    }

    override fun onBackPressed() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss()
        } else
            super.onBackPressed()
    }
}




