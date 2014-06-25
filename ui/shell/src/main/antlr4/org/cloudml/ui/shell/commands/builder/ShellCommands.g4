/*
 * The ANTLR grammar for the shell commands
 */

grammar ShellCommands;

script
    :   command+            
    ;

command
    :   'dump' INTEGER? 'to' PATH   # Dump
    |   'exit'                      # Exit
    |   'help' STRING?              # Help
    |   'history' INTEGER?          # History
    |   'messages' INTEGER?         # Messages
    |   'quit'                      # Quit
    |   'replay' PATH               # Replay
    |   'version'                   # Version
    |   action '&'?                 # Proxy
    ;


action
    :   'deploy'                    # Deploy
    ;

DIGIT   
    :   [0-9] 
    ;

LETTER  
    :   [a-zA-Z\u0080-\u00FF_]
    ;

ID      
    :   LETTER(LETTER|DIGIT)+;

INTEGER
    :   DIGIT+
    ;

PATH
    :   DRIVE? FILE (PATH_SEPARATOR FILE)*
    ;

DRIVE
    :   [a-zA-Z] ':' '\\'       
    |   '/'                 
    ;

PATH_SEPARATOR
    :   '\\' 
    |   '/'
    ;

FILE
    :   [a-zA-Z0-9-$_'.']+
    ;

STRING
    :   '"' (~["])* '"'
    ;

WS
    :   [ \t\n\r]+ -> skip 
    ;