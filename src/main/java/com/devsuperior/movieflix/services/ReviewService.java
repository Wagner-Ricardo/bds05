package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService  {

	@Autowired
	private ReviewRepository repository;
	
	@Autowired
	private AuthService authService;
	
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Transactional(readOnly = true)
	public ReviewDTO findById(Long id) {
		Optional<Review> obj = repository.findById(id);
		Review entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ReviewDTO(entity);
	}

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {
		User user = authService.authenticated();
		
		     Review entity = new Review();
		     entity.setUser(user);
		     entity.setText(dto.getText());
		     entity.setMovie(movieRepository.getOne(dto.getMovieId()));
		     entity = repository.save(entity);
		    
		     
		    return new ReviewDTO(entity);
	}
	
	public List<ReviewDTO> findByMovie(Long movieId) {
		List<Review> list = repository.findByMoviesId(movieId);
		return list.stream().map(x -> new ReviewDTO(x)).collect(Collectors.toList());
		
	}
	

  	
	
}
