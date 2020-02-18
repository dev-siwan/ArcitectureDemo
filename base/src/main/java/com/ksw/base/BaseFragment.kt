package com.ksw.base

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import app.hayd.base.utils.viewModel
import com.ksw.base.utils.guard
import kotlin.reflect.KClass

abstract class BaseFragment<V : ViewDataBinding, VM : ViewModel>(
    @get:LayoutRes val layoutId: Int,
    viewModelCls: KClass<VM>? = null,
    private val bindingVariable: Int? = null,
    @get:MenuRes val menuId: Int? = null
) : Fragment() {
    private lateinit var binding: V
    val viewModel: VM? by viewModel(clazz = viewModelCls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this,object :OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                   onBackPressed()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? =
        DataBindingUtil.inflate<V>(inflater, layoutId, container, false).apply {
            onBindBefore(this)
            onBind(this)
            onBindAfter(this)
        }.root

    protected open fun onBindBefore(dataBinding :V) = Unit
    protected open fun onBind(dataBinding :V) = dataBinding.run {
        guard(bindingVariable, viewModel) {
            setVariable(first, second)
            executePendingBindings()
        }
        this.lifecycleOwner=this@BaseFragment
        this@BaseFragment.binding = this
    }
    protected open fun onBindAfter(dataBinding :V) = Unit

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) = menuId?.run {
        menu.clear()
        inflater.inflate(this, menu)
    } ?: Unit

    val intent: Intent? get() = activity?.intent


    protected fun binding(action: V.() -> Unit) {
        binding.run(action)
    }

    protected fun viewModel(action: VM.() -> Unit) {
        viewModel!!.run(action)
    }

    protected open fun onBackPressed(): Boolean = false
}