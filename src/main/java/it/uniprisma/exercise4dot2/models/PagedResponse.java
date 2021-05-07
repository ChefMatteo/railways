package it.uniprisma.exercise4dot2.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResponse<T> {
    List<T> data;
    String index;
    String elements;
    Long totalElements;
}
