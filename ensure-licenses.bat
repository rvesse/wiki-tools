@echo off

cd > temp.dir
set /p pwd=<temp.dir
del temp.dir

echo Directory to check is %pwd%

LicenseChecker.exe -d "%pwd%" -exclude-exts "xml,class,properties,classpath,project,prefs,mf,jar,md,orig,txt,bat,dir" -exclude "\.hg|target" -license-search "dotNetRDF Project" -license-file "%pwd%\license-header.txt" -fix -overwrite