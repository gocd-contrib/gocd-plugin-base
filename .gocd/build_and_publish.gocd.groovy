GoCD.script {
  pipelines {
    pipeline('gocd-plugin-base') {
      group = 'plugins'
      labelTemplate = '${COUNT}'
      lockBehavior = 'none'
      materials {
        svn('signing-keys') {
          url = "https://github.com/gocd-private/signing-keys/trunk"
          username = "gocd-ci-user"
          encryptedPassword = "AES:taOvOCaXsoVwzIi+xIGLdA==:GSfhZ6KKt6MXKp/wdYYoyBQKKzbTiyDa+35kDgkEIOF75s9lzerGInbqbUM7nUKc"
          destination = "signing-keys"
        }
        git('plugin-base') {
          branch = 'master'
          shallowClone = false
          url = 'https://github.com/gocd-contrib/gocd-plugin-base'
          destination = 'plugin-base'
          autoUpdate = true
        }
      }
      stages {
        stage('build') {
          artifactCleanupProhibited = false
          cleanWorkingDir = true
          fetchMaterials = true
          jobs {
            job('build') {
              elasticProfileId = 'ecs-gocd-dev-build'
              tasks {
                bash {
                  commandString = "./gradlew assemble check"
                  workingDir = 'plugin-base'
                }
              }
            }
          }
        }
        stage('publish') {
          artifactCleanupProhibited = false
          cleanWorkingDir = true
          fetchMaterials = true
          environmentVariables = [
            GNUPGHOME            : '~/.code-signing-keys',
            GOCD_GPG_KEYRING_FILE: 'signing-key.gpg',
          ]
          secureEnvironmentVariables = [
            GOCD_NEXUS_USERNAME: 'AES:H0l96KkJWnHkWdJPVdha2Q==:XvKyOuun43zh4L+TSpbGvw==',
            GOCD_NEXUS_PASSWORD: 'AES:r/8x3K1wEReuRZRmm37h/g==:ASQOrYbpwigCPA8GfkQi7NWDzejOTean0v9XwYUcvjPlgWdvR6xnvbwvrAdBE6Rg',
            GOCD_GPG_PASSPHRASE: 'AES:7lAutKoRKMuSnh3Sbg9DeQ==:8fhND9w/8AWw6dJhmWpTcCdKSsEcOzriQNiKFZD6XtN+sJvZ65NH/QFXRNiy192+SSTKsbhOrFmw+kAKt5+MH1Erd6H54zJjpSgvJUmsJaQ=',
            GOCD_GPG_KEY_ID    : 'AES:abQlKbCl6aE5NgVisyu7jg==:/wrZ34d5qiawh14DxK/6JQbw22kPO+k7HNcuFqJOqL8='
          ]
          jobs {
            job('upload-to-maven') {
              elasticProfileId = 'ecs-gocd-dev-build'
              tasks {
                bash {
                  commandString = 'echo $GOCD_GPG_PASSPHRASE > gpg-passphrase'
                }
                bash {
                  commandString = 'gpg --quiet --batch --passphrase-file gpg-passphrase --output - ../signing-keys/gpg-keys.pem.gpg | gpg --import --batch --quiet'
                }
                bash {
                  commandString = 'gpg --export-secret-keys $GOCD_GPG_KEY_ID > $GOCD_GPG_KEYRING_FILE'
                }
                bash {
                  commandString = './gradlew clean uploadArchives closeAndReleaseRepository'
                  workingDir = "plugin-base"
                }
              }
            }
          }
        }
      }
    }
  }
}

