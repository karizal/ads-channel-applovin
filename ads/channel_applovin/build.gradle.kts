plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.karizal.ads_channel_applovin"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api("com.github.karizal:ads-base:1.0.0-r2")
    implementation("com.applovin:applovin-sdk:11.10.1")
    implementation("com.google.android.gms:play-services-base:18.2.0")

    //noinspection GradleDynamicVersion
    implementation("com.applovin.mediation:adcolony-adapter:+")
    //noinspection GradleDynamicVersion
    implementation("com.applovin.mediation:facebook-adapter:+")
    //noinspection GradleDynamicVersion
    implementation("com.applovin.mediation:google-adapter:+")
    //noinspection GradleDynamicVersion
    implementation("com.applovin.mediation:unityads-adapter:+")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.karizal"
                artifactId = "ads_channel_applovin"
                version = "1.0.0"
            }
        }
    }
}