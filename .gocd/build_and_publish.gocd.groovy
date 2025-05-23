import cd.go.contrib.plugins.configrepo.groovy.dsl.GoCD

def secretParam = { String param ->
  return "{{SECRET:[build-pipelines][$param]}}".toString()
}

GoCD.script {
  pipelines {
    pipeline('gocd-plugin-base') {
      group = 'plugins'
      labelTemplate = '${COUNT}'
      lockBehavior = 'none'
      materials {
        git('signing-keys') {
          url = "https://git.gocd.io/git/gocd/signing-keys"
          destination = "signing-keys"
        }
        git('plugin-base') {
          url = 'https://git.gocd.io/git/gocd-contrib/gocd-plugin-base'
          destination = 'plugin-base'
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
          approval {
            type = 'manual'
          }
          environmentVariables = [
            GNUPGHOME                    : '.signing',
            GOCD_GPG_KEYRING_FILE        : 'signing-key.gpg',
            GOCD_GPG_PASSPHRASE          : secretParam("GOCD_GPG_PASSPHRASE"),
            MAVEN_CENTRAL_TOKEN_USERNAME : secretParam("MAVEN_CENTRAL_TOKEN_USERNAME"),
            MAVEN_CENTRAL_TOKEN_PASSWORD : secretParam("MAVEN_CENTRAL_TOKEN_PASSWORD"),
          ]
          secureEnvironmentVariables = [
            GOCD_GPG_KEY_ID              : 'AES:+ORNmqROtoiLtfp+q4FlfQ==:PxQcI6mOtG4J/WQHS9jakg==',
          ]
          jobs {
            job('upload-to-maven') {
              elasticProfileId = 'ecs-gocd-dev-build'
              tasks {
                bash {
                  commandString = 'mkdir -p ${GNUPGHOME}'
                  workingDir = "plugin-base"
                }
                bash {
                  commandString = 'echo ${GOCD_GPG_PASSPHRASE} > gpg-passphrase'
                  workingDir = "plugin-base"
                }
                bash {
                  commandString = 'gpg --quiet --batch --passphrase-file gpg-passphrase --output - ../signing-keys/gpg-keys.pem.gpg | gpg --import --batch --quiet'
                  workingDir = "plugin-base"
                }
                bash {
                  commandString = 'gpg --export-secret-keys ${GOCD_GPG_KEY_ID} > ${GOCD_GPG_KEYRING_FILE}'
                  workingDir = "plugin-base"
                }
                bash {
                  commandString = './gradlew clean publishToSonatype closeAndReleaseSonatypeStagingRepository'
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

