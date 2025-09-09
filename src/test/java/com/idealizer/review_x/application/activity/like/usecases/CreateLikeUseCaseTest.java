package com.idealizer.review_x.application.activity.like.usecases;

import com.idealizer.review_x.application.activity.like.ports.LikeCounterPort;
import com.idealizer.review_x.application.activity.like.services.LikeCounterRegistryService;
import com.idealizer.review_x.domain.core.activity.like.entities.Like;
import com.idealizer.review_x.domain.core.activity.like.entities.LikeType;
import com.idealizer.review_x.domain.core.activity.like.repositories.LikeRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.mockito.Mockito.*;
class CreateLikeUseCaseTest {

    private LikeRepository likeRepository;
    private LikeCounterRegistryService registry;
    private LikeCounterPort reviewCounter;
    private CreateLikeUseCase useCase;

    @BeforeEach
    void setUp() {
        likeRepository = mock(LikeRepository.class);
        registry = mock(LikeCounterRegistryService.class);
        reviewCounter = mock(LikeCounterPort.class);

        when(registry.get(LikeType.REVIEW)).thenReturn(reviewCounter);

        useCase = new CreateLikeUseCase(likeRepository, registry);
    }

    @Test
    void shouldInsertLikeAndIncCounterOnSuccess() {
        var userId = new ObjectId();
        var targetId = new ObjectId();
        when(likeRepository.insert(ArgumentMatchers.any(Like.class)))
                .thenReturn(null);

        useCase.execute(userId, targetId, LikeType.REVIEW);

        verify(likeRepository, times(1)).insert(any(Like.class));
        verify(reviewCounter, times(1)).inc(targetId, +1);
        verifyNoMoreInteractions(reviewCounter);
    }

    @Test
    void shouldNotIncCounterOnDuplicate() {
        var userId = new ObjectId();
        var targetId = new ObjectId();
        var dup = mock(com.mongodb.DuplicateKeyException.class);
        when(likeRepository.insert(any(Like.class))).thenThrow(dup);

        useCase.execute(userId, targetId, LikeType.REVIEW);

        verify(likeRepository, times(1)).insert(any(Like.class));
        verify(reviewCounter, never()).inc(any(), anyInt());
    }
}
