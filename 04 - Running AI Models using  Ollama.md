# Running AI Models using Ollama - 1st March 2026

## Setup:
1. Downwload Ollama - https://ollama.com/download
2. Install Ollama

## Using Ollama Chat to run cloud models
1. Launch Ollama
2. You would need to sign in - link your google account to ollama account.
3. Search for cloud version of deepseek model e.g. select deepsek-v3.2:cloud
4. Click on the Bulb button if it's there - i.e. enable thinking mode
5. Ask a question such as what is 1 + 1
6. Click on "Thought for " link to see the reasoning behind the answers

## Using Ollama from Command prompt  to run Cloud modesl
1. Follow instructions from: https://docs.ollama.com/cloud
2. Open a command prompt (windows + R and type cmd) and run ollama signin
3. Click on the link provided to sign in
4. Come back to the terminal and type ollama
5. Run this command:
  1. ollama run deepseek-v3.2:cloud
  2. Ask the question "what is 1 + 1"
  3. Test with other prompts
```
C:\Users\swapp>ollama run deepseek-v3.2:cloud
Connecting to 'deepseek-v3.2' on 'ollama.com' ⚡
>>> what is 1 + 1
Thinking...
We are asked: "what is 1 + 1". This is a very simple arithmetic question. The answer is 2. However, the user might
be expecting a response that is not just the answer but perhaps a bit more context or a joke? But the instruction
is straightforward: "what is 1 + 1". So we should answer directly. But as an AI, we can provide a concise answer.

Given the simplicity, I'll answer: 2.

But to be thorough, we can explain: 1 + 1 equals 2. It's the basis of arithmetic.

Thus, final answer: 2.
...done thinking.

1 + 1 = 2.
```
## Using Ollama from command prompt to download and run models locally
1. Follow instructions from: https://docs.ollama.com/quickstart
2. Lets run deepseek-coder model
   1.  Open command prompt and run this
     1. ollama run deepseek-coder
   2. Ask the question - "Write me java, python and c++ programs to print hello"
Sample output:
C:\Users\swapp>ollama run deepseek-coder
pulling manifest
pulling d040cc185215: 100% ▕██████████████████████████████████████████████████████████▏ 776 MB
pulling a3a0e9449cb6: 100% ▕██████████████████████████████████████████████████████████▏  13 KB
pulling 8893e08fa9f9: 100% ▕██████████████████████████████████████████████████████████▏   59 B
pulling 8972a96b8ff1: 100% ▕██████████████████████████████████████████████████████████▏  297 B
pulling d55c9eb1669a: 100% ▕██████████████████████████████████████████████████████████▏  483 B
verifying sha256 digest
writing manifest
success
```
C:\Users\swapp>ollama run deepseek-coder
>>> can you give me a code to print hello world using java as well as python
Sure! Here is how we can do it in Java and Python respectively :

Java Code (HelloWorld.java):
```JAVA
public class HelloWorld {
     public static void main(String[] args) {         System.out.println("Hello World!");       }        // Main
method to start the execution of Java program
}
```
To compile and run this code:
`$ javac -version` (This will check your java version). If you get an error, it means that `javac cannot find
java), then open a new terminal tab or command prompt. Navigate to the directory where 'HelloWorld' file is
present using above commands ('cd Hello World'). Then run this code by executing following:
`$java -classpath . HelloWorld  `   (This will compile and execute Java program). The output should be "HEllo
WOrld!". This means that it has executed successfully! It's working fine now, you can check the file permission if
necessary using `chmod +x [filename]`

Python Code:
```python
print("Hello World")     # Print Hello world in Python language      // main method to start execution of python
program        def my_function():         print('This is a function')          for i in range(10):             if
(i == 5) :                 break                  else:                     pass               end
```
To run this code, save it with `.py` extension and then execute the file using command line by typing following
commands respectively ('cd' to change directory 'python [filename]'). Then you will see "Hello World" printed in
your terminal/command prompt or output window:   Hello world is displayed on screen as a result of this code
execution!


>>> /bye
```
