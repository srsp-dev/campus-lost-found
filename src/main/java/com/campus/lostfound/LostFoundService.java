
package com.campus.lostfound;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

public class LostFoundService {

    private final List<LostFoundItem> items = new ArrayList<>();

    // Report a lost or found item
    public LostFoundItem reportItem(
            String name,
            String category,
            String description,
            String location,
            LocalDate date,
            ItemStatus status) {

        validateText(name, "Item name");
        validateText(category, "Category");
        validateText(description, "Description");
        validateText(location, "Location");

        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null.");
        }

        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the future.");
        }

        if (status != ItemStatus.LOST && status != ItemStatus.FOUND) {
            throw new IllegalArgumentException(
                    "A new report must have LOST or FOUND status.");
        }

        LostFoundItem item = new LostFoundItem(
                name.trim(),
                category.trim(),
                description.trim(),
                location.trim(),
                date,
                status
        );

        items.add(item);
        return item;
    }

    // Return all reported items
    public List<LostFoundItem> getAllItems() {
        return new ArrayList<>(items);
    }

    // Search by item name, category, description, or location
    public List<LostFoundItem> searchItems(String keyword) {
        validateText(keyword, "Search keyword");

        String search = keyword.trim().toLowerCase(Locale.ROOT);

        return items.stream()
                .filter(item ->
                        item.getName().toLowerCase(Locale.ROOT).contains(search)
                        || item.getCategory().toLowerCase(Locale.ROOT).contains(search)
                        || item.getDescription().toLowerCase(Locale.ROOT).contains(search)
                        || item.getLocation().toLowerCase(Locale.ROOT).contains(search))
                .toList();
    }

    // Find an item by its unique ID
    public LostFoundItem getItemById(String id) {
        validateText(id, "Item ID");

        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException("Item not found: " + id));
    }

    // Claim a found item
    public LostFoundItem claimItem(String id) {
        LostFoundItem item = getItemById(id);

        if (item.getStatus() != ItemStatus.FOUND) {
            throw new IllegalStateException(
                    "Only found items can be claimed.");
        }

        LostFoundItem claimed = copyWithStatus(item, ItemStatus.CLAIMED);
        replaceItem(claimed);

        return claimed;
    }

    // Return an item after it has been claimed
    public LostFoundItem returnItem(String id) {
        LostFoundItem item = getItemById(id);

        if (item.getStatus() != ItemStatus.CLAIMED) {
            throw new IllegalStateException(
                    "Only claimed items can be returned.");
        }

        LostFoundItem returned = copyWithStatus(item, ItemStatus.RETURNED);
        replaceItem(returned);

        return returned;
    }

    private LostFoundItem copyWithStatus(
            LostFoundItem item,
            ItemStatus status) {

        LostFoundItem copy = new LostFoundItem(
                item.getName(),
                item.getCategory(),
                item.getDescription(),
                item.getLocation(),
                item.getDate(),
                status
        );

        // Preserve the original ID when changing status.
        return new LostFoundItemWithSameId(item, status);
    }

    private void replaceItem(LostFoundItem updated) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(updated.getId())) {
                items.set(i, updated);
                return;
            }
        }

        throw new NoSuchElementException("Item not found.");
    }

    private void validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank.");
        }
    }

    // Internal helper to preserve the original item ID.
    private static class LostFoundItemWithSameId extends LostFoundItem {

        private final String originalId;

        LostFoundItemWithSameId(LostFoundItem original, ItemStatus status) {
            super(
                    original.getName(),
                    original.getCategory(),
                    original.getDescription(),
                    original.getLocation(),
                    original.getDate(),
                    status
            );

            this.originalId = original.getId();
        }

        @Override
        public String getId() {
            return originalId;
        }
    }
}
