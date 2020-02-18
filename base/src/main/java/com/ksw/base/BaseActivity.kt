package com.ksw.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import app.hayd.base.utils.viewModel
import com.ksw.base.utils.guard
import kotlin.reflect.KClass

abstract class BaseActivity<V : ViewDataBinding, VM : ViewModel>
    (
    @get:LayoutRes val layout: Int?,
    viewModelClass: KClass<VM>? = null,
    private val bindingVariable: Int? = null,
    @get:MenuRes val menuID: Int? = null
) : AppCompatActivity() {

    private lateinit var binding: V
    private val viewModel: VM? by viewModel(viewModelClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout?.let { layoutID ->
            binding = DataBindingUtil.setContentView<V>(this, layoutID).apply {
               guard(bindingVariable,viewModel){
                   this@apply.setVariable(first,second)
               }
                lifecycleOwner = this@BaseActivity
                executePendingBindings()
            }
        }
    }

    protected fun binding(action: V.() -> Unit) {
        binding.action()
    }

    protected fun viewModel(action: VM.() -> Unit) {
        viewModel?.action()
    }
}