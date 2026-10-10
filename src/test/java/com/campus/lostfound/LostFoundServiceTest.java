
package com.campus.lostfound;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class LostFoundServiceTest {

    private LostFoundService service;

    @BeforeEach
    void setUp() {
        service = new LostFoundService();
    }

    @Test
    void shouldReportFoundItem() {
        LostFoundItem item = service.reportItem(
                "Water Bottle",
                "Accessories",
                "Blue bottle with a sticker",
                "Library",
                LocalDate.now(),
                ItemStatus.FOUND
        );

        assertNotNull(item.getId());
        assertEquals("Water Bottle", item.getName());
        assertEquals(ItemStatus.FOUND, item.getStatus());
        assertEquals(1, service.getAllItems().size());
    }

    @Test
    void shouldRejectBlankItemName() {
        assertThrows(IllegalArgumentException.class, () ->
                service.reportItem(
                        " ",
                        "Accessories",
                        "Blue bottle",
                        "Library",
                        LocalDate.now(),
                        ItemStatus.FOUND
                )
        );

        assertEquals(0, service.getAllItems().size());
    }

    @Test
    void shouldRejectFutureDate() {
        assertThrows(IllegalArgumentException.class, () ->
                service.reportItem(
                        "Wallet",
                        "Personal Items",
                        "Brown wallet",
                        "Canteen",
                        LocalDate.now().plusDays(1),
                        ItemStatus.LOST
                )
        );
    }

    @Test
    void shouldSearchItemsByLocation() {
        service.reportItem(
                "Notebook",
                "Stationery",
                "Black notebook",
                "Library",
                LocalDate.now(),
                ItemStatus.LOST
        );

        assertEquals(1, service.searchItems("library").size());
        assertEquals(0, service.searchItems("playground").size());
    }

    @Test
    void shouldClaimFoundItem() {
        LostFoundItem found = service.reportItem(
                "Umbrella",
                "Accessories",
                "Black umbrella",
                "Canteen",
                LocalDate.now(),
                ItemStatus.FOUND
        );

        LostFoundItem claimed = service.claimItem(found.getId());

        assertEquals(ItemStatus.CLAIMED, claimed.getStatus());
        assertEquals(found.getId(), claimed.getId());
    }

    @Test
    void shouldRejectClaimingSameItemTwice() {
        LostFoundItem found = service.reportItem(
                "Keys",
                "Personal Items",
                "Set of three keys",
                "Parking Area",
                LocalDate.now(),
                ItemStatus.FOUND
        );

        service.claimItem(found.getId());

        assertThrows(IllegalStateException.class, () ->
                service.claimItem(found.getId())
        );
    }

    @Test
    void shouldReturnClaimedItem() {
        LostFoundItem found = service.reportItem(
                "Laptop Charger",
                "Electronics",
                "USB-C charger",
                "Computer Lab",
                LocalDate.now(),
                ItemStatus.FOUND
        );

        service.claimItem(found.getId());
        LostFoundItem returned = service.returnItem(found.getId());

        assertEquals(ItemStatus.RETURNED, returned.getStatus());
        assertEquals(found.getId(), returned.getId());
    }

    @Test
    void shouldRejectReturningUnclaimedItem() {
        LostFoundItem found = service.reportItem(
                "Pen",
                "Stationery",
                "Blue ink pen",
                "Classroom",
                LocalDate.now(),
                ItemStatus.FOUND
        );

        assertThrows(IllegalStateException.class, () ->
                service.returnItem(found.getId())
        );
    }

    @Test
    void shouldRejectUnknownItemId() {
        assertThrows(NoSuchElementException.class, () ->
                service.getItemById("nonexistent-id")
        );
    }
}
