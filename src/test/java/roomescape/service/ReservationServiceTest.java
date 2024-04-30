package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.reservation.ReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.controller.time.TimeResponse;
import roomescape.exception.DuplicateReservation;
import roomescape.exception.PreviousTimeException;
import roomescape.exception.TimeNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                new ReservationFakeRepository(),
                new ReservationTimeFakeRepository()
        );
    }

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getReservations() {
        // given
        List<ReservationResponse> expected = List.of(
                new ReservationResponse(1L, "al", "2025-01-20", new TimeResponse(1L, "10:15")),
                new ReservationResponse(2L, "be", "2025-02-19", new TimeResponse(2L, "11:20"))
        );

        // when
        List<ReservationResponse> actual = reservationService.getReservations();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        // given
        ReservationRequest request = new ReservationRequest("cha", "2025-03-18", 3L);
        ReservationResponse expected = new ReservationResponse(3L, "cha", "2025-03-18", new TimeResponse(3L, "12:25"));

        // when
        ReservationResponse actual = reservationService.addReservation(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하는 예약을 삭제한다.")
    void deleteReservationPresent() {
        // given
        Long id = 2L;

        // when & then
        assertThat(reservationService.deleteReservation(id)).isOne();
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제한다.")
    void deleteReservationNotPresent() {
        // given
        Long id = 3L;

        // when & then
        assertThat(reservationService.deleteReservation(id)).isZero();
    }

    @Test
    @DisplayName("존재하지 않는 시간(id)로 예약을 할 때 예외가 발생한다.")
    void addReservationNonExistTime() {
        ReservationRequest request = new ReservationRequest("redddy", "2024-06-21", 100L);
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(TimeNotFoundException.class);
    }

    @Test
    @DisplayName("예약하려는 시간이 현재 시간보다 이전일 경우 예외가 발생한다.")
    void validateReservationTimeAfterThanNow() {
        assertThatThrownBy(() -> reservationService.addReservation(new ReservationRequest("cha", "2024-04-30", 1L)))
                .isInstanceOf(PreviousTimeException.class);
    }

    @Test
    @DisplayName("중복된 시간으로 예약을 할 때 예외가 발생한다.")
    void duplicateDateTimeReservation() {
        //given
        final ReservationRequest request = new ReservationRequest("레디", "2025-11-20", 1L);

        //when
        reservationService.addReservation(request);

        //then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(DuplicateReservation.class);
    }
}
