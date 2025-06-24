package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TopicControllerTest {

    @InjectMocks
    private TopicController topicController;

    @Mock
    private TopicService topicService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TopicRepository topicRepository;
    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTopics_shouldReturnList() {
        List<TopicDto> dtos = List.of(new TopicDto());
        when(topicService.getAllTopics()).thenReturn(dtos);

        List<TopicDto> result = topicController.getAllTopics();

        assertThat(result).isEqualTo(dtos);
        verify(topicService).getAllTopics();
    }

    @Test
    void getUserSubscriptions_shouldReturnIds_whenAuthenticated() {
        when(principal.getName()).thenReturn("user");
        Topic topic = new Topic();
        topic.setId(42L);
        when(topicRepository.findAbonnementsByUsername("user")).thenReturn(List.of(topic));

        ResponseEntity<List<Long>> response = topicController.getUserSubscriptions(principal);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).containsExactly(42L);
    }

    @Test
    void getUserSubscriptions_shouldReturn401_whenNoPrincipal() {
        ResponseEntity<List<Long>> response = topicController.getUserSubscriptions(null);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    void subscribeToTopic_shouldAddSubscription() {
        when(principal.getName()).thenReturn("user");
        User user = new User();
        user.setAbonnements(new java.util.HashSet<>());
        Topic topic = new Topic();
        topic.setAbonnes(new java.util.HashSet<>());
        when(userRepository.findWithAbonnementsByUsername("user")).thenReturn(Optional.of(user));
        when(topicRepository.findByIdWithAbonnes(1L)).thenReturn(Optional.of(topic));

        ResponseEntity<?> response = topicController.subscribeToTopic(1L, principal);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(user.getAbonnements()).contains(topic);
        assertThat(topic.getAbonnes()).contains(user);
        verify(userRepository).save(user);
        verify(topicRepository).save(topic);
    }

    @Test
    void unsubscribeFromTopic_shouldRemoveSubscription() {
        when(principal.getName()).thenReturn("user");
        User user = new User();
        Topic topic = new Topic();
        user.setAbonnements(new java.util.HashSet<>(Set.of(topic)));
        topic.setAbonnes(new java.util.HashSet<>(Set.of(user)));
        when(userRepository.findWithAbonnementsByUsername("user")).thenReturn(Optional.of(user));
        when(topicRepository.findByIdWithAbonnes(1L)).thenReturn(Optional.of(topic));

        ResponseEntity<?> response = topicController.unsubscribeFromTopic(1L, principal);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(user.getAbonnements()).doesNotContain(topic);
        verify(userRepository).save(user);
    }
}