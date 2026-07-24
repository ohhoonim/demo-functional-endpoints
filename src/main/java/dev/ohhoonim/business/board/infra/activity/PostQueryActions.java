package dev.ohhoonim.business.board.infra.activity;

import java.util.List;
import java.util.Optional;
import dev.ohhoonim.business.board.activity.PostQueryActivity;
import dev.ohhoonim.business.board.activity.out.PostRepository;
import dev.ohhoonim.business.board.application.SearchCondition;
import dev.ohhoonim.business.board.model.Post;
import dev.ohhoonim.business.board.model.PostComponent.User;
import dev.ohhoonim.business.board.model.PostId;
import dev.ohhoonim.component.model.paging.PageRequest;
import dev.ohhoonim.component.model.paging.PagedData;
import dev.ohhoonim.component.model.unit.Activity;
import io.jsonwebtoken.lang.Collections;

@Activity
public class PostQueryActions implements PostQueryActivity {

    private final PostRepository postRepository;

    public PostQueryActions(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public PagedData<Post> searchPost(SearchCondition searchCondition, PageRequest pageRequest) {
        int totalCount = postRepository.getTotalCount(searchCondition);
        if (totalCount == 0) {
            return new PagedData<>(Collections.emptyList(), pageRequest.toPaged(0));
        }

        List<Post> posts = postRepository.findByCondition(searchCondition, pageRequest);
        return new PagedData<>(posts, pageRequest.toPaged(totalCount));
    }

    @Override
    public Optional<Post> detailPost(PostId postId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'detailPost'");
    }

    @Override
    public PagedData<Post> searchPostByUser(User user, PageRequest pageRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchPostByUser'");
    }

}
