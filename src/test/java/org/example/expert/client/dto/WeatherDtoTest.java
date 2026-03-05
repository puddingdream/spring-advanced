package org.example.expert.client.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherDtoTest {

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void weather_dto를_생성한다() {
        // given
        String date = "03-05";
        String weather = "맑음";

        // when
        WeatherDto dto = new WeatherDto(date, weather);

        // then
        assertEquals(date, dto.getDate());
        assertEquals(weather, dto.getWeather());
    }
}
