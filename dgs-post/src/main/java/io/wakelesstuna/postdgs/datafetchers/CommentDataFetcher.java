package io.wakelesstuna.postdgs.datafetchers;

import com.netflix.graphql.dgs.*;
import io.wakelesstuna.post.generated.DgsConstants;
import io.wakelesstuna.post.generated.types.Comment;
import io.wakelesstuna.post.generated.types.CommentInput;
import io.wakelesstuna.post.generated.types.CreateCommentInput;
import io.wakelesstuna.post.generated.types.Post;
import io.wakelesstuna.postdgs.dataloader.CommentsDataLoader;
import io.wakelesstuna.postdgs.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class is the DataFetcher for {@link Comment}.
 *
 * @author oscar.steen.forss
 */
@RequiredArgsConstructor
@DgsComponent
public class CommentDataFetcher {

    private final CommentService commentService;

    @DgsMutation
    public Comment createComment(@InputArgument CreateCommentInput createCommentInput) {
        return commentService.createComment(createCommentInput);
    }

    @DgsMutation
    public String deleteComment(@InputArgument CommentInput commentInput) {
        return commentService.deleteComment(commentInput);
    }

    @DgsData(parentType = DgsConstants.POST.TYPE_NAME)
    public Integer totalComments(DgsDataFetchingEnvironment dfe) {
        Post post = dfe.getSource();
        return commentService.getTotalComments(post.getId());
    }

    @DgsData(parentType = DgsConstants.POST.TYPE_NAME)
    public CompletableFuture<Comment> comments(DgsDataFetchingEnvironment dfe) {
        DataLoader<UUID, Comment> dataLoader = dfe.getDataLoader(CommentsDataLoader.class);
        Post post = dfe.getSource();
        return dataLoader.load(post.getId());
    }
}
