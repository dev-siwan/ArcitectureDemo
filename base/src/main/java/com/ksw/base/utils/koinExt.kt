package app.hayd.base.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import kotlin.reflect.KClass

fun <T : ViewModel> LifecycleOwner.viewModel(
    clazz: KClass<T>?,
    qualifier: Qualifier? = null,
    parameters: ParametersDefinition? = null
): Lazy<T?> = clazz?.let {
    lazy { getViewModel(clazz, qualifier, parameters) }
} ?: lazy {
    null
}