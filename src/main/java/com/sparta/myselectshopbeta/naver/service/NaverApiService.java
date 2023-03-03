package com.sparta.myselectshopbeta.naver.service;

import com.sparta.myselectshopbeta.naver.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NaverApiService {
    //네이버 검색 API 활용
    public List<ItemDto> searchItems(String query) {
        // RestTemplate, HttpHeaders 어디에서 온거지? -> naver developers document 참고
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Naver-Client-Id", "VJLs3pqxel0mJkCG7wqz");
        headers.add("X-Naver-Client-Secret", "rcuOE8tEvp");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?display=15&query=" + query , HttpMethod.GET, requestEntity, String.class);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        log.info("NAVER API Status Code : " + status);

        String response = responseEntity.getBody();

        return fromJSONtoItems(response);
    }

    // 위의 JSON을 DTO로 변환
    public List<ItemDto> fromJSONtoItems(String response) {
        // 가져온 JSON 형식을 rjson에 넣어준다
        JSONObject rjson = new JSONObject(response);
        //getJSONArray를 사용해 key가 item인 것들만 가져와서 items에 넣어준다.
        // Q. 위에껀 JSONObject에 그냥 넣어주는데 이건 왜 JSONArray로 넣어주지? , 배열로 바꾸는 이유는? for문 돌리기 위해서인가?
        JSONArray items  = rjson.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        //향상된 for문으로 바꿔보기
        // for문을 돌려 itemDtoList에 하나씩 넣어준다.
        for (int i=0; i<items.length(); i++) {
            JSONObject itemJson = items.getJSONObject(i);
            ItemDto itemDto = new ItemDto(itemJson);
            itemDtoList.add(itemDto);
        }
        //List로 만들어 클라이언트에 돌려준다.
        return itemDtoList;
    }
}