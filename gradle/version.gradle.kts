import java.util.*

fun getVersionNameFromProperties(): String {
    return loadVersionProperties().getProperty("version_name")
}

fun getVersionCodeFromProperties(): Int {
    return loadVersionProperties().getProperty("version_code").toInt()
}

fun loadVersionProperties(): Properties {
    val props = Properties()
    project.rootProject.file("version.properties")
        .inputStream()
        .use(props::load)
    return props
}

extra.apply{
    set("version_name", getVersionNameFromProperties())
    set("version_code", getVersionCodeFromProperties())
}
