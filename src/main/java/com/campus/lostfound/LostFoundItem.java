
package com.campus.lostfound;

import java.time.LocalDate;
import java.util.UUID;

public class LostFoundItem {

    private final String id;
    private final String name;
    private final String category;
    private final String description;
    private final String location;
    private final LocalDate date;
    private final ItemStatus status;

    public LostFoundItem(
            String name,
            String category,
            String description,
            String location,
            LocalDate date,
            ItemStatus status) {

        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.category = category;
        this.description = description;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public LocalDate getDate() {
        return date;
    }

    public ItemStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "LostFoundItem{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
