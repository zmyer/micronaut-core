import static org.apache.commons.io.FileUtils.moveFile
import static org.apache.commons.io.FilenameUtils.getBaseName

String appName = getBaseName(projectDir.path)
Map props = [:]
props.groupId = ask("Define value for 'groupId' [org.example]: ", "org.example", "groupId")
props.version = ask("Define value for 'version' [0.1]: ", "0.1", "version")
props.appName = appName

File rootDir = projectDir
File srcDir = new File (rootDir, "src/main/groovy")

String pkg = "${props.groupId}.${appName}"
props.pkg = pkg
String pkgPath = pkg.replace('.' as char, System.getProperty('file.separator') as char)
File pkgDir = new File (srcDir, pkgPath)
assert pkgDir.mkdirs()

File buildGradleTmpl = new File(rootDir, "build.gradle.gtpl")
File applicationClassTmpl = new File(rootDir, "Application.groovy.gtpl")

File buildGradle = new File(rootDir, "build.gradle")
File applicationClass = new File(pkgDir, "Application.groovy")
applicationClass.parentFile.mkdirs()

processTemplates "**/*.gtpl", props
moveFile(buildGradleTmpl, buildGradle)
moveFile(applicationClassTmpl, applicationClass)
