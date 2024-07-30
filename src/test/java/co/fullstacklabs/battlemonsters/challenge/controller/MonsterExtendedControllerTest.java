package co.fullstacklabs.battlemonsters.challenge.controller;

import co.fullstacklabs.battlemonsters.challenge.ApplicationConfig;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@Import(ApplicationConfig.class)
public class MonsterExtendedControllerTest {
    protected static final String MONSTER_PATH = "/monster";
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenDeleteInexistingMonster_thenResponseIsNotFound() throws Exception {
        mockMvc.perform(delete(MONSTER_PATH + "/9999")).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateReturning201StatusCodeAndDeleteReturning200StatusCode() throws Exception {
    	MonsterDTO localMonster = new MonsterDTO();
    	
    	localMonster.setId(25);
    	localMonster.setName("Kraken");
    	localMonster.setHp(100);
    	localMonster.setAttack(40);
    	localMonster.setDefense(70);
    	localMonster.setSpeed(45);
    	localMonster.setImageUrl("url");
    	
    	
    	mockMvc.perform(post(MONSTER_PATH)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(localMonster))
    			.accept(MediaType.APPLICATION_JSON))
    	.andExpect(status().isCreated());
    	
    	mockMvc.perform(delete(MONSTER_PATH + "/4")).andExpect(status().isOk());

    }

    @Test
    void shouldDeleteMonsterReturning404StatusCode() throws Exception {
    	mockMvc.perform(delete(MONSTER_PATH + "/8888")).andExpect(status().isNotFound());
    }

    @Test
    void testImportCsvSuccessfullyReturning200StatusCode() throws Exception {
    	final byte[] data = Files.readAllBytes(Paths.get("data/monsters-correct.csv"));
    	mockMvc.perform(multipart(MONSTER_PATH + "/import").file("file", data)
    			.accept(MediaType.TEXT_PLAIN))
    	.andExpect(status().isOk());
    }

    @Test
    @ExceptionHandler
    void testImportCsvNonexistentColumnsReturningInternalServerError() throws Exception {
       	final byte[] data = Files.readAllBytes(Paths.get("data/monsters-empty-monster.csv"));
    	mockMvc.perform(multipart(MONSTER_PATH + "/import").file("file", data)
    			.contentType(MediaType.TEXT_PLAIN))
    	.andExpect(status().is5xxServerError());
    }

}
