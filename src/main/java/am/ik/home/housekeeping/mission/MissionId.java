package am.ik.home.housekeeping.mission;

public interface MissionId {
	int value();

	static MissionId valueOf(final int value) {
		return () -> value;
	}
}
