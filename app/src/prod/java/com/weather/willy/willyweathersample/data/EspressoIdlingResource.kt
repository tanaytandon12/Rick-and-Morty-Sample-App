package com.weather.willy.willyweathersample.data

object EspressoIdlingResource {


    fun increment() {
        // do nothing
    }

    fun decrement() {
        //do nothing
    }

}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
    return try {
        function()
    } finally {
        // do nothing
    }
}
