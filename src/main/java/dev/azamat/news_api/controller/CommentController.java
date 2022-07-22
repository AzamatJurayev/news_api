package dev.azamat.news_api.controller;

import dev.azamat.news_api.dto.ApiResponse;
import dev.azamat.news_api.entity.Comment;
import dev.azamat.news_api.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("comment")
public class CommentController {

    @Autowired
    CommentService commentService;
    @PreAuthorize("hasAuthority('READ_CATEGORY')")
    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(commentService.getAll());
    }
    @PreAuthorize("hasAnyRole('ADMIN','VENTOR')")
//    @PreAuthorize(value = "hasAuthority('ADD_CATEGORY')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Comment comment){
        ApiResponse save = commentService.save(comment);
        return ResponseEntity.status(save.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(save);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        ApiResponse response = commentService.getOne(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }
    @PreAuthorize("hasAuthority('UPDATE_CATEGORY')")
    @PutMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,@RequestBody Comment comment){
        ApiResponse response = commentService.edit(id, comment);
        return ResponseEntity.status(response.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(response);
    }
    @PreAuthorize("hasAuthority('DELETE_CATEGORY')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        ApiResponse response = commentService.delete(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 404).body(response);
    }
}
