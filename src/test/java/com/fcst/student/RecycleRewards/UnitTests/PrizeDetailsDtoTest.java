package com.fcst.student.RecycleRewards.UnitTests;

import com.fcst.student.RecycleRewards.model.PrizeDetailsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PrizeDetailsDtoTest {

    private PrizeDetailsDto prizeDetailsDto;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        prizeDetailsDto = new PrizeDetailsDto(1L, 12345L, "PrizeName", "PrizeDescription", now, "WinnerCode");
    }

    @Test
    public void testDefaultConstructor() {
        PrizeDetailsDto dto = new PrizeDetailsDto();
        assertNull(dto.getPrizeId());
        assertNull(dto.getPrizeCode());
        assertNull(dto.getPrizeName());
        assertNull(dto.getPrizeDescription());
        assertNull(dto.getPrizeDate());
        assertNull(dto.getWinnerCode());
    }

    @Test
    public void testConstructorWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        PrizeDetailsDto dto = new PrizeDetailsDto(1L, 12345L, "PrizeName", "PrizeDescription", now, "WinnerCode");
        assertEquals(1L, dto.getPrizeId());
        assertEquals(12345L, dto.getPrizeCode());
        assertEquals("PrizeName", dto.getPrizeName());
        assertEquals("PrizeDescription", dto.getPrizeDescription());
        assertEquals(now, dto.getPrizeDate());
        assertEquals("WinnerCode", dto.getWinnerCode());
    }

    @Test
    public void testConstructorWithoutPrizeId() {
        LocalDateTime now = LocalDateTime.now();
        PrizeDetailsDto dto = new PrizeDetailsDto(12345L, "PrizeName", "PrizeDescription", now, "WinnerCode");
        assertNull(dto.getPrizeId());
        assertEquals(12345L, dto.getPrizeCode());
        assertEquals("PrizeName", dto.getPrizeName());
        assertEquals("PrizeDescription", dto.getPrizeDescription());
        assertEquals(now, dto.getPrizeDate());
        assertEquals("WinnerCode", dto.getWinnerCode());
    }

    @Test
    public void testConstructorWithoutPrizeIdAndDate() {
        PrizeDetailsDto dto = new PrizeDetailsDto(12345L, "PrizeName", "PrizeDescription", "WinnerCode");
        assertNull(dto.getPrizeId());
        assertEquals(12345L, dto.getPrizeCode());
        assertEquals("PrizeName", dto.getPrizeName());
        assertEquals("PrizeDescription", dto.getPrizeDescription());
        assertNull(dto.getPrizeDate());
        assertEquals("WinnerCode", dto.getWinnerCode());
    }

    @Test
    public void testConstructorWithoutPrizeDate() {
        PrizeDetailsDto dto = new PrizeDetailsDto(1L, 12345L, "PrizeName", "PrizeDescription", "WinnerCode");
        assertEquals(1L, dto.getPrizeId());
        assertEquals(12345L, dto.getPrizeCode());
        assertEquals("PrizeName", dto.getPrizeName());
        assertEquals("PrizeDescription", dto.getPrizeDescription());
        assertNull(dto.getPrizeDate());
        assertEquals("WinnerCode", dto.getWinnerCode());
    }

    @Test
    public void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();

        prizeDetailsDto.setPrizeId(2L);
        assertEquals(2L, prizeDetailsDto.getPrizeId());

        prizeDetailsDto.setPrizeCode(54321L);
        assertEquals(54321L, prizeDetailsDto.getPrizeCode());

        prizeDetailsDto.setPrizeName("NewPrizeName");
        assertEquals("NewPrizeName", prizeDetailsDto.getPrizeName());

        prizeDetailsDto.setPrizeDescription("NewPrizeDescription");
        assertEquals("NewPrizeDescription", prizeDetailsDto.getPrizeDescription());

        prizeDetailsDto.setPrizeDate(now);
        assertEquals(now, prizeDetailsDto.getPrizeDate());

        prizeDetailsDto.setWinnerCode("NewWinnerCode");
        assertEquals("NewWinnerCode", prizeDetailsDto.getWinnerCode());
    }
}