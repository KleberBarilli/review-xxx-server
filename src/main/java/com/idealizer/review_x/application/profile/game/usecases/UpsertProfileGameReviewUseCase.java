package com.idealizer.review_x.application.profile.game.usecases;

import com.idealizer.review_x.application.profile.game.commands.UpsertProfileGameReviewCommand;
import com.idealizer.review_x.common.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class UpsertProfileGameReviewUseCase {

    private static final Logger logger = Logger.getLogger(UpsertProfileGameReviewUseCase.class.getName());

    private final MessageUtil messageUtil;

    public UpsertProfileGameReviewUseCase(MessageUtil messageUtil) {
        this.messageUtil = messageUtil;
    }
    public void execute (UpsertProfileGameReviewCommand command) {

        logger.info("Executing UpsertProfileGameReviewUseCase..." + command);

    }
}
