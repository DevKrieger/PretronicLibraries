package net.prematic.libraries.command.command;

import net.prematic.libraries.command.sender.CommandSender;
import net.prematic.libraries.utility.GeneralUtil;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.10.18 14:31
 *
 */

public class MainCommand extends Command {

    private List<SubCommand> subCommands;

    public MainCommand(String name) {
        super(name);
        this.subCommands = new LinkedList<>();
    }

    public MainCommand(String name, String description) {
        super(name, description);
        this.subCommands = new LinkedList<>();
    }

    public MainCommand(String name, String description, String permission) {
        super(name, description, permission);
        this.subCommands = new LinkedList<>();
    }

    public MainCommand(String name, String description, String permission, String usage, String... aliases) {
        super(name, description, permission, usage, aliases);
        this.subCommands = new LinkedList<>();
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    protected int getMaxPages() {
        return GeneralUtil.getMaxPages(8, subCommands);
    }

    public void registerSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    public void sendHelp(CommandSender sender, int page) {
        int maxPages = getMaxPages();
        if(page > maxPages) page = 1;
        int nextPage = page+1;
        if(nextPage > maxPages) nextPage = 1;
        if(nextPage == page) sender.sendMessage("Seite " + page + "/" + maxPages);
        else sender.sendMessage("Seite " + page + "/" + maxPages + " | Weitere Hilfeseiten mit " + getName() + " " + nextPage);
        int from = 1;
        if(page > 1) from = 8 * (page - 1) + 1;
        int to = 8 * page;
        for(int h = from; h <= to; h++) {
            if(h > subCommands.size()) break;
            SubCommand subCommand = subCommands.get(h - 1);
            if(sender.hasPermission(subCommand.getPermission())) {
                sender.sendMessage(getName()+(subCommand.getUsage() != null ? " " + subCommand.getUsage() : "") + (subCommand.getDescription() != null ? " " + subCommand.getDescription() : ""));
                if(!subCommand.getSubCommands().isEmpty()){
                    String helpMessage = "";
                    for(SubCommand nextSubCommand : subCommand.getSubCommands()) {
                        helpMessage+=getName() +" "+ subCommand.getName()+(nextSubCommand.getUsage() != null ? " " + nextSubCommand.getUsage() : "")+(nextSubCommand.getDescription() != null ? " " + nextSubCommand.getDescription() : "")+"\n";
                    }
                    sender.sendMessage(helpMessage);
                }
            }
        }
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        executeMainCommand(sender, args);
        if(args.length >= 1) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.hasAliases(args[0])) {
                    subCommand.executeSubCommand(sender, Arrays.copyOfRange(args, 1, args.length));
                    subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                    return;
                }
            }
        }
        if(!(this instanceof SubCommand)) {
            if (args.length == 1) {
                if (GeneralUtil.isNumber(args[0])) {
                    sendHelp(sender, Integer.valueOf(args[0]));
                    return;
                }
            }
            sendHelp(sender, 1);
        }
    }

    public void executeMainCommand(CommandSender sender, String[] args) {

    }
}