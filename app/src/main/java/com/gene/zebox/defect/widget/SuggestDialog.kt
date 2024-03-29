package com.gene.zebox.defect.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.gene.zebox.databinding.DialogSuggestBinding
import com.gene.zebox.defect.viewmodel.DefectViewModel
import com.google.android.material.snackbar.Snackbar


class SuggestDialog : DialogFragment() {
    companion object {
        const val TAG = "SuggestDialog"
    }

    private var newDefectSnackbar: Snackbar? = null

    private val addDefectSnackbar by lazy {
        Snackbar.make(
            binding.root,
            ""
            , Snackbar.LENGTH_LONG
        ).setAction("撤销") {
            vm.removeLastAdd()
        }
    }

    private val normalSnackbar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
    }

    private lateinit var binding: DialogSuggestBinding
    private lateinit var vm: DefectViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProviders.of(activity!!)[DefectViewModel::class.java]
        binding.vm = vm
        vm.edit.observe(this) {
            binding.searchView.query(it) { hasData ->
                if (hasData || it.isEmpty()) {
                    newDefectSnackbar?.dismiss()
                    newDefectSnackbar = null
                } else {
                    newDefectSnackbar =
                        Snackbar.make(binding.root, "空空如野，创建此缺陷？", Snackbar.LENGTH_INDEFINITE)
                            .setAction("创建") {
                                vm.newItem(binding.edit.text.toString())
                                normalSnackbar.setText("创建缺陷成功").show()
                            }.apply { show() }
                }
            }
        }
        binding.searchView.bind(vm.allLiveData, this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSuggestBinding.inflate(inflater, container, false)

        binding.parent.setOnClickListener {
            dismissAllowingStateLoss()
        }
        binding.searchView.setOnItemClickListener { data, selectOrEdit ->
            if (selectOrEdit) {
                vm.add2Selected(data) { success ->
                    if (success)
                        addDefectSnackbar.setText("“${data.text}”添加成功").show()
                    else
                        normalSnackbar.setText("已添加过此缺陷了哦").show()
                }
            } else {
                binding.edit.setText(data.text)
                binding.edit.setSelection(data.text.length)
            }
        }

        return binding.root
    }

    fun reset() {
        try {
            vm.edit.value = ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val inAnim = {
        val view = binding.root

        ViewAnimationUtils.createCircularReveal(
            view,
            view.width * 2,
            view.y.toInt(),
            0f,
            view.width.toFloat() * 2
        ).apply {
            duration = 512
            interpolator = AccelerateInterpolator()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), com.gene.zebox.R.style.MaterialSearch).apply {
            setOnShowListener {
                //                inAnim.invoke().start()

                val dialog = it as Dialog
//                dialog.window?.setWindowAnimations(-1)
                dialog.window?.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                dialog.window?.decorView?.setPadding(0, 0, 0, 0);
                dialog.window?.setGravity(Gravity.TOP)
                dialog.findViewById<EditText>(com.gene.zebox.R.id.edit).requestFocus()
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
                    0,
                    InputMethodManager.SHOW_FORCED
                )
            }
        }
    }

}