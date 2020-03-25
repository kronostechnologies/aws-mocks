rootProject.name = "aws-mocks"

if (System.getenv().containsKey("CI")) {
    buildCache {
        local {
            directory = File(rootDir, ".gradle-cache")
        }
    }
}
