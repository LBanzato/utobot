package nl.focalor.utobot.base.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import nl.focalor.utobot.utopia.service.IUtopiaService;

public class HourlyJob implements IScheduledJob {
	private final IUtopiaService utopiaService;
	private final TreeMap<Integer, List<IScheduledJob>> hourlyJobs = new TreeMap<>();

	public HourlyJob(IUtopiaService utopiaService) {
		super();
		this.utopiaService = utopiaService;
	}

	public synchronized void addHourlyJob(int hour, IScheduledJob job) {
		if (utopiaService.getHourOfAge() > hour) {
			job.run();
		} else {
			List<IScheduledJob> jobs = hourlyJobs.get(hour);
			if (jobs == null) {
				jobs = new ArrayList<>();
				hourlyJobs.put(hour, jobs);
			}
			jobs.add(job);
		}
	}

	@Override
	public void run() {
		System.out.println(utopiaService.getUtopiaDate().toString(false));

		SortedMap<Integer, List<IScheduledJob>> completedJobs = hourlyJobs.headMap(utopiaService.getHourOfAge());
		for (Entry<Integer, List<IScheduledJob>> entry : completedJobs.entrySet()) {
			hourlyJobs.remove(entry.getKey());
			for (IScheduledJob job : entry.getValue()) {
				job.run();
			}
		}
		return;
	}

}
