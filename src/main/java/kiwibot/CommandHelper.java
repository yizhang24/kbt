package kiwibot;

import kiwibot.Commands.MasterCommand;

import java.util.TreeMap;

public class CommandHelper {
    private static TreeMap<String, MasterCommand> commands;
    public CommandHelper(){
        commands = new TreeMap<>();
    }
    public MasterCommand registerCommand(MasterCommand _command){
        System.out.println("Command Handler: Command Added: " + _command);
        commands.put(_command.name, _command);
        return _command;
    }

    public static TreeMap<String, MasterCommand> getSubcommandList(){
        return commands;
    }
}
