package com.idealizer.review_x.domain.profile.game.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "profile_game_logs")
@CompoundIndexes({
        @CompoundIndex(name = "uq_profile_game_day",
                def = "{'profile_game_id': 1, user_id: 1, 'year': 1, 'month': 1, 'day': 1}", unique = true)
})
public class ProfileGameLog {
    @Id
    private ObjectId id;
    @Indexed
    @Field("user_id")
    private ObjectId userId;
    @Indexed
    @Field("profile_game_id")
    private ObjectId profileGameId;
    private int year;
    private int month;
    private int day;
    @Field("minutes_played")
    private int minutesPlayed;
    private String note;


    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProfileGameId() {
        return profileGameId;
    }

    public void setProfileGameId(ObjectId profileGameId) {
        this.profileGameId = profileGameId;
    }


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public void setMinutesPlayed(int minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
