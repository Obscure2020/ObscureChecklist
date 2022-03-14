@echo off
echo.
echo Rebuilding classes...
echo.
javac Checklist.java && java Checklist
del *.class
if exist outBook.dat (
    del Book.dat
    rename outBook.dat Book.dat
)