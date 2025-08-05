package com.idealizer.review_x.application.games.profile.game.usecases;

import com.idealizer.review_x.application.games.profile.game.commands.CreateUpdateProfileGameCommand;
import com.idealizer.review_x.common.dtos.UpdateFavoriteGameDTO;
import com.idealizer.review_x.domain.game.entities.Game;
import com.idealizer.review_x.domain.game.repositories.GameRepository;
import com.idealizer.review_x.domain.profile.game.ProfileGame;
import com.idealizer.review_x.domain.profile.game.repositories.ProfileGameRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UpdateFavoriteGamesUseCase {

    private final Logger logger = Logger.getLogger(UpdateFavoriteGamesUseCase.class.getName());

    private final ProfileGameRepository profileGameRepository;
    private final CreateProfileGameUseCase createProfileGameUseCase;
    private final GameRepository gameRepository;

    public UpdateFavoriteGamesUseCase(ProfileGameRepository profileGameRepository, CreateProfileGameUseCase createProfileGameUseCase, GameRepository gameRepository) {
        this.profileGameRepository = profileGameRepository;
        this.createProfileGameUseCase = createProfileGameUseCase;
        this.gameRepository = gameRepository;
    }

    public void execute(ObjectId userId, UpdateFavoriteGameDTO dto) {

        Set<Integer> uniqueOrders = new HashSet<>();
        Set<ObjectId> newFavoriteIds = new HashSet<>();

        List<UpdateFavoriteGameDTO.FavoriteGameOrderDTO> favoriteDtos = dto.getFavorites();

        for (UpdateFavoriteGameDTO.FavoriteGameOrderDTO fav : favoriteDtos) {
            if (!uniqueOrders.add(fav.getOrder())) {
                throw new IllegalArgumentException("Favorite order must be unique: " + fav.getOrder());
            }

            ObjectId gameId = new ObjectId(fav.getGameId());
            if (!newFavoriteIds.add(gameId)) {
                throw new IllegalArgumentException("Duplicate gameId found: " + gameId);
            }
        }

        List<ProfileGame> currentFavorites = profileGameRepository.findByUserIdAndFavoriteIsTrue(userId);

        List<ProfileGame> existingProfileGames = profileGameRepository.findByUserIdAndGameIdIn(userId, new ArrayList<>(newFavoriteIds));
        Map<ObjectId, ProfileGame> existingMap = existingProfileGames.stream()
                .collect(Collectors.toMap(ProfileGame::getGameId, Function.identity()));
        Set<ObjectId> missingGameIds = new HashSet<>(newFavoriteIds);
        missingGameIds.removeAll(existingMap.keySet());
        Map<ObjectId, Game> gamesMap = gameRepository.findByIdIn(missingGameIds).stream()
                .collect(Collectors.toMap(Game::getId, Function.identity()));

        for (UpdateFavoriteGameDTO.FavoriteGameOrderDTO fav : favoriteDtos) {
            ObjectId gameId = new ObjectId(fav.getGameId());
            int order = fav.getOrder();

            if (existingMap.containsKey(gameId)) {
                ProfileGame profileGame = existingMap.get(gameId);
                profileGame.setFavorite(true);
                profileGame.setFavoriteOrder(order);
                profileGameRepository.save(profileGame);
            } else {
                Game game = gamesMap.get(gameId);
                if (game == null) {
                    throw new IllegalArgumentException("Game not found for id: " + gameId);
                }

                logger.info("Profile game with id " + gameId + " does not exist, creating...");
                CreateUpdateProfileGameCommand profileGameCommand = new CreateUpdateProfileGameCommand();
                profileGameCommand.setGameId(gameId);
                profileGameCommand.setUserId(userId);
                profileGameCommand.setFavorite(true);
                profileGameCommand.setFavoriteOrder(order);
                profileGameCommand.setGameName(game.getName());
                profileGameCommand.setGameSlug(game.getSlug());
                profileGameCommand.setGameCover(game.getCover());

                createProfileGameUseCase.execute(profileGameCommand);
            }
        }

        List<ProfileGame> toUnfavorite = currentFavorites.stream()
                .filter(pg -> !newFavoriteIds.contains(pg.getGameId()))
                .collect(Collectors.toList());

        for (ProfileGame game : toUnfavorite) {
            game.setFavorite(false);
            game.setFavoriteOrder(null);
            profileGameRepository.save(game);
        }
    }

}