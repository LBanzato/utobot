package nl.focalor.utobot.irc.input;

import java.util.List;
import nl.focalor.utobot.base.input.IResult;
import nl.focalor.utobot.base.input.handler.IInputHandler;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * @author focalor
 */
public interface IIrcInputHandler extends IInputHandler {

	public List<String> getCommandNames();

	public IResult handleCommand(MessageEvent<PircBotX> event);
}
