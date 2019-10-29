package vn.zuni.findpurpose.extensions

import java.lang.reflect.Method
import java.util.*

/**
 *
 * Created by namnd on 06-Oct-17.
 */

class ComponentReflectionInjector<T>(private val componentClass: Class<T>, private val component: T) : Injector {
    private val mMethods: HashMap<Class<*>, Method>

    init {
        this.mMethods = getMethods(componentClass)
    }

    override fun inject(target: Any) {

        var targetClass: Class<*>? = target.javaClass
        var method = mMethods[targetClass]
        while (method == null && targetClass != null) {
            targetClass = targetClass.superclass
            method = mMethods[targetClass]
        }
        if (method == null) throw RuntimeException(String.format("No %s injecting method exists in %s component", target.javaClass, componentClass))

        try {
            method.invoke(component, target)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private val cache = HashMap<Class<*>, HashMap<Class<*>, Method>>()
        private fun getMethods(componentClass: Class<*>): HashMap<Class<*>, Method> {
            var methods = cache[componentClass]
            if (methods == null) {
                synchronized(cache) {
                    methods = cache[componentClass]
                    if (methods == null) {
                        methods = HashMap()
                        componentClass.methods.forEach {
                            val params = it.parameterTypes
                            if (params.size == 1) methods!!.put(params[0], it)
                        }
                        cache.put(componentClass, methods!!)
                    }
                }
            }
            return methods!!
        }
    }

}
