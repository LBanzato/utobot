package nl.focalor.utobot.utopia.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nl.focalor.utobot.base.input.CommandInput;
import nl.focalor.utobot.base.input.IResult;
import nl.focalor.utobot.base.input.MultiReplyResult;
import nl.focalor.utobot.base.input.handler.AbstractCommandHandler;
import nl.focalor.utobot.base.model.Person;
import nl.focalor.utobot.base.model.service.IPersonService;
import nl.focalor.utobot.utopia.model.Attack;
import nl.focalor.utobot.utopia.model.SpellCast;
import nl.focalor.utobot.utopia.service.IAttackService;
import nl.focalor.utobot.utopia.service.ISpellService;
import nl.focalor.utobot.utopia.service.IUtopiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author focalor
 */
@Component
public class ShowStatusHandler extends AbstractCommandHandler {
	public static final String COMMAND = "status";
	@Autowired
	private IPersonService personService;
	@Autowired
	private IAttackService attackService;
	@Autowired
	private ISpellService spellService;
	@Autowired
	private IUtopiaService utopiaService;

	public ShowStatusHandler() {
		super(COMMAND);
	}

	@Override
	public IResult handleCommand(CommandInput event) {
		String name = event.getArgument() == null ? event.getSource() : event.getArgument();

		List<Attack> attacks;
		List<SpellCast> spellCasts;
		Person person = personService.find(name, false);
		if (person == null) {
			attacks = attackService.find(null, name);
			spellCasts = spellService.find(null, name);
		} else {
			attacks = attackService.find(person.getId(), null);
			spellCasts = spellService.find(person.getId(), null);
		}

		List<String> messages = new ArrayList<>();
		messages.add("Status for " + ((person == null) ? name : person.getName()));
		messages.add("Armies out:");
		//@formatter:off
		messages.addAll(attacks.stream()
					.sorted((left, right) -> left.getReturnDate().compareTo(right.getReturnDate()))
					.map(attack -> attack.toString())
					.collect(Collectors.toList()));
		//@formatter:on
		messages.add("Active spells:");
		messages.addAll(spellCasts.stream().map(this::toMessage).collect(Collectors.toList()));
		return new MultiReplyResult(messages);
	}

	private String toMessage(SpellCast cast) {
		StringBuilder builder = new StringBuilder();
		builder.append(cast.getSpellId());
		builder.append(" ends in ");

		int hoursLeft = cast.getLastHour() - utopiaService.getHourOfAge() + 1;
		builder.append(hoursLeft);
		builder.append(" days");
		return builder.toString();
	}
}
