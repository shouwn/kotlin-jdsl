coverage {
    exclude(project)
}

dependencies {
    api(Modules.testFixtureCore)
    api(Modules.testFixtureEntity)
    api(Dependencies.javaPersistenceApi)
    api(Dependencies.slf4j)
    api(Modules.reactiveCore)

    implementation(Dependencies.kotlinReflect)
    implementation(Dependencies.coroutineJdk8)
    implementation(Dependencies.blockhound)
    implementation(Dependencies.coroutineDebug)
    implementation(Dependencies.junitPlatformLauncher)
    implementation(Dependencies.vertxJdbcClient)
    implementation(Dependencies.springJpa)
}
