package compose.diffmorph.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform