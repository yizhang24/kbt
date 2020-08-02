package kiwibot;

import kiwibot.Commands.Command;

import java.util.TreeMap;

public class CommandHelper {
    private TreeMap<String, Command> commands;
    public CommandHelper(){
        commands = new TreeMap<String, Command>();
    }
    public Command registerCommand(Command _command){
        commands.put(_command.name, _command);
        return _command;
    }
}
