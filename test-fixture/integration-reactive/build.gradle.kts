coverage {
    exclude(project)
}

dependencies {
    api(Modules.testFixtureCore)
    api(Modules.testFixtureEntity)
    api(libs.java.persistence.api)
    api(libs.slf4j)
    api(Modules.reactiveCore)

    implementation(libs.kotlin.reflect)
    implementation(libs.coroutine.jdk8)
    implementation(libs.blockhound)
    implementation(libs.vertx.jdbc.client)
    implementation(libs.agroal.pool)
    implementation(libs.spring.jpa)
    implementation(libs.coroutine.jdk8)
}
