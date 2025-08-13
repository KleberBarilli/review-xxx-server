package com.idealizer.review_x.common.dtos.profile.game;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;

import java.util.*;

public class UpdateFavoriteGameDTO {

    @NotNull
    @Size(max = 10, message = "You can only have up to 10 favorite games")
    @Valid
    private List<FavoriteGameOrderDTO> favorites;

    public List<FavoriteGameOrderDTO> getFavorites() {
        return favorites;
    }


    public void setFavorites(List<FavoriteGameOrderDTO> favorites) {
        this.favorites = favorites;
    }

    public List<FavoriteGameEntry> toCommandList() {
        if (favorites == null) {
            return Collections.emptyList();
        }

        Set<Integer> uniqueOrders = new HashSet<>();
        Set<ObjectId> uniqueGameIds = new HashSet<>();

        List<FavoriteGameEntry> converted = new ArrayList<>();

        for (FavoriteGameOrderDTO dto : favorites) {
            ObjectId gameId = new ObjectId(dto.getGameId());
            int order = dto.getOrder();

            if (!uniqueOrders.add(order)) {
                throw new IllegalArgumentException("Duplicate favorite order: " + order);
            }

            if (!uniqueGameIds.add(gameId)) {
                throw new IllegalArgumentException("Duplicate gameId: " + gameId);
            }

            converted.add(new FavoriteGameEntry(gameId, order));
        }

        return converted;
    }

    public static class FavoriteGameEntry {
        private final ObjectId gameId;
        private final int order;

        public FavoriteGameEntry(ObjectId gameId, int order) {
            this.gameId = gameId;
            this.order = order;
        }

        public ObjectId getGameId() {
            return gameId;
        }

        public int getOrder() {
            return order;
        }
    }

    public static class FavoriteGameOrderDTO {
        @NotBlank
        private String gameId;

        @Min(1)
        @Max(10)
        private int order;

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }
}