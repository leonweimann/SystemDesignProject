@echo off

echo ### Start download of LEGO Fantom driver. This may take a while...
curl https://www.lego.com/cdn/cs/set/assets/bltea140e66e32fadf0/NXT_Fantom_Drivers_v120.zip --output "%1"
echo ### Download finished
tar -xf "%1" -C %2
echo ### Extraction finished
powershell -Command "Start-Process \"%~dp0NXT Fantom Drivers\Windows\1.2\1.2.0\autorun.exe\" -Verb runAs"
echo ### Click on 'Install NXT Driver' at the appearing window and then alway on 'Next'