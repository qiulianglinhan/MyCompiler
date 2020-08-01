# My Compiler
[![build](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/qiulianglinhan/MyCompiler)

## Profile

- This is a simple  C language  compiler  interpreted and executed by Java.
- The compiler uses recursive descent method.

## Requirements

- [x] JDK1.8 or later
- [x] IIntelliJ IDEA 2020.1.2 x64

## Building & Running

1. Import the project into IDEA 
2. Set the input source code in ApplicationRun.java file
3. Run as Java application from class ApplicationRun

## Final executed code

- Using Java interprets and executes four formula

## Support keywords

- You can find every support words in common/Tag.java

## Note

- This is a practice project by myself. If I have interest and time, I will perfect the project.
- There are many bugs I know:
  1. expression assign is not support negative number ( the problem can be fixed in reverse function by adding an new inter function )
  2. if else statement is not support square bool (  eg:  if ( (a > 0 && b < 0) || c > 0 )  ) , I think the problem can be fixed in bool function.