cd "J:\Useful documents\java Projects\Github\Android-WhiteLabel-Lab"
call android update project --name "Appcompat-v7" --path \appcompat
call android update project --name "Nobank" --path \Nobank --library "..\appcompat"
call android update project --name "ReferenceBank" --path \Reference --library "..\Nobank"
cd "Reference"
call ant clean release > release.log