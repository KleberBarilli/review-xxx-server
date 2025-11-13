package com.idealizer.review_x.common.dtos.user;

import com.idealizer.review_x.common.validators.ValidUrl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateUserDTO(
        @Size(min = 1, max = 255) @Schema(defaultValue = "john doe")
        String fullName,
        @Size(min = 1, max = 500)   @Schema(defaultValue = "\uD83C\uDFAE Story-driven game enthusiast, open-world explorer," +
                " and lover of immersive RPGs. Add in epic soundtracks, moral choices, and deep lore" +
                " — I’m all in. From Night City to Skellige, I’ll take both.")
        String bio,
        @URL @Schema(defaultValue = "https://steamcommunity.com/id/gaules")
        String steamUrl,
        @URL @Schema(defaultValue = "https://psnprofiles.com/Zenoxio")
        String psnUrl,
        @URL @Schema(defaultValue = "https://www.xbox.com/pt-BR/play/user/Skyyz4927?gamertag=Skyyz#4927")
        String xboxUrl,
        @URL @Schema(defaultValue = "https://letterboxd.com/barbiest/")
        String letterboxdUrl,
        @URL @Schema(defaultValue = "http://x.com/elonmusk")
        String twitterUrl,
        @URL @Schema(defaultValue = "https://www.instagram.com/leomessi/")
        String instagramUrl,
        @URL @Schema(defaultValue = "https://www.reddit.com/user/skyyzBR/")
        String redditUrl,
        @URL @Schema(defaultValue = "https://www.youtube.com/@FlowGamesPodcast")
        String youtubeUrl,
        @URL @Schema(defaultValue = "https://www.discordapp/users/351891565649592321")
        String discordUrl
) {
}
