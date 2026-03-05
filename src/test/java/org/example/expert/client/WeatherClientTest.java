package org.example.expert.client;

import org.example.expert.client.dto.WeatherDto;
import org.example.expert.domain.common.exception.ServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class WeatherClientTest {

    @Mock
    private RestTemplateBuilder builder;
    @Mock
    private RestTemplate restTemplate;

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 날씨_조회_성공_시_오늘_날씨를_반환한다() {
        // given
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd"));
        WeatherDto[] body = {new WeatherDto(today, "맑음")};
        given(builder.build()).willReturn(restTemplate);
        given(restTemplate.getForEntity(any(java.net.URI.class), eq(WeatherDto[].class)))
                .willReturn(new ResponseEntity<>(body, HttpStatus.OK));
        WeatherClient weatherClient = new WeatherClient(builder);

        // when
        String result = weatherClient.getTodayWeather();

        // then
        assertEquals("맑음", result);
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 날씨_API_상태코드가_OK가_아니면_에러가_발생한다() {
        // given
        given(builder.build()).willReturn(restTemplate);
        given(restTemplate.getForEntity(any(java.net.URI.class), eq(WeatherDto[].class)))
                .willReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));
        WeatherClient weatherClient = new WeatherClient(builder);

        // when
        ServerException exception = assertThrows(ServerException.class, weatherClient::getTodayWeather);

        // then
        assertEquals("날씨 데이터를 가져오는데 실패했습니다. 상태 코드: 500 INTERNAL_SERVER_ERROR", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 날씨_데이터가_비어있으면_에러가_발생한다() {
        // given
        given(builder.build()).willReturn(restTemplate);
        given(restTemplate.getForEntity(any(java.net.URI.class), eq(WeatherDto[].class)))
                .willReturn(new ResponseEntity<>(new WeatherDto[0], HttpStatus.OK));
        WeatherClient weatherClient = new WeatherClient(builder);

        // when
        ServerException exception = assertThrows(ServerException.class, weatherClient::getTodayWeather);

        // then
        assertEquals("날씨 데이터가 없습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 날씨_데이터_body가_null이면_에러가_발생한다() {
        // given
        given(builder.build()).willReturn(restTemplate);
        given(restTemplate.getForEntity(any(java.net.URI.class), eq(WeatherDto[].class)))
                .willReturn(new ResponseEntity<>(null, HttpStatus.OK));
        WeatherClient weatherClient = new WeatherClient(builder);

        // when
        ServerException exception = assertThrows(ServerException.class, weatherClient::getTodayWeather);

        // then
        assertEquals("날씨 데이터가 없습니다.", exception.getMessage());
    }

    @SuppressWarnings("NonAsciiCharacters")
    @Test
    void 오늘_날짜와_일치하는_날씨가_없으면_에러가_발생한다() {
        // given
        WeatherDto[] body = {new WeatherDto("01-01", "눈")};
        given(builder.build()).willReturn(restTemplate);
        given(restTemplate.getForEntity(any(java.net.URI.class), eq(WeatherDto[].class)))
                .willReturn(new ResponseEntity<>(body, HttpStatus.OK));
        WeatherClient weatherClient = new WeatherClient(builder);

        // when
        ServerException exception = assertThrows(ServerException.class, weatherClient::getTodayWeather);

        // then
        assertEquals("오늘에 해당하는 날씨 데이터를 찾을 수 없습니다.", exception.getMessage());
    }
}
