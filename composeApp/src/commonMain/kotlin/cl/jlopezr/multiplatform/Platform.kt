package cl.jlopezr.multiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform