rem cd "J:\Useful documents\java Projects\Github\Android-WhiteLabel-Lab"
call android update project --name "Appcompat-v7" --path \appcompat
call android update project --name "Nobank" --path \Nobank --library "..\appcompat" --target "Google Inc.:Google APIs:17"
call android update project --name "StubServer" --path \StubServer --library "..\Nobank" --target "Google Inc.:Google APIs:17"
call android update project --name "ReferenceBank" --path \Reference --library "..\Nobank" --target "Google Inc.:Google APIs:17"
call android update project --name "ReferenceBank" --path \Reference --library "..\StubServer" --target "Google Inc.:Google APIs:17"
call android update test-project --main ..\Reference --path \TestReferenceTest
rem cd "Reference"
rem call ant clean release > release.log