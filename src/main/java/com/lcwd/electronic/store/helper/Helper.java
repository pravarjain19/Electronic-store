package com.lcwd.electronic.store.helper;

import com.lcwd.electronic.store.dtos.PageResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class    Helper  {

    public static  <U,V> PageResponse<V> getPageableResponse( Page<U> page , Class<V> type){

        List<U> entity = page.getContent();
        List<V> dtoList = entity.stream().map(object -> new ModelMapper().map(object , type )).toList();


        PageResponse<V> response  = new PageResponse<>();

        response.setContent(dtoList);
        response.setTotalPages(page.getTotalPages());
        response.setIslastPage(page.isLast());
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElement(page.getTotalElements());

        return response;
    }
}
