package dev.azamat.news_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArticleDto {

    private String title;

    private String commentInfo;

    private List<String> categories;

}
