package am.ik.home.housekeeping.mission;

import java.time.LocalDate;

import org.immutables.value.Value.Immutable;

@Immutable
public abstract class MissionCompleteEvent {
	public abstract MissionId missionId();

	public abstract LocalDate date();

	public abstract String username();
}
