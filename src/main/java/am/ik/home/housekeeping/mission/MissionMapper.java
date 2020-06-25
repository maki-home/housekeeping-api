package am.ik.home.housekeeping.mission;

import java.sql.Date;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class MissionMapper {
	private final NamedParameterJdbcTemplate jdbcTemplate;

	public MissionMapper(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Mission> getLatestMissions() {
		return this.jdbcTemplate.query("SELECT m.mission_id, m.place, m.cycle, max(e.date) AS last_date "
						+ "FROM mission AS m "
						+ "LEFT OUTER JOIN mission_event e ON m.mission_id = e.mission_id "
						+ "GROUP BY m.mission_id "
						+ "ORDER BY cycle;",
				(rs, rowNum) -> {
					final Date lastDate = rs.getDate("last_date");
					return ImmutableMission.builder()
							.missionId(MissionId.valueOf(rs.getInt("mission_id")))
							.place(rs.getString("place"))
							.cycle(Period.ofDays(rs.getInt("cycle")))
							.lastDate(lastDate == null ? null : lastDate.toLocalDate())
							.build();
				});
	}

	@Transactional
	public MissionId insertMission(String place, Period cycle) {
		final MapSqlParameterSource source = new MapSqlParameterSource()
				.addValue("place", place)
				.addValue("cycle", cycle.getDays());
		return this.jdbcTemplate.queryForObject("INSERT INTO mission(place, cycle) VALUES (:place, :cycle) RETURNING mission_id",
				source, (rs, rowNum) -> MissionId.valueOf(rs.getInt("mission_id")));
	}

	@Transactional
	public int[] insertMissionEvents(List<MissionCompleteEvent> events) {
		final SqlParameterSource[] sources = events.stream()
				.map(event -> new MapSqlParameterSource()
						.addValue("missionId", event.missionId().value())
						.addValue("date", event.date())
						.addValue("username", event.username()))
				.toArray(SqlParameterSource[]::new);
		return this.jdbcTemplate.batchUpdate("INSERT INTO mission_event(mission_id, date, username) VALUES(:missionId, :date, :username)", sources);
	}

	@Transactional
	public int removeMissions(List<MissionId> missionIds) {
		final List<Integer> ids = missionIds.stream().map(MissionId::value).collect(Collectors.toUnmodifiableList());
		return this.jdbcTemplate.update("DELETE FROM mission WHERE mission_id IN (:missionIds)",
				new MapSqlParameterSource().addValue("missionIds", ids));
	}
}
