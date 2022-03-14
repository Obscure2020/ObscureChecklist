@echo off
javac Checklist.java && java Checklist
del *.class
if exist outBook.dat (
    del Book.dat
    rename outBook.dat Book.dat
)