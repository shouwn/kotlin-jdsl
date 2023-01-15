coverage {
    exclude(project)
}

dependencies {
    api(libs.junit)
    api(libs.mockk)
    api(libs.assertJ)
    api(libs.logback)
    api(libs.coroutine.jdk8)
}
