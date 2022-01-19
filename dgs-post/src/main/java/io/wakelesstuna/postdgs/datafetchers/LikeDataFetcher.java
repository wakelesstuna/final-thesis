package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.*;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.LikeInput;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.service.LikeService;
import lombok.RequiredArgsConstructor;

/**
 * This class is the DataFetcher.
 * It handles the mutations of the liking post type.
 *
 * @author oscar.steen.forss
 */
@DgsComponent
@RequiredArgsConstructor
public class LikeDataFetcher {

    private final LikeService likeService;

    @DgsData(parentType = DgsConstants.MUTATION.TYPE_NAME, field = DgsConstants.MUTATION.LikePost)
    public String likePost(@InputArgument LikeInput likeInput) {
        return likeService.likePost(likeInput);
    }

    @DgsMutation
    public String unLikePost(@InputArgument LikeInput likeInput) {
        return likeService.unLikePost(likeInput);
    }

    @DgsData(parentType = DgsConstants.QUERY.TYPE_NAME, field = DgsConstants.QUERY.IsPostLiked)
    public boolean isPostLiked(@InputArgument LikeInput likeInput) {
        return likeService.isPostLiked(likeInput);
    }

    @DgsData(parentType = DgsConstants.POST.TYPE_NAME, field = DgsConstants.POST.TotalLikes)
    public Integer totalLikes(DgsDataFetchingEnvironment dfe) {
        Post post = dfe.getSource();
        return likeService.getTotalLikes(post.getId());
    }

}
