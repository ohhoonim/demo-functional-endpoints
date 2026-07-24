package dev.ohhoonim.business.board.infra.adapter;

import java.util.List;
import org.springframework.jdbc.core.simple.JdbcClient;
import dev.ohhoonim.business.board.activity.out.PostRepository;
import dev.ohhoonim.business.board.application.SearchCondition;
import dev.ohhoonim.business.board.model.Post;
import dev.ohhoonim.component.model.paging.PageRequest;
import dev.ohhoonim.component.model.unit.Adapter;

@Adapter
public class PostAdapter implements PostRepository {

    private final JdbcClient jdbcClient;

    public PostAdapter (JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Post> findByCondition(SearchCondition searchCondition, PageRequest pageRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByCondition'");
    }

    @Override
    public int getTotalCount(SearchCondition searchCondition) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalCount'");
    }
    
}
