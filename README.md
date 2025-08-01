# life-evolution-calculator

To install, just run maven compilation : 

`mvn clean package`

This will create a zip file in 'target' directory. Copy this zip file anywhere and unzip the file.

To run the tool, you just have to have a java 17 jre, and launch : 

`java -jar [tools-library.jar]`

All arguments will be described.

Tool parameter can also be change in application.yml file.


For the actual version, you can have a quick result with this command (show statictics and 5 iterations) : 
`java -jar .\life-evolution-calculator-1.0.jar -s=true .\test.life`
