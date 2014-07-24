CloudML comes along with a shell component, which allows you to load, modify and enact CloudML models. The documentation below describes how to install, run, and use the shell.

# Installing CloudML Shell
The CloudML shell is a standalone Java application distributed as a single executable Jar file. There is therefore no specific installation procedure, except to have Java installed and to download the JAR file and to run it.

> Please note that CloudML needs to have the JClouds provider related library available in your class path. For instance, if want to you use AWS-EC2 as a provider, you need the related JAR file (e.g., `aws-ec2-1.5.4.jar`) to be available through your Java classpath.

# Starting the CloudML Shell

The CloudML shell can be run in two main modes: a batch mode and an interactive shell. The batch mode lets you run a single command, whereas the interactive mode lets you input cloudML commands in a step-wise fashion.

## Batch Mode
To run the batch mode, the user must specify the CloudML commands to be executed

Any single command, which is supported by the interactive mode, can be passed on the command line, as done above for the `deploy` command. Another simple example is the `version` command, which simply shows the version number of the CloudML Shell

```
$> java -jar cloudml-shell.jar version
CloudML Shell (v0.0.1)
$>
```

## Interactive Mode
The interactive mode can be started by using `-i` flag on the command. In this mode the user is prompted with command to execute. The commonly used commands are provided below. 
```
$> java -jar cloudml-shell.jar -i
CloudML Shell (v0.0.1)
Copyright (c) 2012 - SINTEF ICT [http://sintef.no]

This program comes with ABSOLUTELY NO WARRANTY; See LGPL v3 for details.
This is free software, and you are welcome to redistribute it
under certain conditions; see LGPL v3 for details.


CloudML >
```

# The CloudML Shell Commands

This session provides a brief description of the commands you can use while using the interactive mode of the CloudML Shell.

> It is worth to note that the CloudML shell supports browsing the history of past commands using the up and down arrows. In addition, it supports completion of command and file names using the `tab` key.

## Key Commands
The table below summarises the key commands, which one must know in order to use the CloudML shell. These commands, will let you manage your shell sessions.

| Name | Syntax | Description |
|------  |--------|-------------|
| exit | `exit` | Terminate the Shell Session |
| quit | `quit` | Alias for `exit` |
| help | `help <d>?` | Provide information about the available commands |
| history | `history <d>?` | List the command you previously entered |
| dump | `dump [from <d>]? to <file>?` | Write the d last commands of the history into the given file |
| replay | `replay <file>` | Replay all the shell commands stored in the given file |
| messages | `messages <d>?` | Show the d last messages received by the shell from the CloudML back-end |
| version | `version` | Show the version of the CloudML shell |

The most important command is the `help` command, which can be used in two ways. As such, `help`, will list all the commands currently supported by the CloudML Shell. Extended with a command name, it will provide detailed information about the selected command.

```
CloudML > help "dump"
 - Name: dump
 - Syntax: dump [from <d>]? to <file>
 - Description:
    Dump the commands previously entered by user into a text file, which can
    later be replayed, using the 'replay command' or edited using any text
    editor. The 'from <d>' option, allows the user to specify how many of the
    last commands she wants to write in the given file.
 - Examples:
    - syntax: dump to sessions/mysession.txt
       Dump the complete history of commands into the 'sessions/mysession.txt'
       file
    - syntax: dump 5 to mysession.txt
       Dump the last five commands into the file 'mysession.txt'

CloudML >
```

## Managing Deployment Models through the CloudML Shell

In addition to the above commands, the CloudML shell also provides a set of commands, which enable interaction with the application running in the Cloud. The table below summarises these commands:

| Name | Syntax | Description |
|------|--------|-------------|
| load deployment | `load deployment from <file>` | Load in memory the deployment model stored in the given file |
| deploy | `deploy` | Effectively deploy the deployment model in memory. |
| upload | `upload <file> on <vm-id> at <file>`  | Upload the selected file on the given VM, at the given place |
| snapshot | `snapshot <vm-id>`  | Create a snapshot of the given VM |
| create image | `create image from <vm-id>`  | Create an image of the given VM |

Some of these commands may take some time to be completed (_e.g._, uploading a file on a given node), and one may want to run them as a background task. The CloudML shell let you do so so by appending a `&` symbol at the end of the command.

> It is worth to note that the CloudML back-end does not execute commands in parallel, but the Shell and the back-end run in parallel. Appending a `&` thus ensures that the shell is not blocked during the execution of a given command, but does not necessarily make its execution in a separate process.


### Writing  Scripts
As shown above, the `replay` and `dump` commands let you store or rerun sequences of commands in a text file. You may as well directly edit such a file in your favourite editor. Commented lines must be prefixed by the `#` character, as shown in the following simple example:

```
#
# A simple CloudML Shell Script, which loads 
# a complete deployment file, and deploys it.
#

load deployment from apps/sensapp/production.json
deploy 

# ends here!
```
