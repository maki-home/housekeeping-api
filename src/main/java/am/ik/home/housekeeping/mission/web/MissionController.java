package am.ik.home.housekeeping.mission.web;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import am.ik.home.housekeeping.mission.ImmutableMissionCompleteEvent;
import am.ik.home.housekeeping.mission.Mission;
import am.ik.home.housekeeping.mission.MissionCompleteEvent;
import am.ik.home.housekeeping.mission.MissionId;
import am.ik.home.housekeeping.mission.MissionMapper;
import am.ik.home.housekeeping.spec.MissionNewRequest;
import am.ik.home.housekeeping.spec.MissionRegisterRequest;
import am.ik.home.housekeeping.spec.MissionRemoveRequest;
import am.ik.home.housekeeping.spec.MissionResponse;
import am.ik.home.housekeeping.spec.MissionsApi;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MissionController implements MissionsApi {
	private final MissionMapper missionMapper;


	public MissionController(MissionMapper missionMapper) {
		this.missionMapper = missionMapper;
	}

	@Override
	public ResponseEntity<Void> deleteMissions(MissionRemoveRequest request) {
		final List<MissionId> missionIds = request.getRemove().stream()
				.map(MissionId::valueOf)
				.collect(Collectors.toUnmodifiableList());
		this.missionMapper.removeMissions(missionIds);
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<List<MissionResponse>> getMissions() {
		final List<Mission> missions = this.missionMapper.getLatestMissions();
		return ResponseEntity.ok(missions.stream()
				.map(m -> new MissionResponse()
						.id(m.missionId().value())
						.place(m.place())
						.lastDate(m.lastDate())
						.cycle(m.cycle().getDays()))
				.collect(Collectors.toUnmodifiableList()));
	}

	@Override
	public ResponseEntity<MissionResponse> postMissions(MissionNewRequest request) {
		final MissionId missionId = this.missionMapper.insertMission(request.getPlace(), Period.ofDays(request.getCycle()));
		return ResponseEntity.ok(new MissionResponse()
				.id(missionId.value())
				.cycle(request.getCycle())
				.place(request.getPlace()));
	}

	@Override
	public ResponseEntity<Void> putMissions(MissionRegisterRequest request) {
		final List<MissionCompleteEvent> events = request.getFinished().stream()
				.map(id -> ImmutableMissionCompleteEvent.builder()
						.missionId(MissionId.valueOf(id))
						.date(request.getDate())
						.username("system")
						.build())
				.collect(Collectors.toUnmodifiableList());
		this.missionMapper.insertMissionEvents(events);
		return ResponseEntity.noContent().build();
	}
}
