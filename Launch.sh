#!/bin/bash
echo -e "\nRebuilding classes...\n"
javac Checklist.java && java Checklist
rm *.class
if [ -f "outBook.dat" ]; then
    echo -e "\nSwapping file..."
    [ -f "Book.dat" ] && rm Book.dat
    mv outBook.dat Book.dat
    echo -e "Complete."
fi
echo -ne "\n"
