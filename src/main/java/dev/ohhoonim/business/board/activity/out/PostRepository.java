package dev.ohhoonim.business.board.activity.out;

import java.util.List;
import dev.ohhoonim.business.board.application.SearchCondition;
import dev.ohhoonim.business.board.model.Post;
import dev.ohhoonim.component.model.paging.PageRequest;

/**
 * PostRepository
 */
public interface PostRepository {

    List<Post> findByCondition(SearchCondition searchCondition, PageRequest pageRequest);

    int getTotalCount(SearchCondition searchCondition);

}
