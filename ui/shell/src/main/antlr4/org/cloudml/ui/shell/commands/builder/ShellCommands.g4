/*
 * The ANTLR grammar for the shell commands
 */

grammar ShellCommands;

script
    :   command+            
    ;

command
    :   'dump' INTEGER? 'to' PATH                           # Dump
    |   'exit'                                              # Exit
    |   'help' STRING?                                      # Help
    |   'history' INTEGER?                                  # History
    |   'messages' INTEGER?                                 # Messages
    |   'quit'                                              # Quit
    |   'replay' PATH                                       # Replay
    |   'version'                                           # Version
    |   action asJob?                                       # Proxy
    ;


action
    :   'analyse' 'robustness' filter?                      # AnalyseRobustness 
    |   'connect' customer=ID 'to' provider=ID              # Connect 
    |   'deploy'                                            # Deploy
    |   'destroy' instance=ID                               # Destroy
    |   'disconnect' customer=ID 'from' provider=ID         # Disconnect
    |   'install' component=ID 'on' platform=ID             # Install
    |   'instantiate' type=ID 'as' instance=ID              # Instantiate
    |   'list' level                                        # List
    |   'load' kind 'from' PATH                             # Load                                       
    |   'shot' 'to' PATH                                    # ShotImage
    |   'start' ID                                          # Start
    |   'stop' ID                                           # Stop
    |   'store' kind 'to' PATH                              # Store
    |   'uninstall' component=ID 'from' platform=ID         # Uninstall
    |   'upload' local=PATH 'on' ID 'at' remote=PATH        # Upload
    |   'view' level ID                                     # View
    |   'snapshot' ID                                       # Snapshot
    |   'scale out' ID                                      # ScaleOut
    |   'create image from'  ID                             # Image
    |   'reset deployment'                                  # Reset
    |   'validate' noWarnings='no warnings'?                # Validate
    ;

kind
    :   'deployment'
    |   'credentials'
    ;

level
    :   'instances'
    |   'instance'
    |   'types'
    |   'type'
    ;
    

filter
    :   observe=tag '/' control=tag
    ;


tag
    :   'internal'
    |   'external'
    |   'vm'
    |   'all'
    ;

asJob
    :   '&'
    ;

fragment
DIGIT   
    :   [0-9] 
    ;

LETTER  
    :   [a-zA-Z\u0080-\u00FF_]
    ;


INTEGER
    :   DIGIT+
    ;

ID      
    :   LETTER(LETTER|DIGIT|'-'|PATH_SEPARATOR)+
    |   '@' (HEX_DIGIT+ '-')+ HEX_DIGIT+ { setText(getText().replaceAll("@", "")); }
    ;

fragment
HEX_DIGIT
    :   [a-fA-F-0-9]
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

LINE_COMMENT
    : '#' ~[\r\n]* -> channel(HIDDEN)
    ;


