package com.example.demo.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {

    /*
    가짜 request, response를 이용해서 Test.
    Web과 관련된 Bean들만 등록하여 Test, Slice Test라고 함.
    Web Server를 띄우는 Test보다는 빠르지만 Unit Test보다는 느린 중간 정도의 test.
     */
    @Autowired
    MockMvc mockMvc;

    /*
        Autowired?
        ObjectMapper?
     */
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;


    @Test
    public void createEvent() throws Exception {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Developerment with springboot")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,9,8,18,57))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,9,8,18,57))
                .beginEventDateTime(LocalDateTime.of(2019,9,8,18,57))
                .endEventDateTime(LocalDateTime.of(2019,9,8,18,57))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이태원 메이드ㅋㅋㅋㅋ ")
                .build();

        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);


        // perform 안이 요청, 요청이 갔으면 응답이 있음.
        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE));
    }

}