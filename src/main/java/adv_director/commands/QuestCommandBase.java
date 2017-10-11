package adv_director.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import adv_director.api.api.QuestingAPI;
import adv_director.storage.NameCache;

public abstract class QuestCommandBase
{
	public abstract String getCommand();
	
	public String getUsageSuffix()
	{
		return "";
	}
	
	/**
	 * Are the passed arguments valid?<br>
	 * NOTE: Argument 1 is always the returned value of getCommand()
	 */
	public boolean validArgs(String[] args)
	{
		return args.length == 1;
	}
	
	public List<String> autoComplete(MinecraftServer server, ICommandSender sender, String[] args)
	{
		return new ArrayList<String>();
	}
	
	public abstract void runCommand(MinecraftServer server, CommandBase command, ICommandSender sender, String[] args) throws CommandException;
	
	public final WrongUsageException getException(CommandBase command)
	{
		String message = command.getCommandName() + " " + getCommand();
		
		if(getUsageSuffix().length() > 0)
		{
			message += " " + getUsageSuffix();
		}
		
		return new WrongUsageException(message);
	}
	
	/**
	 * Attempts to find the players ID from the given name or convert it to a UUID if valid
	 */
	public UUID findPlayerID(MinecraftServer server, ICommandSender sender, String name)
	{
		UUID playerID = null;
		
		EntityPlayerMP player = null;
		
		try
		{
			player = CommandBase.getEntity(server, sender, name, EntityPlayerMP.class);
		} catch(Exception e)
		{
			player = null;
		}
		
		if(player == null)
		{
			if(name.startsWith("@"))
			{
				return null;
			}
			
			try
			{
				playerID = UUID.fromString(name);
			} catch(Exception e)
			{
				playerID = NameCache.INSTANCE.getUUID(name);
			}
		} else
		{
			playerID = QuestingAPI.getQuestingUUID(player);
		}
		
		return playerID;
	}
	
	public boolean isArgUsername(String[] args, int index)
	{
		return false;
	}
}