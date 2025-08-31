package kori.tour.tour.adapter.out.persistence;

import java.sql.*;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import kori.tour.tour.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TourJdbcRepository {

	private final JdbcTemplate jdbcTemplate;

	public void saveTours(List<Tour> tours) { // 테스트가 끝났으면 지우는게 맞는데.. 일단 그냥 두도로 하자 1월 31일
		String sql = "INSERT INTO tour (language, road_address, basic_address, area_code, sigun_gu_code, "
				+ "content_id, content_type_id, event_start_date, event_end_date, main_image_url, mapx, "
				+ "mapy, m_level, modified_time, telephone, title) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Tour tour = tours.get(i);

				ps.setString(1, tour.getLanguage().name());
				ps.setString(2, tour.getRoadAddress());
				ps.setString(3, tour.getBasicAddress());
				ps.setString(4, tour.getRegionCode().getAreaCode());
				ps.setString(5, tour.getRegionCode().getSigunGuCode());
				ps.setString(6, tour.getContentId());
				ps.setString(7, tour.getContentTypeId().name());
				ps.setObject(8, tour.getEventStartDate());
				ps.setObject(9, tour.getEventEndDate());
				ps.setString(10, tour.getMainImageUrl());
				ps.setString(11, tour.getMapX());
				ps.setString(12, tour.getMapY());
				ps.setString(13, tour.getMLevel());
				ps.setObject(14, tour.getModifiedTime());
				ps.setString(15, tour.getTelephone());
				ps.setString(16, tour.getTitle());
			}

			@Override
			public int getBatchSize() {
				return tours.size();
			}
		});
	}

	public void saveTourDetailInfos(List<TourDetail> tourDetailInfos) {
		String sql = "INSERT INTO tour_detail (tour_id, age_limit, booking_place, discount_info_festival, "
				+ "event_homepage, event_place, place_info, play_time, program, spend_time_festival, sponsor, "
				+ "sponsor_telephone, sub_event, use_time_festival) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TourDetail detailInfo = tourDetailInfos.get(i);

				ps.setLong(1, detailInfo.getTour().getId());
				ps.setString(2, detailInfo.getAgeLimit());
				ps.setString(3, detailInfo.getBookingPlace());
				ps.setString(4, detailInfo.getDiscountInfoFestival());
				ps.setString(5, detailInfo.getEventHomepage());
				ps.setString(6, detailInfo.getEventPlace());
				ps.setString(7, detailInfo.getPlaceInfo());
				ps.setString(8, detailInfo.getPlayTime());
				ps.setString(9, detailInfo.getProgram());
				ps.setString(10, detailInfo.getSpendTimeFestival());
				ps.setString(11, detailInfo.getSponsor());
				ps.setString(12, detailInfo.getSponsorTelephone());
				ps.setString(13, detailInfo.getSubEvent());
				ps.setString(14, detailInfo.getUseTimeFestival());
			}

			@Override
			public int getBatchSize() {
				return tourDetailInfos.size();
			}
		});
	}

	public void saveTourRepeats(List<TourRepeat> tourRepeats) {
		String sql = "INSERT INTO tour_repeat (tour_id, serial_number, info_name, info_text) " + "VALUES (?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TourRepeat tourRepeat = tourRepeats.get(i);

				ps.setLong(1, tourRepeat.getTour().getId()); // ManyToOne 관계에서 Tour ID 참조
				ps.setString(2, tourRepeat.getSerialNumber());
				ps.setString(3, tourRepeat.getInfoName());
				ps.setString(4, tourRepeat.getInfoText());
			}

			@Override
			public int getBatchSize() {
				return tourRepeats.size();
			}
		});
	}

	public void saveTourImages(List<TourImage> tourImages) {
		String sql = "INSERT INTO tour_image (tour_id, origin_image_url, small_image_url, image_name, serial_number) "
				+ "VALUES (?, ?, ?, ?, ?)";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				TourImage tourImage = tourImages.get(i);

				ps.setLong(1, tourImage.getTour().getId()); // ManyToOne 관계에서 참조되는 Tour의
															// ID
				ps.setString(2, tourImage.getOriginImageUrl());
				ps.setString(3, tourImage.getSmallImageUrl());
				ps.setString(4, tourImage.getImageName());
				ps.setString(5, tourImage.getSerialNumber());
			}

			@Override
			public int getBatchSize() {
				return tourImages.size();
			}
		});
	}

}
