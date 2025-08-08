package kori.tour.tour.domain.area_code;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kori.tour.tour.adapter.out.persistence.area_code.Area;
import kori.tour.tour.adapter.out.persistence.area_code.AreaCodeParser;

class AreaCodeParserTest {

	AreaCodeParser areaCodeParser = new AreaCodeParser();

	@Test
	@DisplayName("Json에서 읽어와서 정상변환")
	void test() {
		// given
		String json = "[{\"code\":\"1\",\"name\":\"서울\",\"subRegions\":[{\"code\":\"1\",\"name\":\"강남구\"},{\"code\":\"2\",\"name\":\"강북구\"}]},{\"code\":\"2\",\"name\":\"대구\",\"subRegions\":[{\"code\":\"1\",\"name\":\"중구\"},{\"code\":\"2\",\"name\":\"동구\"}]}]";
		// when
		List<Area> areas = areaCodeParser.parseToAreaCode(json);

		// then
		assertThat(areas.size()).isEqualTo(2);
		assertThat(areas.get(0).subRegions().size()).isEqualTo(2);
	}

}
