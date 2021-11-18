package data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import domain.admin.*;
import errors.ResourceNotFoundException;
import errors.UserNotFoundException;

public class CommentDB implements CommentRepository{

	private static final Map<String,Comment> COMMENTS_STORE = new ConcurrentHashMap();
	
	@Override
	public String create(Comment newComment) {
        Comment comment = Comment.builder()
                .id(newComment.getId())
                .commenter(newComment.getCommenter())
                .text(newComment.getText())
                .build();
        COMMENTS_STORE.put(newComment.getId(), comment);

            return newComment.getId();
	}

	@Override
	public List<Comment> getComments() {
		return new ArrayList<>( COMMENTS_STORE.values());
	}

	@Override
	public void deleteComment(String id) throws ResourceNotFoundException {
        Comment deleteComment = Optional.of(COMMENTS_STORE.get(id)).orElseThrow(()->  new UserNotFoundException(404, "Comment id not found."));
        COMMENTS_STORE.remove(deleteComment.getId(),deleteComment);
	}

	@Override
	public Comment updateComment(Comment comment) throws ResourceNotFoundException {
		Optional.of(COMMENTS_STORE.get(comment.getId())).orElseThrow(()->  new UserNotFoundException(404, "Comment id not found."));
		COMMENTS_STORE.replace(comment.getId(), comment);
        return  comment;
	}

}