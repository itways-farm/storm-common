package com.stormfarm.common.repository;

import com.stormfarm.common.entity.enums.DeviceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LikePatternTest {

    @Test
    void wildcardsAndTheEscapeCharacterAreEscaped() {
        assertEquals("!%", LikePattern.escape("%"));
        assertEquals("!_", LikePattern.escape("_"));
        assertEquals("!!", LikePattern.escape("!"));
        assertEquals("50!%!_off!!", LikePattern.escape("50%_off!"));
    }

    @Test
    void plainTextAndNullAreUnchanged() {
        assertEquals("Pixel 7", LikePattern.escape("Pixel 7"));
        assertEquals("", LikePattern.escape(""));
        assertNull(LikePattern.escape(null));
    }

    @Test
    void deviceSearchesEscapeTheRawTextBeforeQuerying() {
        DeviceRepository devices = mock(DeviceRepository.class, CALLS_REAL_METHODS);
        Pageable page = PageRequest.of(0, 20);

        devices.findFiltered(DeviceStatus.AVAILABLE, null, "%", page);
        verify(devices).findFilteredEscaped(DeviceStatus.AVAILABLE, null, "!%", page);

        devices.findFilteredWithBrand(null, null, "sam_", "a!b", page);
        verify(devices).findFilteredWithBrandEscaped(null, null, "sam!_", "a!!b", page);
    }
}
