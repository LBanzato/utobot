package nl.focalor.utobot.utopia.job;

import nl.focalor.utobot.base.jobs.IScheduledJob;
import nl.focalor.utobot.base.service.IBotService;
import nl.focalor.utobot.utopia.model.SpellCast;
import nl.focalor.utobot.utopia.service.ISpellService;

public class SpellCastCompletedJob implements IScheduledJob {
	private final IBotService botService;
	private final ISpellService spellService;
	private final SpellCast cast;

	public SpellCastCompletedJob(IBotService botService, ISpellService spellService, SpellCast cast) {
		super();
		this.botService = botService;
		this.spellService = spellService;
		this.cast = cast;
	}

	@Override
	public void run() {
		String msg = cast.getSpellId() + " ended";
		botService.broadcast(msg);
		spellService.delete(cast.getId());
	}

}