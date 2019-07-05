GoCD.script {
  pipelines {
    pipeline('gocd-plugin-base') {
      group = 'plugins'
      labelTemplate = '${COUNT}'
      lockBehavior = 'none'
      secureEnvironmentVariables = [
        GOCD_GPG_PASSPHRASE: 'AES:7lAutKoRKMuSnh3Sbg9DeQ==:8fhND9w/8AWw6dJhmWpTcCdKSsEcOzriQNiKFZD6XtN+sJvZ65NH/QFXRNiy192+SSTKsbhOrFmw+kAKt5+MH1Erd6H54zJjpSgvJUmsJaQ='
      ]
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
            'MAVEN_NEXUS_USERNAME': 'arvindsv'
          ]
          secureEnvironmentVariables = [
            'MAVEN_NEXUS_PASSWORD': 'AES:U0+58CAsIkycH+6DUL+Z6w==:EoTd+MQsXP8iL64+eDUi226NbEOGM3N6RfYxZeXH6C30X70xcKKuaEuFVLATe92Ht9RDNrMhXbv2lAt/iEoEbA==',
            'GPG_KEY_ID'          : 'AES:abQlKbCl6aE5NgVisyu7jg==:/wrZ34d5qiawh14DxK/6JQbw22kPO+k7HNcuFqJOqL8='
          ]
          jobs {
            job('upload-to-maven') {
              elasticProfileId = 'ecs-gocd-dev-build'
              tasks {
                bash {
                  commandString = './gradlew uploadArchives -PmavenUser=${MAVEN_NEXUS_USERNAME} -PmavenPassword=${MAVEN_NEXUS_PASSWORD} -PgpgKeyId=${GPG_KEY_ID} -Psigning.secretKeyRingFile=$(readlink -f ../signing-keys/*.gpg) -PgpgPassword=${GOCD_GPG_PASSPHRASE}'
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

