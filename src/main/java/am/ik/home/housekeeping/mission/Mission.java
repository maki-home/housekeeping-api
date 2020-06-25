package am.ik.home.housekeeping.mission;

import java.time.LocalDate;
import java.time.Period;

import org.immutables.value.Value.Immutable;

import org.springframework.lang.Nullable;

@Immutable
public abstract class Mission {
	public abstract MissionId missionId();

	public abstract String place();

	public abstract Period cycle();

	@Nullable
	public abstract LocalDate lastDate();
}
