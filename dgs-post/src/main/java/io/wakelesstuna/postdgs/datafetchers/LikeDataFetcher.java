package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.*;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.LikeInput;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.service.LikeService;
import lombok.RequiredArgsConstructor;

/**
 * This class is the DataFetcher for like fields on {@link Post}
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
public class LikeDataFetcher {

    private final LikeService likeService;

    @DgsMutation
    public String likePost(@InputArgument LikeInput likeInput) {
        return likeService.likePost(likeInput);
    }

    @DgsMutation
    public String unLikePost(@InputArgument LikeInput likeInput) {
        return likeService.unLikePost(likeInput);
    }

    @DgsQuery
    public boolean isPostLiked(@InputArgument LikeInput likeInput) {
        return likeService.isPostLiked(likeInput);
    }

    @DgsData(parentType = DgsConstants.POST.TYPE_NAME)
    public Integer totalLikes(DgsDataFetchingEnvironment dfe) {
        Post post = dfe.getSource();
        return likeService.getTotalLikes(post.getId());
    }

}
