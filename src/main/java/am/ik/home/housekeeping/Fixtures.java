package am.ik.home.housekeeping;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import am.ik.home.housekeeping.mission.ImmutableMission;
import am.ik.home.housekeeping.mission.Mission;
import am.ik.home.housekeeping.mission.MissionId;

public class Fixtures {
	public static List<Mission> missions() {
		return List.of(ImmutableMission.builder()
						.missionId(MissionId.valueOf(1))
						.place("洗面所")
						.lastDate(LocalDate.of(2020, 3, 20))
						.cycle(Period.ofDays(14))
						.build(),
				ImmutableMission.builder()
						.missionId(MissionId.valueOf(2))
						.place("キッチン")
						.lastDate(LocalDate.of(2020, 4, 15))
						.cycle(Period.ofDays(30))
						.build(),
				ImmutableMission.builder()
						.missionId(MissionId.valueOf(3))
						.place("トイレ")
						.lastDate(LocalDate.of(2020, 4, 19))
						.cycle(Period.ofDays(14))
						.build(),
				ImmutableMission.builder()
						.missionId(MissionId.valueOf(3))
						.place("床")
						.lastDate(LocalDate.of(2020, 5, 2))
						.cycle(Period.ofDays(2))
						.build());
	}
}
